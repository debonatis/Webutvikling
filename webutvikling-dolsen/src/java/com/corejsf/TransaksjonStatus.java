/*
 * TransaksjonStatus.java
 *
 * Presentasjonsklasse for en transaksjon.
 */

package regnskap;

public class TransaksjonStatus {
  private Transaksjon transaksjonen;
  private boolean skalSlettes;

  public TransaksjonStatus() {
    transaksjonen = new Transaksjon();
    skalSlettes = false;
  }

  public TransaksjonStatus(Transaksjon transaksjonen) {
    this.transaksjonen = transaksjonen;
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
  public Transaksjon getTransaksjonen() {
    return transaksjonen;
  }
  public void setTransaksjonen(Transaksjon nyTransaksjon) {
    transaksjonen = nyTransaksjon;
  }
}



