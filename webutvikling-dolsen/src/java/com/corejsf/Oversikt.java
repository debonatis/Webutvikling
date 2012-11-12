/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;

/**
 *
 * @author Martin
 */
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class Oversikt implements Serializable {

    private Bruker sessionBruker = new Bruker();
    private ArrayList<TreningsOkt> alleOkter = new ArrayList();
    private ArrayList<TreningsOkt> hjelp = new ArrayList();
//    private @Length(min = 6, max = 20)
//    @Id
//    String bruker;

    public synchronized ArrayList<TreningsOkt> getAlleOkter() {

        return alleOkter;
    }

//    public synchronized String getBruker() {
//        return bruker == null ? sessionBruker.getName() : bruker;
//    }

    public synchronized void registrerNyOkt(TreningsOkt e) {
        alleOkter.add(e);
    }

    public synchronized void slettOkt(TreningsOkt t) {
        for (TreningsOkt e : alleOkter) {
            if (t.equals(e)) {
                alleOkter.remove(e);
            }
        }

    }

    public synchronized ArrayList<TreningsOkt> getPaManed(int m) {

        try {
            hjelp.clear();
            for (TreningsOkt k : alleOkter) {
                if ((k.getDate().getMonth()) == (m - 1)) {
                    hjelp.add(k);
                }
            }

            return hjelp;
        } catch (ConcurrentModificationException e) {
        }

        return hjelp;
    }

    public synchronized void slettAlle() {
        alleOkter.clear();
    }

    public int antallOkter() {
        return alleOkter.size();
    }
}
