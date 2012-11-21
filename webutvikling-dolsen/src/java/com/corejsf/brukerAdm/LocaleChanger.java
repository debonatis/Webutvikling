/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.corejsf.brukerAdm;
import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped

/**
 *
 * @author Jørgen
 */
public class LocaleChanger implements Serializable{
    
    public String germanAction(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.GERMAN);
        return null;
    }
    
    public String englishAction(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.ENGLISH);
        return null;
    }
    
    public String norwegianAction(){
        FacesContext context = FacesContext.getCurrentInstance();
        //Locale l = new Locale("no");
        context.getViewRoot().setLocale(new Locale("no"));
        return null;
    }

}
