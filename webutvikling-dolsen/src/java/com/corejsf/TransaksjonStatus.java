/*
 * TransaksjonStatus.java
 *
 * Presentasjonsklasse for en transaksjon.
 */

package com.corejsf;

public class OktStatus {
  private TreningsOkt treningsOkten;
  private boolean skalSlettes;

  public OktStatus() {
    treningsOkten = new TreningsOkt();
    skalSlettes = false;
  }

  public OktStatus(TreningsOkt treningsOkten) {
    this.treningsOkten = treningsOkten;
    skalSlettes = false;
  }

  /* EGENSKAP: skalSlettes */
  public synchronized boolean getSkalSlettes() {
    return skalSlettes;
  }
  public void setSkalSlettes(boolean nySkalSlettes) {
    skalSlettes = nySkalSlettes;
  }

  /* EGENSKAP: transaksjonen */
  public TreningsOkt getTreningsikOkt() {
    return treningsOkten;
  }
  public void setTreningsOkten(TreningsOkt nyTreningsOkt) {
    treningsOkten = nyTreningsOkt;
  }
}



