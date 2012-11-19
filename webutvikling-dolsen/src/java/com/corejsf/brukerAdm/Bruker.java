/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.brukerAdm;

/**
 *
 * @author deb
 */
public class Bruker {

    private String name;
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

    public Bruker(String name, String rolle, String passord) {
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
        this.rolle = rolle;
        setEndret(true);
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
        this.passord = passord;
        setEndret(true);
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
        this.name = name;
        setEndret(true);
    }
}
