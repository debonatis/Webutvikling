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
    int maned = 0;
    private int mick = 0;
    private boolean nyOkt = false;
    private boolean manedIkkeEksi = false;

    public synchronized boolean isManedIkkeEksi() {
        return manedIkkeEksi;
    }

    public synchronized void setManedIkkeEksi(boolean manedIkkeEksi) {
        this.manedIkkeEksi = manedIkkeEksi;
    }

    public boolean isNyOkt() {
        return nyOkt;
    }

    public void setNyOkt(boolean nyOkt) {
        this.nyOkt = nyOkt;
    }

    public synchronized boolean getDatafins() {
        return (!treningsOkter.isEmpty());
    }

    public synchronized List<OktStatus> getTabelldata(){

        if (!hjelp.isEmpty() && (getManed() >= 1)) {
            return hjelp;             
        } if(isManedIkkeEksi() && (getManed() >= 1)){            
            return hjelp;
        } if(getManed() == 0){
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
    public synchronized boolean getPaManed(int m){
        try{
        hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
        hjelp = treningsOkter;
        for(OktStatus k :treningsOkter){
            if(!(k.getTreningsikOkt().getDate().getMonth() == (m-1))){
                hjelp.remove(k);
            } 
        } 
        if(hjelp.isEmpty()){
            setManedIkkeEksi(true);
        }
        }catch (ConcurrentModificationException e){
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
        if (!(getManed() == 0)) {
           Boolean sjekk;
            sjekk = getPaManed(getManed());
        } else {
            hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
            setManedIkkeEksi(false);
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
