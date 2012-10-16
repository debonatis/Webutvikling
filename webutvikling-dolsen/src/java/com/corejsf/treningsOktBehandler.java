/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
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
        return (!treningsOkter.isEmpty());
    }

    public synchronized List<OktStatus> getTabelldata() {
        Boolean sjekk = false;
        if ((getManed() >= 1)) {
            sjekk = getPaManed(getManed());
        } else {
            hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
        }
        if (!hjelp.isEmpty() && sjekk) {
            return hjelp;
        }
        if (getManed() == 0) {
            return treningsOkter;
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

    public synchronized boolean getPaManed(int m) {
        try {
            hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
            for (OktStatus k : treningsOkter) {
                if (!(k.getTreningsikOkt().getDate().getMonth() == (m - 1))) {
                    hjelp.add(k);
                }
            }

        } catch (ConcurrentModificationException e) {
            getPaManed(m);
        }
        return true;
    }

    public synchronized void setTempOkt(TreningsOkt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized String oppdater() {

        nyOkt = false;

        try {
            if (!(tempOkt.getVarighet() == 0)) {
                mick++;
                TreningsOkt nyOkt;
                nyOkt = new TreningsOkt((tempOkt.getOktNr() + mick), tempOkt.getDate(),
                        tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst());

                nyOversikt.registrerNyOkt(nyOkt);
                treningsOkter.add(new OktStatus(nyOkt));
                tempOkt.nullstill();
            }
            int indeks = treningsOkter.size() - 1;

            while (indeks >= 0) {
                OktStatus ts = treningsOkter.get(indeks);
                if (ts.getSkalSlettes()) {
                    for (TreningsOkt e : nyOversikt.getAlleOkter()) {
                        if (e.equals(ts.getTreningsikOkt())) {
                            nyOversikt.slettOkt(e);
                        }
                    }
                    treningsOkter.remove(indeks);

                }
                indeks--;
            }
        } catch (ConcurrentModificationException e) {
            System.out.println(e);
            oppdater();
        }



        return "success";
    }

    public synchronized int getManed() {
        return maned;
    }

    public synchronized void setManed(int Maned) {
        this.maned = Maned;
    }
}
