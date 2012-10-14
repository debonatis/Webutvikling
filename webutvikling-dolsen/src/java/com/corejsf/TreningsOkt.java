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

public final class TreningsOkt implements Serializable {
    
    private  int oktNr;
    private Date dato = new Date();

  
    private @NotNull
    int varighet;
    private @NotNull
    String kategori;
    private @NotNull
    @Length(min = 0, max = 30)
    @Id
    String tekst;
    
    
    

    public void setKategori(String kategori) {        
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
        nullstill();
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
