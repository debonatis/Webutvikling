/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;

import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
/**
 *
 * @author deb
 */
public class TreningsOkt implements Serializable {

    int oktNr;
    java.util.Date dato = new Date();
    int varighet;
    String kategori;

    public TreningsOkt(){
    }
    
    public TreningsOkt(int oktNr, Date dato, int varighet, String kategori) {
        this.oktNr = oktNr;
        this.dato = dato;
        this.varighet = varighet;
        this.kategori = kategori;
    }
    
    public int getOktNr(){
        return oktNr;
    }
    public java.sql.Date getSqlDate(){
        return new java.sql.Date(dato.getYear(), dato.getMonth(), dato.getDate());
    }
    public Date getDate(){
        return dato;
    }
    public int getVarighet(){
        return varighet;
    }
    public String getKategori(){
        return kategori;
    }
    public void setOktNr(int nyOktNr){
        oktNr = nyOktNr;
    }
    public void setDate(Date nyDato){
        dato = nyDato;
    }
    public void setVarighet(int oktVarighet){
        varighet = oktVarighet;
    }
}
