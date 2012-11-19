/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.DBadm.DBConnection;
import com.corejsf.DBadm.DBController;
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
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class treningsOktBehandler implements Serializable {

    private FacesMessage fm;
    private List<OktStatus> DBtreningsobjekter;
    private Oversikt nyOversikt;
    private List<OktStatus> treningsOkter;
    private List<OktStatus> temptreningsOkter;
    private List<OktStatus> hjelp;
    private TreningsOkt tempOkt;
    private ArrayList<TreningsOkt> hjelp2;
    private @NotNull
    @Range(min = 0, max = 12)
    int maned = 0;
    private boolean nyOkt = false;
    private FacesContext fc;
    private TimeZone tidssone;

    public treningsOktBehandler() {

        DBtreningsobjekter = Collections.synchronizedList(new ArrayList<OktStatus>());
        nyOversikt = new Oversikt();
        treningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
        temptreningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
        hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
        tempOkt = new TreningsOkt();
        hjelp2 = new ArrayList<>();
    }

    public boolean isNyOkt() {
        return nyOkt;
    }

    public TimeZone getTidssone() {
        this.tidssone = TimeZone.getDefault();
        return tidssone == null ? TimeZone.getTimeZone("GMT") : tidssone;
    }

    public synchronized List<OktStatus> getTemptreningsOkter() {
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
        DBController.slettAlleOkter();

    }

    public synchronized String oppdater() {

        nyOkt = false;
        try {
            if (!(getTempOkt().getVarighet() == 0)) {
                TreningsOkt nyOkt;
                nyOkt = new TreningsOkt(getTempOkt().getOktNr(), new Date(getTempOkt().getDate().getTime()),
                        getTempOkt().getVarighet(), getTempOkt().getKategori(),
                        getTempOkt().getTekst());


                nyOversikt.registrerNyOkt(nyOkt);
                treningsOkter.add(new OktStatus(nyOkt));
                DBController.registrerTreningsOkt(nyOkt, getNavn());
                tempOkt = new TreningsOkt();
               
            }



            if (!(treningsOkter.isEmpty())) {
                int indeks = treningsOkter.size() - 1;
                while (indeks >= 0) {
                    OktStatus ts = treningsOkter.get(indeks);
                    if (ts.getSkalSlettes()) {
                        nyOversikt.slettOkt(ts.getTreningsikOkt());
                        treningsOkter.remove(indeks);
                        DBController.slettTreningsOkt(ts.getTreningsikOkt(), 1, getNavn());
                    }
                    indeks--;
                }
            }

            DBController.oppdaterTreningsOktDB(treningsOkter, getNavn());
            DBtreningsobjekter = DBController.getAlleTreningsOkter(getNavn());
            if (!DBtreningsobjekter.isEmpty()) {
                nyOversikt.slettAlle();
                treningsOkter.clear();
                for (OktStatus s : DBtreningsobjekter) {
                    nyOversikt.registrerNyOkt(s.getTreningsikOkt());
                    treningsOkter.add(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            oppdater();
        }
        return "success";
    }

    public int getManed() {
        return maned;
    }

    public void setManed(int Maned) {

        this.maned = Maned;

    }
}
