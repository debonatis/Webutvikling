/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

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
        
        Statement st = null;
        
        ResultSet rs = null;
        try{
            st = test.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                helpObject = new TreningsOkt(rs.getDate("DATO"), 
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
            System.out.println(t.getBrukernavn());
        }
        
        
    }
}
