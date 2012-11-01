/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.util.Date;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */
public final class TreningsOkt{

    private int oktNr;
    private Date dato;
    private @NotNull
    @Range(min = 1, max = 97696697)
    int varighet = 0;
    private @NotNull
    String kategori;

    public Date getDato() {
        return dato;
    }

    

    public void setDato(java.sql.Date dato) {
        this.dato = dato;
    }
    private @NotNull
    @Length(min = 0, max = 30)
    String tekst;
    private @Length(min = 6, max = 20)
    @Id
    String Brukernavn = "anne";
    private boolean endret = false;

    public synchronized void setKategori(String kategori) {
        this.kategori = kategori;
        endret = true;
    }

    public boolean isEndret() {
        return endret;
    }

    public void setEndret(boolean endret) {
        this.endret = endret;
    }

    public synchronized String getTekst() {
        return tekst;
    }

    @NotNull
    @Length(min = 1, max = 30)
    public synchronized void setTekst(String tekst) {
        this.tekst = tekst;
        endret = true;
    }

    public TreningsOkt() {
        nullstill();
    }
    private @Length(min = 6, max = 20)
    String brukernavn;

    public synchronized String getBrukernavn() {
        return brukernavn;
    }

    public synchronized void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;

    }

    public TreningsOkt(int oktNr, Date dato, int varighet, String kategori, String tekst) {
        this.oktNr = oktNr;
        this.dato = dato;
        this.varighet = varighet;
        this.kategori = kategori;
        this.tekst = tekst;


    }

    public synchronized int getOktNr() {
        return oktNr;
    }

    public synchronized java.sql.Date getSqlDate() {
        return new java.sql.Date(dato.getYear(), dato.getMonth(), dato.getDate());
    }

    public synchronized Date getDate() {
        dato = new Date(dato.getYear(), dato.getMonth(), (dato.getDate()+1));
        return dato;
    }

    public void setDato(Date dato) {
        this.dato = dato;
    }

    public synchronized int getVarighet() {
        return varighet;
    }

    public synchronized String getKategori() {
        return kategori;
    }

    public synchronized void setDate(Date nyDato) {
        dato = nyDato;
        endret = true;
    }

    public synchronized void setVarighet(int oktVarighet) {
        varighet = oktVarighet;
        endret = true;
    }

    public synchronized void nullstill() {
        oktNr = 0;
        dato = new Date();
        kategori = "";
        tekst = "";
        varighet = 0;
    }
}
