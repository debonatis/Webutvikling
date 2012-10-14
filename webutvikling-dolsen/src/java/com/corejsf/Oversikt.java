/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Martin
 */

@Named
@SessionScoped
public class Oversikt implements Serializable{
    
    private ArrayList<TreningsOkt> alleOkter = new ArrayList();    
    private String bruker;
    
    public ArrayList<TreningsOkt> getAlleOkter(){
        return alleOkter;
    }
    
    public ArrayList<TreningsOkt> getAlleOkterEnMnd(int s){
        ArrayList<TreningsOkt> hjelp = new ArrayList<TreningsOkt>();
        for(TreningsOkt e : alleOkter){            
            if((e.getDate().getMonth()) == s){
                 hjelp.add(e);
            }
        }
        if(hjelp.isEmpty()){
            return null;
        }
        return hjelp;
    }
    
    public void registrerNyOkt(TreningsOkt e){
        this.alleOkter.add(e);
    }
    
    public boolean slettOkt(TreningsOkt t){
        boolean hjelp2 = false;
         for(TreningsOkt e : alleOkter){            
            if(t.equals(e)){
              hjelp2 = alleOkter.remove(e);
            }
        }
         return hjelp2;
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
