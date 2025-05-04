package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  // Nota: En ningún lenguaje de programación usen jamás doubles (es decir, números con punto flotante) para modelar dinero en el mundo real.
  // En su lugar siempre usen numeros de precision arbitraria o punto fijo, como BigDecimal en Java y similares
  // De todas formas, NO es necesario modificar ésto como parte de este ejercicio. 
  private double monto;
  private tipoDeMov tipo;

  public Movimiento(LocalDate fecha, double monto, tipoDeMov tipo) {
    this.fecha = fecha;
    this.monto = monto;
    this.tipo = tipo;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public tipoDeMov getTipo() {
    return tipo;
  }

  public boolean fueDepositado(LocalDate fecha) {
    return getTipoDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }//duplicated code


  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean getTipoDeposito() {
    return tipo == tipoDeMov.DEPOSITO;
  }

  public boolean isExtraccion() {
    return tipo == tipoDeMov.EXTRACCION;
  } //duplicated code

  public double calcularValor(Cuenta cuenta) { //divergent change
    if (tipo == tipoDeMov.DEPOSITO) { //type test
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto(); //duplicated code
    }
  }
}
