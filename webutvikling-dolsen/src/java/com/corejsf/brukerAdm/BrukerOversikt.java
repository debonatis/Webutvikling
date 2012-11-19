/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.brukerAdm;

/**
 *
 * @author deb
 */
public class BrukerOversikt {
   
    private String brukernavn;
    private String passord;
    private boolean skalSlettes;

    public BrukerOversikt(String brukernavn, String passord) {
        this.brukernavn = brukernavn;
        this.passord = passord;
        this.skalSlettes = false;
    }

    public boolean isSkalSlettes() {
        return skalSlettes;
    }

    public void setSkalSlettes(boolean skalSlettes) {
        this.skalSlettes = skalSlettes;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }
    
    
}
