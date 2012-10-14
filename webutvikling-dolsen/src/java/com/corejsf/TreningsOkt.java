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

/**
 *
 * @author deb
 */

public class TreningsOkt implements Serializable {

    private TreningsOkt nyTreningsokt = new TreningsOkt();
    private int oktNr;
    private Date dato = new Date();

    public TreningsOkt getNyTreningsokt() {
        return nyTreningsokt;
    }

    public void setNyTreningsokt(TreningsOkt nyTreningsokt) {
        this.nyTreningsokt = nyTreningsokt;
    }
    private @NotNull
    int varighet;
    private @NotNull
    String kategori;
    private @NotNull
    @Length(min = 0, max = 30)
    @Id
    String tekst;    
    

    public void setKategori(String kategori) {
        oktNr++;
        this.kategori = kategori;
    }

    public String getTekst() {
       
        return tekst;
    }

    @NotNull
    @Length(min = 1, max = 30)
    public void setTekst(String tekst) {        
        this.tekst = tekst;
    }

    public TreningsOkt() {
    }

    public TreningsOkt(int oktNr, Date dato, int varighet, String kategori, String tekst) {
        this.oktNr = oktNr;
        this.dato = dato;
        this.varighet = varighet;
        this.kategori = kategori;
        this.tekst = tekst;
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
    
    public void nullstill(){
        oktNr = 0;
        dato = new Date();
        kategori = "";
        tekst = "";
        varighet = 0;
    }
}
