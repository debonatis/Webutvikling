package com.corejsf.brukerAdm;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.corejsf.DBadm.DBController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Cacheable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author deb
 */
@ManagedBean(name = "bruker")
@Cacheable(false)
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class BrukerBehandling extends DBController implements Serializable {

    private String name;
    private String newPassword;
    private String newPassword2;
    private List<BrukerStatus> bOversikt = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private List<BrukerStatus> bOversikthjelp = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private static final Logger logger = Logger.getLogger("com.corejsf");
    private FacesMessage fm = new FacesMessage();
    private FacesContext fc;
    private final String[] roller = {"admin", "bruker"};
    private boolean nyBruker;
    private List<BrukerStatus> tempBrukerListe = Collections.synchronizedList(new ArrayList<BrukerStatus>());
    private Bruker tempBruker = new Bruker();
    private List<BrukerStatus> dbBrukerListe = Collections.synchronizedList(new ArrayList<BrukerStatus>());

    public boolean isNyBruker() {
        return nyBruker;
    }

    public List<BrukerStatus> getTempBrukerListe() {
        tempBrukerListe.clear();
        tempBrukerListe.add(new BrukerStatus(tempBruker));
        return tempBrukerListe;
    }

    public void setTempBrukerListe(List<BrukerStatus> tempBrukerListe) {
        this.tempBrukerListe = tempBrukerListe;
    }

    public Bruker getTempBruker() {
        return tempBruker;
    }

    public void setTempBruker(Bruker tempBruker) {
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
        logout();
        return "NO ROLE, logging you out!";
    }

    public String getName() {
        if (name == null) {
            getUserData();
        }
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

    public boolean isAdmin() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object forsporrselobject = context.getRequest();
        if (!(forsporrselobject instanceof HttpServletRequest)) {
            logger.log(Level.SEVERE, "Det forespurte objektet er av type {0}", forsporrselobject.getClass());
            return false;
        }
        String hjelp = "admin";
        HttpServletRequest foresporrsel2 = (HttpServletRequest) forsporrselobject;
        return foresporrsel2.isUserInRole(hjelp);
    }

    public String logout() {


        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();

        } catch (ServletException e) {
            logger.log(Level.SEVERE, "Failed to logout user!", e);
            fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout failed!", "");
            fc = FacesContext.getCurrentInstance();
            fc.addMessage("null", fm);
            fc.renderResponse();

            return "ikkok";

        }
        fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout OK!", "");
        fc = FacesContext.getCurrentInstance();
        fc.addMessage("null", fm);
        fc.renderResponse();
        return "ok";


    }

    synchronized String skiftPassord(String passord) {
        return skiftPassordDB(passord, getName());
    }

    public synchronized List<BrukerStatus> getBrukerTabell() {
        return bOversikt;
    }

    public synchronized boolean datafins() {
   return (!bOversikt.isEmpty());
        
    }
@RolesAllowed("admin")
    public String oppdater() {
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
                nyBruker = new Bruker(getTempBruker().getName(), getTempBruker().getPassord(),
                        getTempBruker().getRolle());



                bOversikt.add(new BrukerStatus(nyBruker));
                registrerBruker(nyBruker);
                setTempBruker(new Bruker());

            }

            oppdaterBrukerDB(bOversikt);
            dbBrukerListe = getAlleBrukere();
            if (!dbBrukerListe.isEmpty()) {
                bOversikt.clear();
                for (BrukerStatus s : dbBrukerListe) {
                    bOversikt.add(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            oppdater();
        }
        return "success";
    }
}