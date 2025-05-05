package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private Saldo saldo;
  private List<Movimiento> movimientos = new ArrayList<>();
  private Restricciones restriccion = new Restricciones(3, 1000);

  public Cuenta() {
    saldo = new Saldo(0);
  }

  public Cuenta(double montoInicial) {
    saldo = new Saldo(montoInicial);
  }

  public void setRestriccion(int limiteDepositos, double extraccionesMonto) {
    restriccion.setLimiteDepositos(limiteDepositos);
    restriccion.setLimiteMontoExtracciones(extraccionesMonto);
  }

  public void poner(double cuanto) {
    verificarPoner(cuanto);
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, tipoDeMov.DEPOSITO); //long parameter list
    agregarMovimiento(movimiento);
  }

  private void verificarPoner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (verificarCantidadDepositos()) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private boolean verificarCantidadDepositos() {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueDepositado(LocalDate.now()))
        .count() >= restriccion.getLimiteDepositos();
  }

  public void sacar(double cuanto) {
    verificarSacar(cuanto);
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, tipoDeMov.EXTRACCION);
    agregarMovimiento(movimiento);
  }

  private void verificarSacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    if (verificarExtraccionDiario(cuanto)) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + restriccion.getLimiteMontoExtracciones() + " diarios, " + "límite: " + restriccion.getLimiteActual(montoDeHoy())
      );
    }
  }

  private boolean verificarExtraccionDiario(double cuanto) {
    return cuanto > restriccion.getLimiteActual(montoDeHoy());
  }

  private double montoDeHoy() {
    return getMontoExtraidoA(LocalDate.now());
  }

  private void agregarMovimientoALista(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public void agregarMovimiento(Movimiento movimiento) {
    saldo.modificarPorMovimiento(movimiento);
    agregarMovimientoALista(movimiento);
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
