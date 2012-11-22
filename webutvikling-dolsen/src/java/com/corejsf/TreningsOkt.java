/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.util.Date;
import javax.faces.bean.RequestScoped;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */
@RequestScoped
public class TreningsOkt {

    private @Id
    int oktNr;
    private @NotNull(message= "This is not a valid date. Try a new one!")
    Date dato;
    private @NotNull(message= "Du må skrive noe:)")   
    @Range(min = 1, max = 300, message= "The duration must be a number between 1 and 300!")
    int varighet = 0;
    private @NotNull
    @Id
    String kategori;
    private @NotNull
    @Length(min = 0, max = 30, message="The textfield can be empty, or contain maximum 30 characters")
    String tekst;
    private boolean endret = false;
     private @Length(min = 6, max = 20)
    String brukernavn;

    public void setKategori(String kategori) {
        if (!(this.kategori.equalsIgnoreCase(kategori))) {
            this.setEndret(true);
            this.kategori = kategori;
        }
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
    public void setTekst(String tekst) {
        if (!(this.tekst.trim().equalsIgnoreCase(tekst))) {
            this.setEndret(true);
            this.tekst = tekst;
        }
    }

    public TreningsOkt() {
        nullstill();
    }
   

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
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

    public int getOktNr() {
        return oktNr;
    }

    public java.sql.Date getSqlDate() {
        java.sql.Date D = new java.sql.Date(dato.getTime());
        return D;
    }

    public Date getDate() {
        dato = new Date(dato.getTime());
        return dato;
    }

    public int getVarighet() {
        return varighet;
    }

    public String getKategori() {
        return kategori;
    }

    public void setDate(Date nyDato) {
        if (!(this.dato.equals(nyDato))) {
            this.setEndret(true);
            dato = new Date(nyDato.getTime());
        }
    }

    public void setVarighet(int oktVarighet) {
        if (!(this.varighet == oktVarighet)) {
            varighet = oktVarighet;
            this.setEndret(true);
        }
    }

    private void nullstill() {
        oktNr = 0;
        dato = new Date(System.currentTimeMillis());
        kategori = "";
        tekst = "";
        varighet = 0;
    }
}
