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
// Er ikke ne bean fordi <f:validator... kunne tydeligvis ikke forekomme mer enn en gang i en form (Ja, det er unike Id'er)
@FacesValidator("validatorTekst3")
public abstract class ValidatorTekst3 extends DBController implements Validator {

    private boolean brukerNavnOK;
    private boolean brukerNavnRegexOK;
    private String brukerNavnKrav = "(.{6,10})";
    private Pattern brukerNavnSjekk;
    private Matcher sjekker;
    private List<BrukerStatus> brukere;

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        brukerNavnOK = true;
        brukerNavnRegexOK = true;
        String innLagtTekst = (String) object;
        brukerNavnSjekk = Pattern.compile(brukerNavnKrav);
        sjekker = brukerNavnSjekk.matcher(innLagtTekst);
        brukerNavnRegexOK = sjekker.matches();

        brukere = getAlleBrukere();

        for (BrukerStatus k : brukere) {
            if (k.getBruker().getName().trim().equalsIgnoreCase(innLagtTekst)) {
                brukerNavnOK = false;
            }
        }
        if ((!brukerNavnOK) || (!brukerNavnRegexOK) ) {
            FacesMessage message = new FacesMessage();
            message.setSummary("This username is already in use, or it is one that is too similar. Write a new one! The username must have a lenght between 6 to 10 characters!");

            throw new ValidatorException(message);


        }
    }
}
