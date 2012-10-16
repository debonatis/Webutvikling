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
    
    public synchronized void slettOkt(TreningsOkt t){        
         for(TreningsOkt e : alleOkter){            
            if(t.equals(e)){
              alleOkter.remove(e);
            }
        }
         
    }
    
    public int antallOkter(){
        return alleOkter.size();
    }
    
    public String getBrukernavn(){
        return bruker;
    }
    
}
