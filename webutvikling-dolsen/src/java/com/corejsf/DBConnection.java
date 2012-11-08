/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author deb
 */
public class DBConnection {

    @Resource(name = "jdbc/waplj_prosjekt")
    private DataSource source;
    private Connection conn;
    private static final Logger logger = Logger.getLogger("com.corejsf");

    public DBConnection() {
        try {
            if (source == null) {
                throw new SQLException("No data source");
            }
            conn = source.getConnection();
            if (conn == null) {
                throw new SQLException("No connection");
            }
        } catch (Exception e) {
            System.out.println("Could not connect to database(dev): " + e);
        }
        try {
            Context ctx = new InitialContext();
            source = (DataSource) ctx.lookup("java:comp/env/jdbc/waplj_prosjekt");
            try {
                conn = source.getConnection();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NamingException e) {
            logger.log(Level.SEVERE, "Lookup failed!");
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
