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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

@Named("user")
@SessionScoped
public class userBean implements Serializable {

    private String name;
    private String password;
    private String newPassword;
    private int count;
    private boolean loggedIn;
    private static final Logger logger = Logger.getLogger("com.corejsf");
    @Resource(name = "jdbc/waplj-prosjekt")
    private DataSource ds;
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
    public String getName() {
        return name;
    }

    public void setName(String newValue) {
        password = newValue;
    }
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
                PreparedStatement passwordQuery = conn.prepareStatement("select PASSORD from WAPLJ.BRUKER where brukernavn = '" + name + "'");


                ResultSet k = passwordQuery.executeQuery();

//                if (!k.next()) {
//                    return;
//                }
                String storedPassword = k.getString("passord");
                loggedIn = password.equals(storedPassword.trim());

                conn.commit();
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
}
