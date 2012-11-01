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
    private ArrayList<TreningsOkt> hjelp2 = new ArrayList<>();
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
            return (!hjelp2.isEmpty());
        }
        return (!treningsOkter.isEmpty());
    }

    public synchronized List<OktStatus> getTabelldata() {       
        hjelp.clear();
        int m; 
        m= getManed();
        if ((getManed() >= 1)) {            
            hjelp2 = nyOversikt.getPaManed(m);
            try {
                for (TreningsOkt g : hjelp2) {
                    hjelp.add(new OktStatus(g));
                }                
                return hjelp;
            } catch (ConcurrentModificationException e) {
                getTabelldata();
            }
            setManed(0);
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
            oppdaterTreningsOktDB();

            if (!(tempOkt.getVarighet() == 0)) {
                TreningsOkt nyOkt;
                nyOkt = new TreningsOkt(tempOkt.getOktNr(), tempOkt.getSqlDate(),
                        tempOkt.getVarighet(), tempOkt.getKategori(),
                        tempOkt.getTekst());

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
        hjelpeobjekt = new TreningsOkt();
        DBtreningsobjekter.clear();
        DBConnection conn = new DBConnection();
        Statement st = null;
        String bruker = "";
        ResultSet rs = null;
        try {
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                hjelpeobjekt = new TreningsOkt(rs.getInt("OKTNR"), rs.getDate("DATO"),
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"),
                        rs.getString("TEKST"));
                bruker = rs.getString("BRUKERNAVN");
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
            sb += ", 'anne'";
            sb += ")";
            System.out.println(sb);


            st.executeUpdate(sb);
            fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "Registrering fullført", "ja,Registreing fullført!");
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
            getAlleTreningsOkter();
        }


    }

    public synchronized boolean slettTreningsOkt(TreningsOkt objekt) {
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

    public synchronized boolean oppdaterTreningsOktDB() {
        String t = "anne";
        hjelp.clear();
        if (!treningsOkter.isEmpty()) {
            for (OktStatus j : treningsOkter) {
                if (j.getTreningsikOkt().isEndret()) {
                    j.getTreningsikOkt().setEndret(false);
                    hjelp.add(j);
                }
            }
        }

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
            for (OktStatus f : hjelp) {
                oppdaterOkter.setDate(1, f.getTreningsikOkt().getSqlDate());
                oppdaterOkter.setInt(2, f.getTreningsikOkt().getVarighet());
                oppdaterOkter.setString(3, f.getTreningsikOkt().getKategori());
                oppdaterOkter.setString(4, f.getTreningsikOkt().getTekst());
                oppdaterOkter.setInt(5, f.getTreningsikOkt().getOktNr());
                oppdaterOkter.setString(6, t);
                oppdaterOkter.executeUpdate();
                conn.getConn().commit();

            }
            fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "Oppdatering utført!", "ja,Oppdatering utført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
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
