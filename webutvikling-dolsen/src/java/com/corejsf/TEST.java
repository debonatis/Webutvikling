/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author deb
 */
public class TEST {
    
    
    public static void main(String[] args) throws Exception {
    
        DBConnection test = new DBConnection();
        
         TreningsOkt helpObject;
        ArrayList objects = new ArrayList<TreningsOkt>();
        DBConnection conn = new DBConnection();
        Statement st = null;
        Date lolo = new Date(2012,11,22);
        TreningsOkt mick = new TreningsOkt(1, lolo, 23, "lol", "mick", "helvett");
        String sb = "I";
        try {

            st = conn.getConn().createStatement();
            st.executeUpdate("INSERT INTO trening" + "(dato, varighet, kategorinavn, tekst, brukernavn)" + "VALUES(DATE('"+ mick.getSqlDate() +"')," + mick.getVarighet() +", '" + mick.getKategori()"', '" + mick.getTekst()"', '"+ mick.getBrukernavn() + "')");
            
            sb.concat("NSERT INTO USERS");
        sb.concat("(dato, varighet, kategorinavn, tekst, brukernavn) ");
        sb.concat("VALUES ( ");
        sb.concat("  '" + mick.getSqlDate() + "'");
        sb.concat(", '" + mick. + "' ");
        sb.concat(", '" + myUser.GetLastname() + "' ");
        sb.concat(", '" + mick. + "' ");
        sb.concat(", '" + mick.getSqlDate()  + "'");
        sb.concat(")");
        
           
           

        } catch (SQLException e) {
            conn.failed();
            System.out.println(e);
            
        } finally {
            conn.closeS(st);
            conn.close();
            
        }

        
      st = null;
        
        ResultSet rs = null;
        try{
            st = test.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                helpObject = new TreningsOkt(rs.getInt("OKTNR"),rs.getDate("DATO"), 
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"), 
                        rs.getString("TEKST"), rs.getString("BRUKERNAVN"));
                objects.add(helpObject);
            }
        } catch (SQLException e) {
            test.failed(); //Rollback
        } finally {
            test.closeS(st);
            test.closeR(rs);
            test.close();
        }
        
        for(Object e : objects){
            TreningsOkt t = (TreningsOkt) e;
            System.out.println(t.getBrukernavn() + "" + t.getOktNr());
        }
        
        
    }
}
