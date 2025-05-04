package dds.monedero.model;

public class Saldo {
  private double dinero = 0;

  public Saldo(double dinero) {
    this.dinero = dinero;
  }

  public void setSaldo(double dinero) {
    this.dinero = dinero;
  }

  public double getSaldo() {
    return dinero;
  }
}
