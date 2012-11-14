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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */
@ManagedBean
@SessionScoped
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class treningsOktBehandler implements Serializable {

    private FacesMessage fm = new FacesMessage();
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
    final Object laas1 = new Object();
    private FacesContext fc;
    private TimeZone tidssone;

    public boolean isNyOkt() {
        return nyOkt;
    }

    public TimeZone getTidssone() {
        this.tidssone = TimeZone.getDefault();
        return tidssone == null ? TimeZone.getTimeZone("GMT") : tidssone;
    }

    public List<OktStatus> getTemptreningsOkter() {
        temptreningsOkter.clear();
        tempOkt = new TreningsOkt();
        temptreningsOkter.add(new OktStatus(tempOkt));
        return temptreningsOkter;
    }

    public synchronized void setNyOkt(boolean nyOkt) {
        this.nyOkt = nyOkt;
    }

    public boolean getDatafins() {

        if ((getManed() >= 1)) {
            getTabelldata();
            return (!hjelp.isEmpty());
        }
        return (!treningsOkter.isEmpty());

    }

    public List<OktStatus> getTabelldata() {

        int m;
        m = maned;
        if ((getManed() >= 1)) {
            hjelp.clear();
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
        return getTabelldata().size();
    }

    public synchronized int getGjennomsnitt() {
        int max = 0;
        int indeks = 0;
        for (OktStatus t : getTabelldata()) {
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

    @RolesAllowed("admin")
    public synchronized void slettAlleOkter() {
        try {
            for (OktStatus z : treningsOkter) {
                TreningsOkt k = z.getTreningsikOkt();
                nyOversikt.slettOkt(k);
                slettTreningsOkt(k, 1);
                treningsOkter.remove(z);
            }
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sletting av all data utført!", "ja,Sletting utført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();

        } catch (ConcurrentModificationException s) {
            slettAlleOkter();
        }

    }

    public String oppdater() {

        nyOkt = false;
        try {
            if (!(treningsOkter.isEmpty())) {
                for (OktStatus r : treningsOkter) {
                    if (r.getSkalSlettes()) {
                        for (TreningsOkt e : nyOversikt.getAlleOkter()) {
                            if (e.equals(r.getTreningsikOkt())) {
                                slettTreningsOkt(e, 0);
                                nyOversikt.slettOkt(e);
                            }
                        }
                        treningsOkter.remove(r);
                    }
                }
            }

            getAlleTreningsOkter();



            oppdaterTreningsOktDB();

            if (!(tempOkt.getVarighet() == 0)) {
                TreningsOkt nyOkt;
                nyOkt = new TreningsOkt(tempOkt.getOktNr(), new Date(tempOkt.getDate().getTime()),
                        tempOkt.getVarighet(), tempOkt.getKategori(),
                        tempOkt.getTekst());

                nyOversikt.registrerNyOkt(nyOkt);
                treningsOkter.add(new OktStatus(nyOkt));
                registrerTreningsOkt(nyOkt);
                tempOkt = new TreningsOkt();
            }

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
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING "
                    + "where BRUKERNAVN = '" + getNavn() + "'");


            while (rs.next()) {
                hjelpeobjekt = new TreningsOkt(rs.getInt("OKTNR"), new Date(rs.getDate("DATO").getTime()),
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"),
                        rs.getString("TEKST"));
                DBtreningsobjekter.add(new OktStatus(hjelpeobjekt));


            }
            conn.getConn().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alle Okter skaffet!", "ja,Okter skaffet!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
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

    public int getManed() {
        return maned;
    }

    public void setManed(int Maned) {

        this.maned = Maned;

    }

    public synchronized boolean registrerTreningsOkt(TreningsOkt okt) {
        //oktnr blir autogenerert i databasen
        DBConnection conn = new DBConnection();
        PreparedStatement reg = null;
        String regTekst = "INSERT INTO WAPLJ.TRENING (dato, varighet, kategorinavn, tekst, brukernavn) "
                + "VALUES (?,?,?,?,?)";
        try {

            conn.getConn().setAutoCommit(false);
            reg = conn.getConn().prepareStatement(regTekst);
            reg.setDate(1, okt.getSqlDate());
            reg.setInt(2, okt.getVarighet());
            reg.setString(3, okt.getKategori());
            reg.setString(4, okt.getTekst());
            reg.setString(5, getNavn());
            reg.executeUpdate();
            conn.getConn().commit();


            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nyregistrering fullført!", "ja,Nyregistreing fullført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();

            return true;

        } catch (SQLException e) {
            conn.failed();
            return false;
        } finally {
            conn.closeP(reg);
            conn.close();
            getAlleTreningsOkter();
        }


    }

    public synchronized boolean slettTreningsOkt(TreningsOkt objekt, int i) {
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM WAPLJ.TRENING WHERE OKTNR =" + objekt.getOktNr() + " AND BRUKERNAVN = '" + getNavn() + "'");
            st.getConnection().commit();
            if (i == 0) {
                fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sletting utført!", "ja,Sletting utført!");
                fc = FacesContext.getCurrentInstance();
                fc.addMessage("null", fm);
                fc.renderResponse();
            }

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

        hjelp.clear();
        if (!(treningsOkter.isEmpty())) {
            for (OktStatus j : treningsOkter) {
                if (j.getTreningsikOkt().isEndret()) {
                    j.getTreningsikOkt().setEndret(false);
                    hjelp.add(j);
                }
            }
        }


        DBConnection conn = new DBConnection();
        PreparedStatement oppdaterOkter = null;
        String oppdaterString =
                "update WAPLJ.TRENING "
                + "set DATO = ?, VARIGHET= ?, "
                + "KATEGORINAVN= ?, TEKST= ? "
                + "where OKTNR = ? AND BRUKERNAVN= ?";
        if (!hjelp.isEmpty()) {

            try {

                conn.getConn().setAutoCommit(false);
                oppdaterOkter = conn.getConn().prepareStatement(oppdaterString);
                for (OktStatus f : hjelp) {
                    oppdaterOkter.setDate(1, f.getTreningsikOkt().getSqlDate());
                    oppdaterOkter.setInt(2, f.getTreningsikOkt().getVarighet());
                    oppdaterOkter.setString(3, f.getTreningsikOkt().getKategori());
                    oppdaterOkter.setString(4, f.getTreningsikOkt().getTekst());
                    oppdaterOkter.setInt(5, f.getTreningsikOkt().getOktNr());
                    oppdaterOkter.setString(6, getNavn());
                    oppdaterOkter.executeUpdate();
                    conn.getConn().commit();

                }
                fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Oppdatering utført!", "ja,Oppdatering utført!");
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
                conn.closeS(oppdaterOkter);
                conn.close();
            }
        }
        return true;
    }
}
