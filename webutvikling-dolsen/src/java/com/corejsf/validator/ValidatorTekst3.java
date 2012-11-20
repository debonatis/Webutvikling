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
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author deb
 */
// Er ikke ne bean fordi <f:validator... kunne tydeligvis ikke forekomme mer enn en gang i en form (Ja, det er unike Id'er)
public class ValidatorTekst3 extends DBController implements ConstraintValidator<BrukerNavnsjekker, String> {

    private BrukerNavnsjekker sjekker;
    private boolean brukerNavnOK;
    private boolean brukerNavnRegexOK;
    private Pattern brukerNavnSjekk;
    private Matcher treff;
    private String brukerNavnKrav = "(.{4,10})";
    private List<BrukerStatus> brukere;  
    

    @Override
    public void initialize(BrukerNavnsjekker sjekker) {
        this.sjekker = sjekker;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext arg1) {
        brukerNavnOK = true;
        brukerNavnRegexOK = true;
        String innLagtTekst = value;
        if(!(sjekker.eventuellRegEx().equals(""))){
            brukerNavnKrav = sjekker.eventuellRegEx();
        }
        brukerNavnSjekk = Pattern.compile(brukerNavnKrav);
        treff = brukerNavnSjekk.matcher(innLagtTekst);
        brukerNavnRegexOK = treff.matches();

        brukere = getAlleBrukere();

        for (BrukerStatus k : brukere) {
            if (k.getBruker().getName().trim().equalsIgnoreCase(innLagtTekst)) {
                brukerNavnOK = false;
            }
        }
        return  (brukerNavnOK && brukerNavnRegexOK);
    }
}


