/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.DBadm;

import com.corejsf.OktStatus;
import com.corejsf.TreningsOkt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author deb
 */
@RequestScoped
public class DBController {

    private FacesMessage fm;
    private FacesContext fc;
    private List<OktStatus> dBtreningsobjekter = Collections.synchronizedList(new ArrayList<OktStatus>());
    private List<OktStatus> hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());

    public synchronized List<OktStatus> getAlleTreningsOkter(String navn) {
        TreningsOkt hjelpeobjekt;
        dBtreningsobjekter.clear();
        DBConnection conn = new DBConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING "
                    + "where BRUKERNAVN = '" + navn + "'");


            while (rs.next()) {
                hjelpeobjekt = new TreningsOkt(rs.getInt("OKTNR"), new Date(rs.getDate("DATO").getTime()),
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"),
                        rs.getString("TEKST"));
                dBtreningsobjekter.add(new OktStatus(hjelpeobjekt));


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


            return dBtreningsobjekter;
        }
    }

    public synchronized void registrerTreningsOkt(TreningsOkt okt, String navn) {
        //oktnr blir autogenerert i databasen
        DBConnection conn = new DBConnection();
        PreparedStatement reg = null;
        String regTekst = "INSERT INTO WAPLJ.TRENING(dato, varighet, kategorinavn, tekst, brukernavn)"
                + " VALUES (?,?,?,?,?)";
        try {

            conn.getConn().setAutoCommit(false);
            reg = conn.getConn().prepareStatement(regTekst);
            reg.setDate(1, okt.getSqlDate());
            reg.setInt(2, okt.getVarighet());
            reg.setString(3, okt.getKategori());
            reg.setString(4, okt.getTekst());
            reg.setString(5, navn);
            reg.executeUpdate();
            conn.getConn().commit();


            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nyregistrering fullført!", "ja,Nyregistreing fullført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();



        } catch (SQLException e) {
            conn.failed();
        } finally {
            conn.closeP(reg);
            conn.close();
        }


    }

    public synchronized void slettTreningsOkt(TreningsOkt objekt, int i, String navn) {
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM WAPLJ.TRENING WHERE OKTNR =" + objekt.getOktNr() + " AND BRUKERNAVN = '" + navn + "'");
            st.getConnection().commit();
            if (i == 1) {
                fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sletting utført!", "ja,Sletting utført!");
                fc = FacesContext.getCurrentInstance();
                fc.addMessage("null", fm);
                fc.renderResponse();
            }





        } catch (SQLException e) {
            conn.failed();


        } finally {
            conn.closeS(st);
            conn.close();
        }

    }

    public synchronized void oppdaterTreningsOktDB(List<OktStatus> treningsOkter, String navn) {

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
                    oppdaterOkter.setString(6, navn);
                    oppdaterOkter.executeUpdate();
                    conn.getConn().commit();

                }
                fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Oppdatering utført!", "ja,Oppdatering utført!");
                fc = FacesContext.getCurrentInstance();
                fc.addMessage("null", fm);
                fc.renderResponse();


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


            } finally {
                conn.closeS(oppdaterOkter);
                conn.close();

            }

        }


    }

    public synchronized void slettAlleOkterDB() {

        DBConnection conn = new DBConnection();
        Statement st = null;

        try {
            st = conn.getConn().createStatement();
            st.execute("TRUNCATE TABLE WAPLJ.TRENING");

            conn.getConn().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alle Okter slettet!", "ja,Okter skaffet!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
        } catch (SQLException e) {
            conn.failed();
        } finally {
            conn.closeS(st);
            conn.close();
        }

    }
}
