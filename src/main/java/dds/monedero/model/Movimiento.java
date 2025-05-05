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

  public double calcularAgregado() {
    return operarSegunTipo() * getMonto();
  }

  public boolean fueDepositado(LocalDate fecha) {
    return tipoYFecha(tipoDeMov.DEPOSITO, fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return tipoYFecha(tipoDeMov.EXTRACCION, fecha);
  }

  private boolean tipoYFecha(tipoDeMov tipo, LocalDate fecha) {
    return verificarTipo(tipo) && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean verificarTipo(tipoDeMov tipo) {
    return this.tipo.equals(tipo);
  }

  private double operarSegunTipo() {
    return tipo.factor();
  }
}
