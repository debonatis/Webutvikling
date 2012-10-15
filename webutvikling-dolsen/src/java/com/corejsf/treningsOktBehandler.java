/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

/**
 *
 * @author deb
 */
@Named
@SessionScoped
public class treningsOktBehandler implements Serializable {

    private Oversikt nyOversikt = new Oversikt();
    private List<OktStatus> treningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
    List<OktStatus> hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
    List<OktStatus> hjelp2 = Collections.synchronizedList(new ArrayList<OktStatus>());
    private TreningsOkt tempOkt = new TreningsOkt();
    private @NotNull int maned = 0;
    private int mick = 0;

    public synchronized boolean getDatafins() {
        return (!treningsOkter.isEmpty());
    }

    public synchronized List<OktStatus> getTabelldata() {
        if (!hjelp2.isEmpty()) {
            treningsOkter = hjelp2;
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

    public synchronized void setTempOkt(TreningsOkt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized String oppdater() {

        if (!tempOkt.getKategori().trim().equals("")) {
            
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
        return "success";
    }

    public synchronized int getManed() {
        return maned;
    }

    public synchronized void setManed(int Maned) {
        this.maned = Maned;
    }

    public synchronized void alleOkterEnMnd() {
        if (!(maned == 0)) {
            for (OktStatus e : treningsOkter) {
                if ((e.getTreningsikOkt().getDate().getMonth()) == (maned - 1)) {
                    hjelp.add(e);
                for(TreningsOkt f : nyOversikt.getAlleOkter()){
                    if(f.getDate().getMonth() == (maned -1)){
                        nyOversikt.slettOkt(f);
                    }
                }
                }
            }
            hjelp2 = treningsOkter;
            treningsOkter = hjelp;
            oppdater();

        }
    }
}
