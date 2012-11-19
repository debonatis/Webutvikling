/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validator;

import com.corejsf.DBadm.DBController;
import com.corejsf.brukerAdm.BrukerStatus;
import java.util.List;
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

   
    boolean brukernavnOK = true;

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        String innlagtTekst = (String) object;      
        
        List<BrukerStatus> brukere = getAlleBrukere();
        
        for(BrukerStatus k : brukere){
            if(k.getBruker().getName().trim().equalsIgnoreCase(innlagtTekst)){
                brukernavnOK = false;
            }
        }       

        if (!brukernavnOK) {
            FacesMessage message = new FacesMessage();
            message.setSummary("This username is already in use, or it is one that is too similar. Write a new one!");

            throw new ValidatorException(message);


        }
    }
}
