/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Bjørn Tore
 */
public class DBConnection {
    //@Resource(name="jdbc/waplj_prosjekt")
    //private DataSource source;
    private Connection conn;

    public DBConnection() {
        String DBdriver = "org.apache.derby.jdbc.ClientDriver";
        String DBname = "jdbc:derby://localhost:1527/Waplj;user=waplj;password=waplj";

        try {
            Class.forName(DBdriver);
            conn = DriverManager.getConnection(DBname);
        } catch (Exception e) {
            System.out.println("Could not connect to database(dev): " + e);
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void failed() {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("COULD NOT CLOSE DATABASE CONNECTION (waplj)");
            }
        } 
    }

    public void closeS(Statement s) {
        if (s != null) {
            try {
                s.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }

    public void closeR(ResultSet r) {
        if (r != null) {
            try {
                r.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
}
