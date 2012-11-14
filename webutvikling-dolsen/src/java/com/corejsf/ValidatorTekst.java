/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.util.regex.*;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;
import javax.faces.validator.*;

/**
 *
 * @author deb
 */
@FacesValidator("validatorTekst")
public class ValidatorTekst implements Validator {

//    private boolean PassordSjekkOK = false;
//    private Pattern passordSjekk;
//    private Matcher sjekker;
//    private String passordKrav = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {


//        if (uIComponent.getId().equalsIgnoreCase("settpassord")) {
//            String innlagtTekst = (String) object;
//            passordSjekk = Pattern.compile(passordKrav);
//            sjekker = passordSjekk.matcher(innlagtTekst);
//            PassordSjekkOK = sjekker.matches();
//            if (!PassordSjekkOK) {
//                FacesMessage message = new FacesMessage();
//                message.setSummary("The password must contain at least one uppercase "
//                        + "and one lowercase letter and one number. "
//                        + "Password must be between 6-20 characters!");
//
//                throw new ValidatorException(message);
//
//            }
//        }
        UIInput passwordComponent = (UIInput) uIComponent.getAttributes().get("passwordComponent");
        String password = (String) passwordComponent.getValue();
        String confirmPassword = (String) object;

        if (confirmPassword != null && !confirmPassword.equals(password)) {
            throw new ValidatorException(new FacesMessage(
                    "Confirm password is not the same as password"));
        }
    }
}
