package com.corejsf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Cacheable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author deb
 */
@ManagedBean
@Cacheable(false)
public class Bruker implements Serializable {

    private String name;
    private String newPassword;
    private String newPassword2;

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
    private static final Logger logger = Logger.getLogger("com.corejsf");
    private FacesMessage fm = new FacesMessage();
    private FacesContext fc;
    private final String[] roller = {"admin", "bruker"};
    private boolean changePassword;

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public String changePassword() {
        if (getNewPassword().equals(getNewPassword2())) {
            return skiftPassord(newPassword);

        }
        return "ikkeOk";

    }

    public String getRolle() {
        for (String r : roller) {
            if (isInRole(r)) {
                return r;
            }
        }
        logout();
        return "NO ROLE, logging you out!";
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

    public boolean isInRole(String k) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return false;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        return foresporrsel.isUserInRole(k);
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

    public String logout() {
        String result = "ok";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (ServletException e) {
            logger.log(Level.SEVERE, "Failed to logout user!", e);
            logout();

        }

        return result;
    }

    synchronized String skiftPassord(String passord) {

        DBConnection conn = new DBConnection();
        PreparedStatement oppdaterPassord = null;
        String oppdaterString =
                "update WAPLJ.BRUKER set PASSORD = ? where BRUKERNAVN= ?";


        try {

            conn.getConn().setAutoCommit(false);
            oppdaterPassord = conn.getConn().prepareStatement(oppdaterString);
            oppdaterPassord.setString(1, passord);
            oppdaterPassord.setString(2, getName());
            oppdaterPassord.executeUpdate();
            conn.getConn().commit();


            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Endring av passord utført!", "");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();


        } catch (SQLException e) {
            conn.failed();
            if (conn.getConn() != null) {
                try {
                    System.err.print("Endring har blitt trekk tilbake");
                    conn.getConn().rollback();
                } catch (SQLException excep) {
                    conn.failed();
                }
            }
            return "ikkeOk";

        } finally {
            conn.closeS(oppdaterPassord);
            conn.close();

        }

        return "ok";

    }
}
