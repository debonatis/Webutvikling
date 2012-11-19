package com.corejsf.validator;

import java.util.regex.*;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;
import javax.faces.validator.*;

/**
 *
 * @author deb
 */
@FacesValidator("validatorTekst2")
public class ValidatorTekst2 implements Validator {

    private boolean PassordSjekkOK = false;
    private Pattern passordSjekk;
    private Matcher sjekker;
    private String passordKrav = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&]).{6,20})";

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        String innlagtTekst = (String) object;
        passordSjekk = Pattern.compile(passordKrav);
        sjekker = passordSjekk.matcher(innlagtTekst);
        PassordSjekkOK = sjekker.matches();

        if (!PassordSjekkOK) {
            FacesMessage message = new FacesMessage();
            message.setSummary("The password must contain at least one uppercase "
                    + "and one lowercase letter and one number. \n  The password must also contain one of following special characters(@#$%&). "
                    + "Password must be between 6-20 characters!");

            throw new ValidatorException(message);


        }
    }
}