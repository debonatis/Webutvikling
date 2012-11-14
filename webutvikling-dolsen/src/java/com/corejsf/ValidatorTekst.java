/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.*;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;
import javax.faces.validator.*;

/**
 *
 * @author deb
 */
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class ValidatorTekst implements Validator {

    
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

        if (uIComponent.getId().equalsIgnoreCase("settpassord")) {
            if (!PassordSjekkOK) {
                FacesMessage message = new FacesMessage();
                message.setSummary("The password must contain at least one uppercase "
                        + "and one lowercase letter and one number. "
                        + "Password must be between 6-20 characters!");

                throw new ValidatorException(message);

            }
        }
    }
}
