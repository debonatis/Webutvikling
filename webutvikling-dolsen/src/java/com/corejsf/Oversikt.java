/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Id;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author Martin
 */


public class Oversikt implements Serializable{

    public String getBruker() {
        return bruker;
    }

    
    
    private ArrayList<TreningsOkt> alleOkter = new ArrayList();    
   private @Length(min = 6, max = 20)
    @Id String  bruker;
    
    public Oversikt(){
        
    }

    public void setBruker(String bruker) {
        this.bruker = bruker;
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
    
    public synchronized void slettAlle(){
        alleOkter.clear();
    }
    
    public int antallOkter(){
        return alleOkter.size();
    }
    
    
    
}
