package dds.monedero.model;

public class Saldo {
  private double dinero;

  public Saldo(double dinero) {
    this.dinero = dinero;
  }

  public void setSaldo(double dinero) {
    this.dinero = dinero;
  }

  public double getSaldo() {
    return dinero;
  }

  public void modificarPorMovimiento(Movimiento movimiento) {
    setSaldo(calcularValor(movimiento));
  }

  private double calcularValor(Movimiento movimiento) {
    return this.getSaldo() + movimiento.calcularAgregado();
  }
}
