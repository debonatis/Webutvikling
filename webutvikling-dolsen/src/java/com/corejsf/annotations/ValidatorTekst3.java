/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.annotations;

import com.corejsf.brukerAdm.BrukerBehandling;
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
// Er ikke en "FacesValidator-bean" fordi <f:validator... kaster timeout exeption!
public class ValidatorTekst3 extends BrukerBehandling implements ConstraintValidator<UsrPswWaplj, String> {

    private UsrPswWaplj sjekker;
    private boolean brukerNavnOK;
    private boolean brukerNavnRegexOK;
    private Pattern brukerNavnSjekk;
    private Matcher treff;
    private String brukerNavnKrav = "(.{4,10})";
    private String passordKrav = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&]).{6,10})";
    private List<BrukerStatus> brukere;

    @Override
    public void initialize(UsrPswWaplj sjekker) {
        this.sjekker = sjekker;
    }

    @Override
    public synchronized boolean isValid(String value, ConstraintValidatorContext arg1) {
        if (sjekker.passordsjekk() == 0) {
            brukerNavnOK = true;
            brukerNavnRegexOK = true;
            String innLagtTekst = value;
            if (!(sjekker.eventuellRegEx().equals(""))) {
                brukerNavnKrav = sjekker.eventuellRegEx();
            }
            brukerNavnSjekk = Pattern.compile(brukerNavnKrav);
            treff = brukerNavnSjekk.matcher(innLagtTekst);
            brukerNavnRegexOK = treff.matches();
            if (sjekker.sjekkDB() == 1) {
                brukere = getStatiskdbBrukerListe();
                if (brukere.isEmpty()) {
                    brukerNavnOK = true;
                } else {
                    for (BrukerStatus k : brukere) {
                        if (k.getBruker().getName().trim().equalsIgnoreCase(innLagtTekst)) {
                            brukerNavnOK = false;
                        }
                    }
                }
            }
            return (brukerNavnOK && brukerNavnRegexOK);
        }
        String innLagtTekst = value;
        brukerNavnSjekk = Pattern.compile(passordKrav);
        treff = brukerNavnSjekk.matcher(innLagtTekst);
        brukerNavnRegexOK = treff.matches();
        return brukerNavnRegexOK;
    }
}
