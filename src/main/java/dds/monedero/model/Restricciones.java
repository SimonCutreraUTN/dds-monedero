package dds.monedero.model;

import java.time.LocalDate;

public class Restricciones {
  private int limiteDepositos;
  private double limiteMontoExtracciones;

  Restricciones(int depositos, double extraccionesMonto) {
    limiteDepositos = depositos;
    limiteMontoExtracciones = extraccionesMonto;
  }

  public double getLimiteMontoExtracciones() {
    return limiteMontoExtracciones;
  }

  public int getLimiteDepositos() {
    return limiteDepositos;
  }

  public void setLimiteMontoExtracciones(double extraccionesMonto) {
    limiteMontoExtracciones = extraccionesMonto;
  }

  public void setLimiteDepositos(int limiteDepositos) {
    this.limiteDepositos = limiteDepositos;
  }

  public double getLimiteActual(double valorExtraido) {
    return getLimiteMontoExtracciones() - valorExtraido;
  }
}
