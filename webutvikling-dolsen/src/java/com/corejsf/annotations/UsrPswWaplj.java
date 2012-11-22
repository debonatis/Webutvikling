/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.annotations;

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
public  @interface UsrPswWaplj {
    String message() default "{com.corejsf.Lurifakssjekker.melding}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String eventuellRegEx() default "";
    int passordsjekk() default 0; 
    int sjekkDB() default 0;
}
