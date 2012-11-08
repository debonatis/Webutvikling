package com.corejsf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javax.ws.rs.core.Request;

@Named("user")
@SessionScoped
public class userBean implements Serializable {

    private String name;
    private String rolle;
    private String password;
    private String newPassword;
    private int count;
    private boolean loggedIn;
    private static final Logger logger = Logger.getLogger("com.corejsf");   
    private FacesMessage fm = new FacesMessage();
    private @Resource(name = "jdbc/waplj_prosjekt") DataSource ds;
    private FacesContext fc;

    public String getFjas() throws SQLException {
        this.fjas = this.getSkrot();
        return fjas;
    }

    public void setFjas(String fjas) {
        this.fjas = fjas;
    }
    private String fjas;

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
     public int getCount(){
       return count;
    }
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

    public void getUserData() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        name = foresporrsel.getRemoteUser();
    }
    
    public String getSkrot() throws SQLException{
        
        Statement st = null;
        TreningsOkt helpObject;
        Date lolo = new Date(2012,11,22);
        TreningsOkt mick = new TreningsOkt(10, lolo, 23, "styrke", "mick");

        ArrayList<Object> objects = null;
        ResultSet rs = null;
        Connection conn = ds.getConnection();
        try{
            st = ds.getConnection("waplj", "waplj").createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                helpObject = new TreningsOkt(rs.getInt("OKTNR"),rs.getDate("DATO"), 
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"), 
                        rs.getString("TEKST"));
                conn.commit();
                objects.add(helpObject);
            }
        } catch (SQLException e) {
            conn.rollback(); //Rollback
        } finally {
            conn.close();
//            test.closeR(rs);
//            test.close();
        }
        TreningsOkt mick1337 = null;
        for(Object to : objects){
            mick1337 = (TreningsOkt) to;
            
        }
        return mick1337.getKategori();
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
    public boolean isAdmin(){
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
}
