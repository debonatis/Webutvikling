/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Martin
 */


public class Oversikt implements Serializable{

    public String getBruker() {
        return bruker;
    }

    public void setBruker(String bruker) {
        this.bruker = bruker;
    }
    
    private ArrayList<TreningsOkt> alleOkter = new ArrayList();    
    private String bruker;
    
    public Oversikt(){
        
    }
    
    /**
     *
     * @return
     */
    public synchronized ArrayList<TreningsOkt> getAlleOkter(){
       
        return alleOkter;
    }
    
    
    
    public synchronized void registrerNyOkt(TreningsOkt e){
        alleOkter.add(e);
    }
    
    public synchronized boolean slettOkt(TreningsOkt t){
        boolean hjelp2 = false;
         for(TreningsOkt e : alleOkter){            
            if(t.equals(e)){
              hjelp2 = alleOkter.remove(e);
            }
        }
         return hjelp2;
    }
    
    public int antallOkter(){
        return alleOkter.size();
    }
    
    public String getBrukernavn(){
        return bruker;
    }
    public void setBrukernavn(String nyBruker){
        bruker = nyBruker;
    }
}
