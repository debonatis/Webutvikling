/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.brukerAdm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author deb
 */
public class BrukerStatus {

    private boolean skalSlettes;
    private Bruker bruker;

    public Bruker getBruker() {
        return bruker;
    }

    public void setBruker(Bruker bruker) {
        this.bruker = bruker;
    }

    public BrukerStatus(Bruker bruker) {
        this.bruker = bruker;
        this.skalSlettes = false;
    }

    public boolean isSkalSlettes() {
        return skalSlettes;
    }

    public void setSkalSlettes(boolean skalSlettes) {
        this.skalSlettes = skalSlettes;
    }
}
