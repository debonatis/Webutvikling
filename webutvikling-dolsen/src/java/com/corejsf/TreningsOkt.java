/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */
public final class TreningsOkt implements Serializable {

    private int oktNr;
    private Date dato = new Date();
    private @NotNull
    @Range(min = 1, max = 97696697)
    int varighet = 0;
    private @NotNull
    String kategori;
    private @NotNull
    @Length(min = 0, max = 30)
    
    String tekst;
    private @Length(min = 6, max = 20)
    @Id String  Brukernavn;

    public synchronized void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public synchronized String getTekst() {
        return tekst;
    }

    @NotNull
    @Length(min = 1, max = 30)
    public synchronized void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public TreningsOkt() {
        nullstill();
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
        return dato;
    }

    public synchronized int getVarighet() {
        return varighet;
    }

    public synchronized String getKategori() {
        return kategori;
    }

    public synchronized void setDate(Date nyDato) {
        dato = nyDato;
    }

    public synchronized void setVarighet(int oktVarighet) {
        varighet = oktVarighet;
    }

    public synchronized void nullstill() {
        oktNr = 0;
        dato = new Date();
        kategori = "";
        tekst = "";
        varighet = 0;
    }
}