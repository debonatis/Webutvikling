package com.corejsf.brukerAdm;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.corejsf.DBadm.DBController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.Cacheable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author deb
 */
@Named("bruker")
@SessionScoped
@Cacheable(false)
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class BrukerBehandling extends DBController implements Serializable {

    private String name;
    private String newPassword;
    private String newPassword2;
    private List<BrukerStatus> bOversikt = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private static final Logger logger = Logger.getLogger("com.corejsf");
    private FacesMessage fm = new FacesMessage();
    private FacesContext fc;
    private final String[] roller = {"admin", "bruker"};
    private boolean nyBruker;
    private List<BrukerStatus> tempBrukerListe = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private Bruker tempBruker = new Bruker();
    private List<BrukerStatus> dbBrukerListe = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private static int teller = 0;
    private static List<BrukerStatus> statiskdbBrukerListe = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private boolean adminOK;
    private boolean sortRolle = true;
    private boolean sortBruker = true; 

    public boolean isAdminOK() {
        this.adminOK = (getRolle().equals("admin")) ? true : false;
        return adminOK;
    }

    public void setAdminOK(boolean adminOK) {
        this.adminOK = adminOK;
    }

    public boolean isNyBruker() {
        return nyBruker;
    }

    public synchronized List<BrukerStatus> getTempBrukerListe() {
        tempBrukerListe.clear();
        tempBruker = new Bruker();
        tempBrukerListe.add(new BrukerStatus(tempBruker));
        return tempBrukerListe;
    }

    public void setTempBrukerListe(List<BrukerStatus> tempBrukerListe) {
        this.tempBrukerListe = tempBrukerListe;
    }

    public synchronized Bruker getTempBruker() {
        return tempBruker;
    }

    public synchronized void setTempBruker(Bruker tempBruker) {
        this.tempBruker = tempBruker;
    }

    public void setNyBruker(boolean nyBruker) {
        this.nyBruker = nyBruker;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getRolle() {
        for (String r : roller) {
            if (isInRole(r)) {
                return r;
            }
        }
        this.logout();
        return "NO ROLE, logging you out!";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(true);
        session.invalidate();

        try {
            request.logout();
            request.logout();
        } catch (ServletException e) {
        }
        return "ok";
    }

    public String getName() {
        getUserData();
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void getUserData() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        setName(foresporrsel.getRemoteUser());
    }

    public boolean isInRole(String k) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return false;
        }
        HttpServletRequest foresporrsel = (HttpServletRequest) forsporrselobject;
        return foresporrsel.isUserInRole(k);
    }

    public static List<BrukerStatus> getStatiskdbBrukerListe() {
        return statiskdbBrukerListe;
    }

    public synchronized String changePassword() {
        return skiftPassordDB(getNewPassword(), getName());
    }

    @RolesAllowed("admin")
    public synchronized List<BrukerStatus> getBrukerTabell() {
        return bOversikt;
    }

    @RolesAllowed("admin")
    public synchronized boolean datafins() {
        return (!bOversikt.isEmpty());
    }

    public synchronized String oppdater() {

        setNyBruker(false);
        teller++;
        try {
            if (!(bOversikt.isEmpty())) {
                int indeks = bOversikt.size() - 1;
                while (indeks >= 0) {
                    BrukerStatus bs = bOversikt.get(indeks);
                    if (bs.isSkalSlettes()) {
                        bOversikt.remove(indeks);
                        slettBruker(bs.getBruker());
                    }
                    indeks--;
                }
            }
            if (!(getTempBruker().getName().equalsIgnoreCase(""))) {
                Bruker nyBruker;
                String rolle = (getTempBruker().getRolle().equals("")) ? "bruker" : getTempBruker().getRolle();
                nyBruker = new Bruker(getTempBruker().getName(), getTempBruker().getPassord(),
                        rolle);
                bOversikt.add(new BrukerStatus(nyBruker));
                statiskdbBrukerListe.add(new BrukerStatus(nyBruker));
                registrerBruker(nyBruker);
                setTempBruker(new Bruker());
            }

            int antall = oppdaterBrukerDB(getBrukerTabell());
            if (antall >= 1 || teller == 1) {
                dbBrukerListe = getAlleBrukere();
                if (!dbBrukerListe.isEmpty()) {
                    bOversikt.clear();
                    statiskdbBrukerListe.clear();
                    for (BrukerStatus s : dbBrukerListe) {
                        bOversikt.add(s);
                        statiskdbBrukerListe.add(s);
                    }
                    dbBrukerListe.clear();
                }
            }
        } catch (ConcurrentModificationException e) {
            oppdater();
        }
        return "success";
    }

    public String sorterPaaRolle() {

        if (sortRolle) {
            Collections.sort(bOversikt, new Comparator<BrukerStatus>() {
                @Override
                public int compare(BrukerStatus bruker1, BrukerStatus bruker2) {
                    return bruker1.getBruker().getRolle().compareTo(bruker2.getBruker().getRolle());
                }
            });
            sortRolle = false;
        } else {
            Collections.sort(bOversikt, new Comparator<BrukerStatus>() {
                @Override
                public int compare(BrukerStatus bruker1, BrukerStatus bruker2) {
                    return bruker2.getBruker().getRolle().compareTo(bruker1.getBruker().getRolle());
                }
            });
            sortRolle = true;
        }
        return null;
    }

    public String sorterPaaBrukernavn() {

        if (sortBruker) {
            Collections.sort(bOversikt, new Comparator<BrukerStatus>() {
                @Override
                public int compare(BrukerStatus bruker1, BrukerStatus bruker2) {
                    return bruker1.getBruker().getName().compareTo(bruker2.getBruker().getName());
                }
            });
            sortBruker = false;
        } else {
            Collections.sort(bOversikt, new Comparator<BrukerStatus>() {
                @Override
                public int compare(BrukerStatus bruker1, BrukerStatus bruker2) {
                    return bruker2.getBruker().getName().compareTo(bruker1.getBruker().getName());
                }
            });
            sortBruker = true;
        }
        return null;
    }
}
