/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

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
    private int Maned = 0;

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

    public synchronized String getNavn() {
        return nyOversikt.getBruker();
    }

    public synchronized void setNavn(String nyttBrukernavn) {
        nyOversikt.setBruker(nyttBrukernavn);
    }

    public synchronized TreningsOkt getTempOkt() {
        return tempOkt;
    }

    public synchronized void setTempOkt(TreningsOkt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized String oppdater() {

        if (!tempOkt.getKategori().trim().equals("")) {

            TreningsOkt nyOkt;
            nyOkt = new TreningsOkt(tempOkt.getOktNr(), tempOkt.getDate(),
                    tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst());

            nyOversikt.registrerNyOkt(nyOkt);
            treningsOkter.add(new OktStatus(nyOkt));            
            tempOkt.nullstill();
        }

        int indeks = treningsOkter.size() - 1;

        while (indeks >= 0) {
            OktStatus ts = treningsOkter.get(indeks);
            if (ts.getSkalSlettes()) {
                treningsOkter.remove(indeks);
            }
            indeks--;
        }
         return "success";
    }

    public int getManed() {
        return Maned;
    }

    public void setManed(int Maned) {
        this.Maned = Maned;
    }

    public synchronized void alleOkterEnMnd() {

        for (OktStatus e : treningsOkter) {
            if ((e.getTreningsikOkt().getDate().getMonth()) == Maned) {
                hjelp.add(e);
            }
        }
        hjelp2 = treningsOkter;
        treningsOkter = hjelp;
        oppdater();

    }
}
