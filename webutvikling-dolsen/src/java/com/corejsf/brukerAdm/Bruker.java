/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.brukerAdm;

import javax.faces.bean.RequestScoped;
import javax.faces.validator.Validator;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;

/**
 *
 * @author deb
 */
@RequestScoped
public class Bruker {

    private @NotNull
    @v
    String name;
    private String rolle;
    private String passord;
    private boolean endret;

    public boolean isEndret() {
        return endret;
    }

    public void setEndret(boolean endret) {
        this.endret = endret;
    }

    public Bruker() {
        this.name = "";
        this.rolle = "";
        this.passord = "";

    }

    public Bruker(String name, String passord, String rolle) {


        this.name = name;
        this.rolle = rolle;
        this.passord = passord;
        endret = false;
    }

    public Bruker(String name, String passord, String rolle, int i) {
        this.name = name;
        this.rolle = rolle;
        this.passord = passord;
        endret = false;
    }

    /**
     * Get the value of rolle
     *
     * @return the value of rolle
     */
    public String getRolle() {
        return rolle;
    }

    /**
     * Set the value of rolle
     *
     * @param rolle new value of rolle
     */
    public void setRolle(String rolle) {
        if (!(this.rolle.equalsIgnoreCase(rolle))) {
            this.rolle = rolle;
            setEndret(true);
        }
    }

    /**
     * Get the value of passord
     *
     * @return the value of passord
     */
    public String getPassord() {
        return passord;
    }

    /**
     * Set the value of passord
     *
     * @param passord new value of passord
     */
    public void setPassord(String passord) {
        if (!(this.passord.equalsIgnoreCase(passord))) {
            this.passord = passord;
            setEndret(true);
        }
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        if (!(this.name.equalsIgnoreCase(name))) {
            this.name = name;
            setEndret(true);
        }
    }
}
