/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import java.sql.Date;


/**
 *
 * @author Martin
 */
public class Oversikt implements Serializable{
    
    private String brukernavn;
    private ArrayList<TreningsOkt> alleOkter = new ArrayList<TreningsOkt>();
    
    public Oversikt(String brukernavn){
        this.brukernavn = brukernavn;
    }
    
    
     
    public String getBrukernavn(){
        return brukernavn;
    }
    public void setBrukernavn(String nyBruker){
        brukernavn = nyBruker;
    }
    
    public List getAlleOkter(){
        List liste = new ArrayList();
        for(TreningsOkt e : alleOkter){
            liste.add(new SelectItem(e.getOktNr()));
        }
        return liste;
    }
    
    
}