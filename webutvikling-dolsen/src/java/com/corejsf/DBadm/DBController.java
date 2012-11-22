/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.DBadm;

import com.corejsf.OktStatus;
import com.corejsf.TreningsOkt;
import com.corejsf.brukerAdm.Bruker;
import com.corejsf.brukerAdm.BrukerStatus;
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
    private List<BrukerStatus> hjelpBruker = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private List<BrukerStatus> dbBrukerobjekter = Collections.synchronizedList(new ArrayList<BrukerStatus>());

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

        } catch (SQLException e) {
            conn.failed();
        } finally {
            conn.closeP(reg);
            conn.close();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nyregistrering fullført!", "ja,Nyregistreing fullført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
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

    private synchronized void slettalleTreningsOkterNavn(String navn) {
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM WAPLJ.TRENING WHERE BRUKERNAVN = '" + navn + "'");
            st.getConnection().commit();

            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sletting av treningsøkt/er utført!", "ja,Sletting utført!");
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

    public synchronized String skiftPassordDB(String passord, String navn) {

        DBConnection conn = new DBConnection();
        PreparedStatement oppdaterPassord = null;
        String oppdaterString =
                "update WAPLJ.BRUKER set PASSORD = ? where BRUKERNAVN= ?";
        try {
            conn.getConn().setAutoCommit(false);
            oppdaterPassord = conn.getConn().prepareStatement(oppdaterString);
            oppdaterPassord.setString(1, passord);
            oppdaterPassord.setString(2, navn);
            oppdaterPassord.executeUpdate();
            conn.getConn().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Endring av passord utført!", "");
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
            return "ikkeOk";
        } finally {
            conn.closeS(oppdaterPassord);
            conn.close();
        }
        return "ok";
    }

    public synchronized void registrerBruker(Bruker bruker) {

        DBConnection conn = new DBConnection();
        PreparedStatement reg = null;
        String regTekst = "INSERT INTO WAPLJ.BRUKER"
                + " VALUES (?,?)";
        try {
            conn.getConn().setAutoCommit(false);
            reg = conn.getConn().prepareStatement(regTekst);
            reg.setString(1, bruker.getName());
            reg.setString(2, bruker.getPassord());
            reg.executeUpdate();
            conn.getConn().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nyregistrering av bruker fullført!", "ja,Nyregistreing fullført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
        } catch (SQLException e) {
            conn.failed();
        } finally {
            registrerRolle(conn, bruker);
        }
    }

    private synchronized void registrerRolle(DBConnection conn, Bruker bruker) {

        PreparedStatement reg = null;
        String regTekst2 = "INSERT INTO WAPLJ.ROLLE"
                + " VALUES (?,?) ";
        try {
            conn.getConn().setAutoCommit(false);
            reg = conn.getConn().prepareStatement(regTekst2);
            reg.setString(1, bruker.getName());
            reg.setString(2, bruker.getRolle());
            reg.executeUpdate();
            conn.getConn().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nyregistrering av rolle fullført!", "ja,Nyregistreing fullført!");
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

    public synchronized void slettBruker(Bruker bruker) {
        int i = 0;
        slettalleTreningsOkterNavn(bruker.getName());
        DBConnection conn = new DBConnection();
        Statement st = null;
        try {
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM waplj.rolle WHERE rolle.brukernavn = '" + bruker.getName() + "'");
            st.getConnection().commit();
            st = conn.getConn().createStatement();
            st.executeUpdate("DELETE FROM waplj.bruker WHERE bruker.brukernavn = '" + bruker.getName() + "'");
            st.getConnection().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sletting utført!", "ja,Sletting utført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
        } catch (SQLException e) {
            conn.failed();
            i++;
        } finally {
            conn.closeS(st);
            conn.close();
            if (i > 0) {
                slettBruker(bruker);
            }
        }
        slettalleTreningsOkterNavn(bruker.getName());
    }

    public synchronized int oppdaterBrukerDB(List<BrukerStatus> brukere) {

        hjelpBruker.clear();
        if (!(brukere.isEmpty())) {
            for (BrukerStatus s : brukere) {
                if (s.getBruker().isEndret()) {
                    s.getBruker().setEndret(false);
                    hjelpBruker.add(s);
                }
            }
        }
        DBConnection conn = new DBConnection();
        PreparedStatement oppdaterOkter = null;
        PreparedStatement oppdaterOkter2 = null;
        String oppdaterString1 =
                "update WAPLJ.Rolle set rolle.rolle = ? where rolle.BRUKERNAVN= ?";
        String oppdaterString2 =
                "update WAPLJ.BRUKER set bruker.passord = ? where bruker.BRUKERNAVN= ?";
        if (!hjelpBruker.isEmpty()) {
            try {
                conn.getConn().setAutoCommit(false);
                oppdaterOkter = conn.getConn().prepareStatement(oppdaterString1);
                for (BrukerStatus f : hjelpBruker) {
                    oppdaterOkter.setString(1, f.getBruker().getRolle());
                    oppdaterOkter.setString(2, f.getBruker().getName());
                    oppdaterOkter.executeUpdate();
                    conn.getConn().commit();
                }
                conn.getConn().setAutoCommit(false);
                oppdaterOkter2 = conn.getConn().prepareStatement(oppdaterString2);
                for (BrukerStatus f : hjelpBruker) {
                    oppdaterOkter2.setString(1, f.getBruker().getPassord());
                    oppdaterOkter2.setString(2, f.getBruker().getName());
                    oppdaterOkter2.executeUpdate();
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
                conn.closeP(oppdaterOkter);
                conn.close();
            }
        }
        return hjelpBruker.size();
    }

    public synchronized List<BrukerStatus> getAlleBrukere() {

        Bruker hjelpeobjekt;
        dbBrukerobjekter.clear();
        DBConnection conn = new DBConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT BRUKER.brukernavn, Bruker.passord, rolle.rolle\n"
                    + "FROM BRUKER\n"
                    + "RIGHT JOIN ROLLE\n"
                    + "ON Bruker.BRUKERNAVN=ROLLE.BRUKERNAVN\n"
                    + "ORDER BY BRUKER.BRUKERNAVN");
            while (rs.next()) {
                hjelpeobjekt = new Bruker(rs.getString("Brukernavn"), rs.getString("passord"),
                        rs.getString("rolle"), 1);
                dbBrukerobjekter.add(new BrukerStatus(hjelpeobjekt));
            }
            conn.getConn().commit();
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alle Brukere skaffet!", "ja,Brukere skaffet!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
        } catch (SQLException e) {
            conn.failed(); //Rollback
        } finally {
            conn.closeS(st);
            conn.closeR(rs);
            conn.close();
            return dbBrukerobjekter;
        }
    }
}
