/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.validator.*;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;
import java.util.regex.*;

/**
 *
 * @author deb
 */
public class ValidatorTekst implements Validator {

    private boolean kategoriOK = false;
    private boolean PassordSjekkOK = false;
    private Pattern passordSjekk;
    private Matcher sjekker;
    private String passordKrav = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    public ValidatorTekst() {
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        String innlagtTekst = (String) object;
        passordSjekk = Pattern.compile(passordKrav);
        sjekker = passordSjekk.matcher(innlagtTekst);
        PassordSjekkOK = sjekker.matches();

        kategoriOK = kategoriSjekker(innlagtTekst);


        if (uIComponent.getId().equalsIgnoreCase("kategori3")) {
            if (!kategoriOK) {
                FacesMessage message = new FacesMessage();
                message.setSummary("You must select a valid category. "
                        + "Which is either biking, jogging, aerobics or strength.");

                throw new ValidatorException(message);

            }
        }
        
         if (uIComponent.getId().equalsIgnoreCase("passord")) {
        if (!PassordSjekkOK) {
            FacesMessage message = new FacesMessage();
            message.setSummary("The password must contain at least one uppercase "
                    + "and one lowercase letter and one number. "
                    + "Password must be between 6-20 characters!");

            throw new ValidatorException(message);

        }
    }
    }

    public synchronized boolean kategoriSjekker(String s) {

        DBConnection conn = new DBConnection();
        ArrayList<String> hjelp = new ArrayList<>();
        Statement st = null;

        ResultSet rs = null;
        try {
            st = conn.getConn().createStatement();
            rs = st.executeQuery("SELECT * FROM WAPLJ.KATEGORI");
            // WHERE BRUKERNAVN = '" + user + "' (for senere bruk)

            while (rs.next()) {
                hjelp.add(rs.getString("KATEGORINAVN"));

            }
        } catch (SQLException e) {
            conn.failed(); //Rollback
        } finally {
            conn.closeS(st);
            conn.closeR(rs);
            conn.close();

        }

        for (String k : hjelp) {
            if (s.toLowerCase().equalsIgnoreCase(k)) {
                return true;
            }

        }

        return false;
    }
}