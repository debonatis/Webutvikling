/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author deb
 */
@Named
@SessionScoped
public class TreningsOkt implements Serializable {

    private int oktNr;
    private Date dato = new Date();

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
    private int varighet;
    private String kategori;
    private String tekst;

    public TreningsOkt() {
    }

//    public TreningsOkt(int oktNr, Date dato, int varighet, String kategori) {
//        this.oktNr = oktNr;
//        this.dato = dato;
//        this.varighet = varighet;
//        this.kategori = kategori;
//    }

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
   @NotNull
    public void setOktNr(int nyOktNr) {
        oktNr = nyOktNr;
    }

    public void setDate(Date nyDato) {
        dato = nyDato;
    }
    /**
     *
     * @param oktVarighet
     */
    @NotNull 
    public void setVarighet(int oktVarighet) {
        varighet = oktVarighet;
    }
}
