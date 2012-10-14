/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author deb
 */

@Named
@SessionScoped
public class treningsOktBehandler implements Serializable{
    
 private Oversikt nyOversikt = new Oversikt(); 
 private List<TreningsOkt> treningsOkter = Collections.synchronizedList(new ArrayList<TreningsOkt>());
 
 private TreningsOkt tempOkt = new TreningsOkt(); // midlertidig lager for ny transaksjon
/* EGENSKAP: datafins */ // tabell skal vises kun hvis data fins
public synchronized boolean getDatafins() {
return (!treningsOkter.isEmpty());
}
/* EGENSKAP: tabelldata */
public synchronized List<TreningsOkt> getTabelldata() {
return treningsOkter;
}
/* EGENSKAP: navn */
public synchronized String getNavn() {
return nyOversikt.getBruker();
}
public synchronized void setNavn(String nyttBrukernavn) {
nyOversikt.setBruker(nyttBrukernavn);
}
/* EGENSKAP: sum */
public synchronized ArrayList<TreningsOkt> getAlleOkter() {
return nyOversikt.getAlleOkter();
}
/* EGENSKAP: tempTrans*/ // for midlertidig lagring av transaksjonsdata
public synchronized TreningsOkt getTempOkt() {
return tempOkt;
}
public synchronized void setTempOkt(TreningsOkt nyTempOkt) {
tempOkt = nyTempOkt;
}
/*
* action-metode som kalles i JSF
* Metoden behandler registrering av nye transaksjoner og
* sletting av transaksjoner.
* (Editering av data som vises i tabellen skjer automatisk)
*/
public synchronized void oppdater() {
/* Ny transaksjon er midlertidig lagret i tempTrans */
if (!tempOkt.getKategori().trim().equals("")) {
/* Lagrer data om ny transaksjon permanent */
TreningsOkt nyOkt = new TreningsOkt(tempOkt.getOktNr(), tempOkt.getDate(), tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getKategori());

nyOversikt.registrerNyOkt(nyOkt); // lagrer i problemdomeneobjekt
treningsOkter.add(nyOkt); // lagrer i pres.-objektet
nyOkt.nullstill();
}
/* Sletter alle transaksjoner som er merket for sletting */
int indeks = treningsOkter.size() - 1;
      
// while (indeks >= 0) {
//TreningsOkt ts = treningsOkter.get(indeks);
//if (ts.getSkalSlettes()) { // sletter data, først i … oversikt.slettTransaksjon(ts.getTransaksjonen());// … problemdomeneobj.
//tabelldata.remove(indeks); // deretter i presentasjonsobjektet
//}
//indeks--;
// }
}
 
    
    
}
