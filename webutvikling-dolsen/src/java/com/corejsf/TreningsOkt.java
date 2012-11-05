/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */

public class TreningsOkt {

    private @Id int oktNr;
    private @NotNull 
    Date dato;
    private @NotNull
    @Range(min = 1, max = 97696697)
    int varighet = 0;
    private @NotNull @Id
    String kategori;
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

    public TreningsOkt(int oktNr, Date date, int varighet, String kategori, String tekst) {
        this.oktNr = oktNr;
        this.dato = new Date(date.getTime());
        this.varighet = varighet;
        this.kategori = kategori;
        this.tekst = tekst;
        this.endret = false;


    }

    public synchronized int getOktNr() {
        return oktNr;
    }

    public synchronized java.sql.Date getSqlDate() {
        java.sql.Date D = new java.sql.Date(dato.getTime());
        return D;
    }

    public synchronized Date getDate() {
        dato = new Date(dato.getTime());
        return dato;
    }

    public synchronized int getVarighet() {
        return varighet;
    }

    public synchronized String getKategori() {
        return kategori;
    }
   @Transient    
    public synchronized void setDate(Date nyDato) {
        dato = new Date(nyDato.getTime());
        endret = true;
    }

    public synchronized void setVarighet(int oktVarighet) {
        varighet = oktVarighet;
        endret = true;
    }

    public synchronized void nullstill() {
        oktNr = 0;
        dato = new Date(System.currentTimeMillis());
        kategori = "";
        tekst = "";
        varighet = 0;
        
        
         
    }
}
