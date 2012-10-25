/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */
@Named
@SessionScoped
public class treningsOktBehandler implements Serializable {

    public synchronized List<OktStatus> getTemptreningsOkter() {
        temptreningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
        tempOkt.nullstill();
        temptreningsOkter.add(new OktStatus(tempOkt));
        return temptreningsOkter;
    }
    private List<OktStatus> DBtreningsobjekter = Collections.synchronizedList(new ArrayList<OktStatus>());
    private Oversikt nyOversikt = new Oversikt();
    private List<OktStatus> treningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
    private List<OktStatus> temptreningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
    private List<OktStatus> hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
    private TreningsOkt tempOkt = new TreningsOkt();
    private @NotNull
    @Range(min = 0, max = 12)
    int maned = 0;
    private int mick = 0;
    private boolean nyOkt = false;

    public synchronized boolean isNyOkt() {
        return nyOkt;
    }

    public synchronized void setNyOkt(boolean nyOkt) {
        this.nyOkt = nyOkt;
    }

    public synchronized boolean getDatafins() {
        if ((getManed() >= 1)) {
            return (!hjelp.isEmpty());
        }
        return (!treningsOkter.isEmpty());
    }

    public synchronized List<OktStatus> getTabelldata() {
        Boolean sjekk = false;
        try{
        if ((getManed() >= 1)) {
            return getPaManed(getManed());
        } else if ((getManed() == 0)) {
            return treningsOkter;

        } 
        } catch (ConcurrentModificationException e){
            getTabelldata();
        }
        return treningsOkter;
    }

    public synchronized int getAntOkter() {
        return treningsOkter.size();
    }

    public synchronized int getGjennomsnitt() {
        int max = 0;
        int indeks = 0;
        for (OktStatus t : treningsOkter) {
            max += t.getTreningsikOkt().getVarighet();
            indeks++;
        }
        if (indeks == 0) {
            indeks = 1;

        }
        return max / indeks;
    }

    public synchronized String getNavn() {
        return nyOversikt.getBruker();
    }

    public synchronized TreningsOkt getTempOkt() {
        return tempOkt;
    }

    public synchronized List<OktStatus> getPaManed(int m) {
        try {
            hjelp.clear();
            for (OktStatus k : treningsOkter) {
                if (k.getTreningsikOkt().getDate().getMonth() == (m - 1)) {
                    hjelp.add(k);
                }
            }
        } catch (ConcurrentModificationException e) {
            getPaManed(m);
        }
        return hjelp;
    }

    public synchronized void setTempOkt(TreningsOkt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized String oppdater() {

        nyOkt = false;

        try {
            if (!(tempOkt.getVarighet() == 0)) {
                TreningsOkt nyOkt;
                nyOkt = new TreningsOkt(tempOkt.getOktNr(), tempOkt.getDate(),
                        tempOkt.getVarighet(), tempOkt.getKategori(),
                        tempOkt.getTekst(), tempOkt.getBrukernavn());

                nyOversikt.registrerNyOkt(nyOkt);
                treningsOkter.add(new OktStatus(nyOkt));
                registrerTreningsOkt(nyOkt);
                tempOkt.nullstill();
            }

            int indeks = treningsOkter.size() - 1;

            while (indeks >= 0) {
                OktStatus ts = treningsOkter.get(indeks);
                if (ts.getSkalSlettes()) {
                    for (TreningsOkt e : nyOversikt.getAlleOkter()) {
                        if (e.equals(ts.getTreningsikOkt())) {
                            slettTreningsOkt(e);
                            nyOversikt.slettOkt(e);
                            
                        }
                    }
                    treningsOkter.remove(indeks);

                }
                indeks--;
            }
            List<OktStatus> sjekk = updateArray(); 
            if (!sjekk.isEmpty()) {
                nyOversikt.slettAlle();
                treningsOkter.clear();


                for (OktStatus s : sjekk) {
                    nyOversikt.registrerNyOkt(s.getTreningsikOkt());
                    treningsOkter.add(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            oppdater();
        }
        return "success";
    }

    public synchronized List<OktStatus> updateArray() {
        TreningsOkt hjelpeobjekt;
        DBtreningsobjekter.clear();
        DBConnection conn = new DBConnection();
        Statement st = null;

        ResultSet rs = null;
        try {
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                hjelpeobjekt = new TreningsOkt(rs.getInt("OKTNR"), rs.getDate("DATO"),
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"),
                        rs.getString("TEKST"), rs.getString("BRUKERNAVN"));
                DBtreningsobjekter.add(new OktStatus(hjelpeobjekt));

            }
        } catch (SQLException e) {
            conn.failed(); //Rollback
        } finally {
            conn.closeS(st);
            conn.closeR(rs);
            conn.close();
            if (!DBtreningsobjekter.isEmpty()) {
                return DBtreningsobjekter;
            } else {
                DBtreningsobjekter.clear();
                return DBtreningsobjekter;
            }
        }
    }

    public synchronized int getManed() {
        return maned;
    }

    public synchronized void setManed(int Maned) {
        this.maned = Maned;
    }

    public synchronized boolean registrerTreningsOkt(TreningsOkt mick) {
        //oktnr blir autogenerert i databasen
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {

            st = conn.getConn().createStatement();

            String sb = "";

            sb += "INSERT INTO TRENING";
            sb += "(dato, varighet, kategorinavn, tekst, brukernavn)";
            sb += "VALUES ( ";
            sb += "  '" + mick.getSqlDate() + "'";
            sb += ", " + mick.getVarighet() + " ";
            sb += ", '" + mick.getKategori() + "' ";
            sb += ", '" + mick.getTekst() + "' ";
            sb += ", 'anne'";
            sb += ")";
            System.out.println(sb);


            st.executeUpdate(sb);

            return true;

        } catch (SQLException e) {
            conn.failed();
            return false;
        } finally {
            conn.closeS(st);
            conn.close();
            updateArray();
        }


    }

    public boolean slettTreningsOkt(TreningsOkt objekt) {
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM WAPLJ.TRENING WHERE OKTNR = "
                    + objekt.getOktNr());
            return true;

        } catch (SQLException e) {
            conn.failed();
            return false;

        } finally {
            conn.closeS(st);
            conn.close();
        }

    }
}
