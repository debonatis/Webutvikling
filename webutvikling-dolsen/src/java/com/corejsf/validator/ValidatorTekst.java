/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validator;

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

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        UIInput passwordComponent = (UIInput) uIComponent.getAttributes().get("passwordComponent");
        String password = (String) passwordComponent.getValue();
        String confirmPassword = (String) object;

        if (confirmPassword != null && !confirmPassword.equals(password)) {
            throw new ValidatorException(new FacesMessage(
                    "Confirm password is not the same as password"));
        }
    }
}
