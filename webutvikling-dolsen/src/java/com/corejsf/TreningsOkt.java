/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author deb
 */
@Named
@SessionScoped
public class TreningsOkt implements Serializable{

    public int getVarighet() {
        return varighet;
    }

    public void setVarighet(int varighet) {
        this.varighet = varighet;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    
    private int varighet;
    private String kategori;
    private String tekst;
    
    
    
}
