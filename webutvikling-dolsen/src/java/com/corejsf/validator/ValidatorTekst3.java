/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validator;

import com.corejsf.DBadm.DBController;
import com.corejsf.brukerAdm.BrukerStatus;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author deb
 */
@FacesValidator("validatorTekst3")
public class ValidatorTekst3 extends DBController implements Validator {

    private boolean brukerNavnOK = true;
    private String brukerNavnKrav = "(.{6,10})";
    private Pattern brukerNavnSjekk;
    private Matcher sjekker;

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        String innlagtTekst = (String) object;
        brukerNavnSjekk = Pattern.compile(brukerNavnKrav);
        sjekker = brukerNavnSjekk.matcher(innlagtTekst);
        brukerNavnOK = sjekker.matches();
        
        

        List<BrukerStatus> brukere = getAlleBrukere();

        for (BrukerStatus k : brukere) {
            if (k.getBruker().getName().trim().equalsIgnoreCase(innlagtTekst)) {
                brukerNavnOK = false;
            }
        }

        if (!brukerNavnOK) {
            FacesMessage message = new FacesMessage();
            message.setSummary("This username is already in use, or it is one that is too similar. Write a new one! The username must have a lenght between 6 to 10 characters!");

            throw new ValidatorException(message);


        }
    }
}
