/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author deb
 */
@Named
@SessionScoped
public class TreningsOkt implements Serializable {

    int oktNr;
    Date dato = new Date();

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    int varighet;
    String kategori;
    String tekst;

    public TreningsOkt() {
    }

    public TreningsOkt(int oktNr, Date dato, int varighet, String kategori) {
        this.oktNr = oktNr;
        this.dato = dato;
        this.varighet = varighet;
        this.kategori = kategori;
    }

    public int getOktNr() {
        return oktNr;
    }

    public java.sql.Date getSqlDate() {
        return new java.sql.Date(dato.getYear(), dato.getMonth(), dato.getDate());
    }

    public Date getDate() {
        return dato;
    }

    public int getVarighet() {
        return varighet;
    }

    public String getKategori() {
        return kategori;
    }

    public void setOktNr(int nyOktNr) {
        oktNr = nyOktNr;
    }

    public void setDate(Date nyDato) {
        dato = nyDato;
    }

    public void setVarighet(int oktVarighet) {
        varighet = oktVarighet;
    }
}
