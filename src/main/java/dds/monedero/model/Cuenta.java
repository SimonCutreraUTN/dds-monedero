package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta { //large class

  private Saldo saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = new Saldo(0);
  } //duplicated code

  public Cuenta(double montoInicial) {
    saldo = new Saldo(montoInicial);
  }

  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream()
        .filter(movimiento -> movimiento.fueDepositado(LocalDate.now()))
        .count() >= 3) { //shotgun surgery
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, tipoDeMov.DEPOSITO); //message chain
    agregarMovimiento(movimiento);
  } //long method

  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    var montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    var limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + 1000 + " diarios, " + "límite: " + limite);
    } //shotgun surgery
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, tipoDeMov.EXTRACCION);
    agregarMovimiento(movimiento);
  } //long method

  public void agregarMovimiento(LocalDate fecha, double cuanto, tipoDeMov tipo) {
    var movimiento = new Movimiento(fecha, cuanto, tipo);
    movimientos.add(movimiento);
  } //long parameters list

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.getTipoDeposito() && movimiento.getFecha().equals(fecha)) //message chain
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public void agregarMovimiento(Movimiento movimiento) {
    setSaldo(movimiento.calcularValor(this));
    agregarMovimiento(movimiento.getFecha(), movimiento.getMonto(), movimiento.getTipo());
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public double getSaldo() {
    return saldo.getSaldo();
  }

  public void setSaldo(double dinero) {
    saldo.setSaldo(dinero);
  }

}
