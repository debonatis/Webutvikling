package com.corejsf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author deb
 */
@Named("user")
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin","bruker"})  
public class Bruker implements Serializable {

    private String name;
    private String rolle;    
    private String newPassword;
    private int count;
    private boolean loggedIn;
   private static final Logger logger = Logger.getLogger("com.corejsf");
    private FacesMessage fm = new FacesMessage();    
    private FacesContext fc;   
    

    public String getRolle() {
        return rolle == null ? "" : rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public String getName() {
        if (name == null) {
            getUserData();
        }
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changePassword() {
        if (loggedIn); //do_changePassword(password);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }  
    

    public void getUserData() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        setName(foresporrsel.getRemoteUser()); 
    }

    public boolean isInRole() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return false;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        return foresporrsel.isUserInRole(rolle);
    }

    public boolean isAdmin() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return false;
        }
        String hjelp = "admin";
        HttpServletRequest foresporrsel2 = (HttpServletRequest) forsporrselobject;
        return foresporrsel2.isUserInRole(hjelp);
    }
     private static Logger log = Logger.getLogger(Bruker.class.getName());
   
  public String logout() {
    String result="/index?faces-redirect=true";
     
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
     
    try {
      request.logout();
    } catch (ServletException e) {
      log.log(Level.SEVERE, "Failed to logout user!", e);
      result = "/loginError?faces-redirect=true";
    }
     
    return result;
  }
}
