/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.DBadm.DBController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author deb
 */
@Named
@SessionScoped
@DeclareRoles({"admin", "bruker"})
@RolesAllowed({"admin", "bruker"})
public class TreningsOktBehandling extends DBController implements Serializable {

    private FacesMessage fm;
    private List<OktStatus> DBtreningsobjekter;
    private Oversikt nyOversikt;
    private List<OktStatus> treningsOkter;
    private List<OktStatus> temptreningsOkter;
    private List<OktStatus> hjelp;
    private TreningsOkt tempOkt;
    private ArrayList<TreningsOkt> hjelp2;
    private @NotNull
    @Range(min = 0, max = 12)
    int maned = 0;
    private boolean nyOkt = false;
    private FacesContext fc;
    private TimeZone tidssone;
    private boolean sortAscendingK = true;
    private boolean sortAscendingT = true;
    private boolean sortAscendingV = true;
    private boolean sortAscendingD = true;

    public TreningsOktBehandling() {

        DBtreningsobjekter = Collections.synchronizedList(new ArrayList<OktStatus>());
        nyOversikt = new Oversikt();
        treningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
        temptreningsOkter = Collections.synchronizedList(new ArrayList<OktStatus>());
        hjelp = Collections.synchronizedList(new ArrayList<OktStatus>());
        tempOkt = new TreningsOkt();
        hjelp2 = new ArrayList<>();
    }

    public boolean isNyOkt() {
        return nyOkt;
    }

    public int getManed() {
        return maned;
    }

    public void setManed(int Maned) {
        this.maned = Maned;
    }

    public TimeZone getTidssone() {
// Kan også sette dette i web.xml  
//        (Skal egentlig ikke brukes hvis side ment brukt internasjonalt)
//      <context-param>
//        <param-name>DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE</param-name>
//        <param-value>true</param-value>
//      </context-param>
        
        this.tidssone = TimeZone.getDefault();
        return tidssone == null ? TimeZone.getTimeZone("GMT") : tidssone;
    }

    public synchronized List<OktStatus> getTemptreningsOkter() {
        temptreningsOkter.clear();
        tempOkt = new TreningsOkt();
        temptreningsOkter.add(new OktStatus(tempOkt));
        return temptreningsOkter;
    }

    public synchronized void setNyOkt(boolean nyOkt) {
        this.nyOkt = nyOkt;
    }
    
    public boolean getDatafins() {
        if ((getManed() >= 1)) {
            getTabelldata();
            return (!hjelp.isEmpty());
        }
        return (!treningsOkter.isEmpty());
    }

    public List<OktStatus> getTabelldata() {
        int m;
        m = maned;
        if ((getManed() >= 1)) {
            hjelp.clear();
            hjelp2 = nyOversikt.getPaManed(m);
            try {
                for (TreningsOkt g : hjelp2) {
                    hjelp.add(new OktStatus(g));
                }
                return hjelp;
            } catch (ConcurrentModificationException e) {
                getTabelldata();
            }
            setManed(0);
        }
        return treningsOkter;
    }

    public synchronized int getAntOkter() {
        return getTabelldata().size();
    }

    public synchronized int getGjennomsnitt() {
        int max = 0;
        int indeks = 0;
        for (OktStatus t : getTabelldata()) {
            max += t.getTreningsikOkt().getVarighet();
            indeks++;
        }
        if (indeks == 0) {
            indeks = 1;
        }
        return max / indeks;
    }

    public synchronized String getNavn() {
        return nyOversikt.getBruker();
    }

    public synchronized TreningsOkt getTempOkt() {
        return tempOkt;
    }

    public synchronized void setTempOkt(TreningsOkt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized String oppdater() {
        nyOkt = false;
        try {
            if (!(getTempOkt().getVarighet() == 0)) {
                TreningsOkt nyOkt;
                nyOkt = new TreningsOkt(getTempOkt().getOktNr(), new Date(getTempOkt().getDate().getTime()),
                        getTempOkt().getVarighet(), getTempOkt().getKategori(),
                        getTempOkt().getTekst());
                nyOversikt.registrerNyOkt(nyOkt);
                treningsOkter.add(new OktStatus(nyOkt));
                registrerTreningsOkt(nyOkt, getNavn());
                tempOkt = new TreningsOkt();
            }
            if (!(treningsOkter.isEmpty())) {
                int indeks = treningsOkter.size() - 1;
                while (indeks >= 0) {
                    OktStatus ts = treningsOkter.get(indeks);
                    if (ts.getSkalSlettes()) {
                        nyOversikt.slettOkt(ts.getTreningsikOkt());
                        treningsOkter.remove(indeks);
                        slettTreningsOkt(ts.getTreningsikOkt(), 1, getNavn());
                    }
                    indeks--;
                }
            }
            oppdaterTreningsOktDB(treningsOkter, getNavn());
            DBtreningsobjekter = getAlleTreningsOkter(getNavn());
            if (!DBtreningsobjekter.isEmpty()) {
                nyOversikt.slettAlle();
                treningsOkter.clear();
                for (OktStatus s : DBtreningsobjekter) {
                    nyOversikt.registrerNyOkt(s.getTreningsikOkt());
                    treningsOkter.add(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            oppdater();
        }
        return "success";
    }

    public String sorterPaaKategori() {
        if (sortAscendingK) {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return okt1.getTreningsikOkt().getKategori().
                            compareTo(okt2.getTreningsikOkt().getKategori());
                }
            });
            sortAscendingK = false;
        } else {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return okt2.getTreningsikOkt().getKategori().compareTo(okt1.getTreningsikOkt().getKategori());
                }
            });
            sortAscendingK = true;
        }
        return null;
    }

    public String sorterPaaTekst() {
        if (sortAscendingT) {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return okt1.getTreningsikOkt().getTekst().compareTo(okt2.getTreningsikOkt().getTekst());
                }
            });
            sortAscendingT = false;
        } else {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return okt2.getTreningsikOkt().getTekst().compareTo(okt1.getTreningsikOkt().getTekst());
                }
            });
            sortAscendingT = true;
        }
        return null;
    }
    
    public String sorterPaaDato() {
        if (sortAscendingD) {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return okt1.getTreningsikOkt().getDate().compareTo(okt2.getTreningsikOkt().getDate());
                }
            });
            sortAscendingD = false;
        } else {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return okt2.getTreningsikOkt().getDate().compareTo(okt1.getTreningsikOkt().getDate());
                }
            });
            sortAscendingD = true;
        }
        return null;
    }
   
    public String sorterPaaVarighet() {
        if (sortAscendingV) {
            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return ((Integer) okt1.getTreningsikOkt().getVarighet()).
                            compareTo(((Integer) okt2.getTreningsikOkt().getVarighet()));
                }
            });
            sortAscendingV = false;

        } else {


            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {
                    return ((Integer) okt2.getTreningsikOkt().getVarighet()).
                            compareTo(((Integer) okt1.getTreningsikOkt().getVarighet()));
                }
            });
            sortAscendingV = true;
        }
        return null;
    }
    public String sorterPaaSlett() {

        if (sortAscendingV) {


            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {

                    return ((Boolean)okt1.getSkalSlettes()).compareTo(okt2.getSkalSlettes());

                }
            });
            sortAscendingV = false;

        } else {


            Collections.sort(treningsOkter, new Comparator<OktStatus>() {
                @Override
                public int compare(OktStatus okt1, OktStatus okt2) {

                    return ((Boolean) okt2.getSkalSlettes()).compareTo(okt1.getSkalSlettes());

                }
            });
            sortAscendingV = true;
        }
        return null;
    }
}
