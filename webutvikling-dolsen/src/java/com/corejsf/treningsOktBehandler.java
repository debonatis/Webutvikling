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

    public synchronized void oppdater() {

        if (!tempOkt.getKategori().trim().equals("")) {

            TreningsOkt nyOkt;
            nyOkt = new TreningsOkt(TreningsOkt.getOktNr(), tempOkt.getDate(),
                    tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getKategori());

            nyOversikt.registrerNyOkt(nyOkt);
            treningsOkter.add(new OktStatus(nyOkt));
            nyOkt.nullstill();
        }

        int indeks = treningsOkter.size() - 1;

        while (indeks >= 0) {
            OktStatus ts = treningsOkter.get(indeks);
            if (ts.getSkalSlettes()) {
                treningsOkter.remove(indeks);
            }
            indeks--;
        }
    }

    public synchronized void AlleOkterEnMnd(int s) {

        for (OktStatus e : treningsOkter) {
            if ((e.getTreningsikOkt().getDate().getMonth()) == s) {
                hjelp.add(e);
            }
        }
        hjelp2 = treningsOkter;
        treningsOkter = hjelp;

    }
}
