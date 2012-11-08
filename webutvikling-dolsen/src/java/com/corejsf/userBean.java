package com.corejsf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Named("user")
@SessionScoped
public class userBean implements Serializable {

    private String name;
    private String rolle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private String password;
    private String newPassword;
    private int count;
    private boolean loggedIn;
    private static final Logger logger = Logger.getLogger("com.corejsf");
   
    private  @Resource(name = "jdbc/waplj-prosjekt") DataSource ds;
    private FacesMessage fm = new FacesMessage();
    private FacesContext fc;

    public void changePassword() {
        if (loggedIn); //do_changePassword(password);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newValue) {
        password = newValue;
    }


    /* quote fra boka: "If your container does not
     support resource injection, add this constructor:
     public UserBean() {
     try {
     Context ctx = new InitialContext();
     ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mydb");
     } catch (NamingException ex) {
     logger.log(level.SEVERE, "DataSource lookup failed", ex);
     }
     }*/
   
    //Count = antall ganger besøkt
    // public int getCount(){
    //   return count;
    //}

    public String login() {
        try {
            doLogin();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "login failed", ex);
            return "internalError";

        }
        if (loggedIn) {
            return "loginSuccess";
        } else {
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Username or password is wrong!", "ja,Oppdatering utført!");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();
            return "loginFailure";

        }
    }

    public String logout() {
        loggedIn = false;
        return "login";
    }

    public void doLogin() throws SQLException {
        if (ds == null) {
            throw new SQLException("No data source");
        }
        Connection conn = ds.getConnection("waplj", "waplj");
        if (conn == null) {
            throw new SQLException("No conection");
        }

        try {
            conn.setAutoCommit(false);
            boolean committed = false;
            try {
                PreparedStatement passwordQuery = conn.prepareStatement("select BRUKER.PASSORD from WAPLJ.BRUKER where BRUKER.BRUKERNAVN = ? ");
                passwordQuery.setString(1, this.getName()); 
                ResultSet k = passwordQuery.executeQuery();
                conn.commit();               
                if (!k.next()) {
                    return;
                }
                String storedPassword = k.getString(1);
                loggedIn = password.equals(storedPassword.trim());


                committed = true;
            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        } finally {
            conn.close();
        }
    }
    public void getUserData(){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if(!(forsporrselobject instanceof HttpServletRequest)){
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        name = foresporrsel.getRemoteUser();
    }
    
    public boolean isInRole(){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if(!(forsporrselobject instanceof HttpServletRequest)){
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return false;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        return foresporrsel.isUserInRole(rolle);
    }
    
}
