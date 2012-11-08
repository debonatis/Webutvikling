/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

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
import javax.sql.DataSource;
import javax.xml.transform.Source;

/**
 *
 * @author deb
 */
public class TEST {
       public static DBConnection conn = new DBConnection();
    
    public static void main(String[] args) throws Exception {
    
    
        
         TreningsOkt helpObject;
        ArrayList objects = new ArrayList<TreningsOkt>();
       
        Statement st = null;
        Date lolo = new Date(2012,11,22);
        TreningsOkt mick = new TreningsOkt(20,lolo, 23, "styrke", "mick");

        
        ResultSet rs = null;
        try{
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                helpObject = new TreningsOkt(rs.getInt("OKTNR"),rs.getDate("DATO"), 
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"), 
                        rs.getString("TEKST"));
                objects.add(helpObject);
            }
        } catch (SQLException e) {
            conn.failed(); //Rollback
        } finally {
            conn.closeS(st);
            conn.closeR(rs);
            conn.close();
        }
        
        for(Object e : objects){
            TreningsOkt t = (TreningsOkt) e;
            System.out.println(t.getBrukernavn() + "" + t.getOktNr());
        }
        DBConnection jk = new DBConnection();
       st = null;
        try {
            synchronized (jk){
                
            st = jk.getConn().createStatement();
            st.executeUpdate("DELETE FROM WAPLJ.TRENING WHERE OKTNR = "
                    + mick.getOktNr() + " AND BRUKERNAVN = '" + mick.getBrukernavn() + "'");
            }

        } catch (SQLException e) {
            jk.failed();
            

        } finally {
            jk.closeS(st);
            jk.close();
        }
        st = null;
        
        rs = null;
        
  
        try{
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.TRENING");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                helpObject = new TreningsOkt(rs.getInt("OKTNR"),rs.getDate("DATO"), 
                        rs.getInt("VARIGHET"), rs.getString("KATEGORINAVN"), 
                        rs.getString("TEKST"));
                objects.add(helpObject);
            }
        } catch (SQLException e) {
            conn.failed(); //Rollback
        } finally {
            conn.closeS(st);
            conn.closeR(rs);
            conn.close();
        }
        
        for(Object e : objects){
            TreningsOkt t = (TreningsOkt) e;
            System.out.println(t.getBrukernavn() + "" + t.getOktNr());
        
    }
        
         DBConnection conn = new DBConnection();
        
        PreparedStatement oppdaterOkter = null;
        String oppdaterString =
                "update WAPLJ.TRENING "
                + "set DATO = ?, VARIGHET= ?, "
                + "KATEGORINAVN= ?, TEKST= ? "
                + "where OKTNR = ? AND BRUKERNAVN= ?";
        OktStatus lol = new OktStatus(new TreningsOkt(2, new java.util.Date(), 3, "styrke", "lolol77"));
        ArrayList<OktStatus> liste = new ArrayList<OktStatus>();
        liste.add(lol);

        try {
            conn.getConn().setAutoCommit(false);
            oppdaterOkter = conn.getConn().prepareStatement(oppdaterString);            
            for(OktStatus f : liste){
                oppdaterOkter.setDate(1, f.getTreningsikOkt().getSqlDate());
                oppdaterOkter.setInt(2, f.getTreningsikOkt().getVarighet());
                oppdaterOkter.setString(3, f.getTreningsikOkt().getKategori());
                oppdaterOkter.setString(4, f.getTreningsikOkt().getTekst());
                oppdaterOkter.setInt(5, f.getTreningsikOkt().getOktNr());
                oppdaterOkter.setString(6, f.getTreningsikOkt().getBrukernavn());
                oppdaterOkter.executeUpdate();
                conn.getConn().commit();
                
            }
//            st = conn.getConn().createStatement();
//            st.executeUpdate("UPDATE TRENING SET DATO= '" + objekt.getSqlDate() + "', VARIGHET=" + objekt.getVarighet() + ", KATEGORINAVN ='" + objekt.getKategori() + "', TEKST='" + objekt.getTekst() + "' "
//                    + "WHERE OKTNR =" + objekt.getOktNr() + " AND  BRUKERNAVN = '" + objekt.getBrukernavn() + "';");
           

        } catch (SQLException e) {
            conn.failed();
             Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            if (conn.getConn() != null) {
            try {
                System.err.print("Endring har blitt trekk tilbake");
                conn.getConn().rollback();
            } catch(SQLException excep) {
                conn.failed();
            }
            }
            

        } finally {
            conn.closeS(st);
            conn.close();
        }
        
        
        Date test1 = new Date(2002, 11, 15);
        java.util.Date micke = new java.util.Date(2012, 11, 15);
        
        System.out.println( test1.toString());
       
        System.out.println(micke.toString());
        System.out.println( test1.toString());
       
        System.out.println(micke.toString());
        
        System.out.println( test1.toString());
       
        System.out.println(micke.toString());
        
        System.out.println( test1.toString());
       
        System.out.println(micke.toString());
        
       
        
        System.out.println(mick.getDate().toString());
        
        mick.setDate(new Date(2012, 11, 15));
         System.out.println(mick.getDate().toString());
         
         System.out.println(mick.getSqlDate().toString());
        
         Date mick7 = new Date(System.currentTimeMillis());
         System.out.println(mick7.toString());
        
        
       
}
}
