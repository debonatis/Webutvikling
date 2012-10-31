/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private boolean nyOkt = false;
    private FacesMessage fm = new FacesMessage();
    private FacesContext fc;

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
        try {
            if ((getManed() >= 1)) {
                return getPaManed(getManed());
            } else if ((getManed() == 0)) {
                return treningsOkter;

            }
        } catch (ConcurrentModificationException e) {
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
                     
            
            int indeks = treningsOkter.size() - 1;
            if (!treningsOkter.isEmpty()) {
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
            }
            if (!treningsOkter.isEmpty()) {
                for (OktStatus j : treningsOkter) {
                    if (j.getTreningsikOkt().isEndret()) {
                        j.getTreningsikOkt().setEndret(false);
                        oppdaterTreningsOktDB(treningsOkter);
                    }
                }
            }

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
            getAlleTreningsOkter(); 


        } catch (ConcurrentModificationException e) {
            oppdater();
        }
        return "success";
    }

    public synchronized void getAlleTreningsOkter() {
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
                nyOversikt.slettAlle();
                treningsOkter.clear();
                for (OktStatus s : DBtreningsobjekter) {
                    nyOversikt.registrerNyOkt(s.getTreningsikOkt());
                    treningsOkter.add(s);
                }
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
            sb += ", '" + mick.getBrukernavn() + "'";
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
            getAlleTreningsOkter();
        }


    }

    public boolean slettTreningsOkt(TreningsOkt objekt) {
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM WAPLJ.TRENING WHERE OKTNR =" + objekt.getOktNr());
            fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "Sletting utført!", "ja,Sletting utført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();

            return true;



        } catch (SQLException e) {
            conn.failed();
            return false;

        } finally {
            conn.closeS(st);
            conn.close();
        }

    }

    public synchronized boolean oppdaterTreningsOktDB(List<OktStatus> liste) {
        DBConnection conn = new DBConnection();
        Statement st = null;
        PreparedStatement oppdaterOkter = null;
        String oppdaterString =
                "update WAPLJ.TRENING "
                + "set DATO = ?, VARIGHET= ?, "
                + "KATEGORINAVN= ?, TEKST= ? "
                + "where OKTNR = ? AND BRUKERNAVN= ?";

        try {
            conn.getConn().setAutoCommit(false);
            oppdaterOkter = conn.getConn().prepareStatement(oppdaterString);
            for (OktStatus f : liste) {
                oppdaterOkter.setDate(1, f.getTreningsikOkt().getSqlDate());
                oppdaterOkter.setInt(2, f.getTreningsikOkt().getVarighet());
                oppdaterOkter.setString(3, f.getTreningsikOkt().getKategori());
                oppdaterOkter.setString(4, f.getTreningsikOkt().getTekst());
                oppdaterOkter.setInt(5, f.getTreningsikOkt().getOktNr());
                oppdaterOkter.setString(6, f.getTreningsikOkt().getBrukernavn());
                oppdaterOkter.executeUpdate();
                conn.getConn().commit();

            }
            return true;

        } catch (SQLException e) {
            conn.failed();
            if (conn.getConn() != null) {
                try {
                    System.err.print("Endring har blitt trekk tilbake");
                    conn.getConn().rollback();
                } catch (SQLException excep) {
                    conn.failed();
                }
            }
            return false;

        } finally {
            conn.closeS(st);
            conn.close();
        }


    }
}
