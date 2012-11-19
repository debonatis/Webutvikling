/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

/**
 *
 * @author deb
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatorTekst3.class)
@Documented
@NotNull
public @interface BrukerNavnsjekker {   
    String message() default "This username is already in use, or it is one that is too similar.\n Write a new one! The username must have a lenght between 6 to 10 characters!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
}
