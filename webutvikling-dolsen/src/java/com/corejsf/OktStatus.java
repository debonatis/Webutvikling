/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

  
  public synchronized boolean getSkalSlettes() {
    return skalSlettes;
  }
  public synchronized void setSkalSlettes(boolean nySkalSlettes) {
    skalSlettes = nySkalSlettes;
  }

  
  public TreningsOkt getTreningsikOkt() {
    return treningsOkten;
  }
  public void setTreningsOkten(TreningsOkt nyTreningsOkt) {
    treningsOkten = nyTreningsOkt;
  }
}




