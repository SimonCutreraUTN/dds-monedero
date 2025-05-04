package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  @DisplayName("Se puede poner saldo al inicializar la cuenta")
  void CuentaConSaldoInicial() {
    Cuenta cuentaSaldoInicial = new Cuenta(100);
    assertEquals(100, cuentaSaldoInicial.getSaldo());
  }

  @Test
  @DisplayName("Es posible poner $1500 en una cuenta vacía")
  void Poner() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  @DisplayName("Es posible extraer de una cuenta no vacía")
  void Sacar() {
    cuenta.poner(1500);
    cuenta.sacar(1000);
    assertEquals(500, cuenta.getSaldo());
  }

  @Test
  @DisplayName("Se estima correctamente el monto extraído de un día")
  void MontoDeUnDia() {
    cuenta.poner(1500);
    cuenta.sacar(1000);
    assertEquals(1000, cuenta.getMontoExtraidoA(LocalDate.now()));
  }

  @Test
  @DisplayName("No es posible poner montos negativos")
  void NoSePuedePonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  @DisplayName("Es posible realizar múltiples depósitos consecutivos")
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(3, cuenta.getMovimientos().size());
  }

  @Test
  @DisplayName("No es posible superar la máxima cantidad de depositos diarios")
  void MaximoTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
  }

  @Test
  @DisplayName("No es posible extraer más que el saldo disponible")
  void NoSePuedeExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.setSaldo(90);
      cuenta.sacar(91);
    });
  }

  @Test
  @DisplayName("No es posible extraer más que el límite diario")
  void NoSePuedeExtraerMasDeLimite() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  @DisplayName("No es posible extraer un monto negativo")
  void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  @DisplayName("Se estima bien la fecha")
  void EstimaBienLaFecha() {
    LocalDate fecha = LocalDate.of(2025, 1, 1);
    Movimiento movimiento = new Movimiento(fecha, 1000, true);
    assertTrue(movimiento.esDeLaFecha(fecha));
  }

  @Test
  @DisplayName("Se puede agregar un movimiento a la cuenta")
  void agregarMovimiento() {
    LocalDate fecha = LocalDate.of(2025, 1, 1);
    Movimiento movimiento = new Movimiento(fecha, 1000, true);
    movimiento.agregateA(cuenta);
    assertEquals(1, cuenta.getMovimientos().size());
  }

  @Test
  @DisplayName("Se calcula correctamente el saldo")
  void calculaCorrectamenteSaldo() {
    cuenta.poner(1500);
    LocalDate fecha = LocalDate.of(2025, 1, 1);
    Movimiento movimiento = new Movimiento(fecha, 1000, true);
    assertEquals(2500, movimiento.calcularValor(cuenta));
  }

  @Test
  @DisplayName("Se diferencia entre depósito y extracción")
  void DiferenciaDepositoYExtraccion(){
    cuenta.poner(1500);
    cuenta.sacar(1000);
    Movimiento movimientoDeposito = cuenta.getMovimientos().get(0);
    Movimiento movimientoExtraccion = cuenta.getMovimientos().get(1);
    assertTrue(movimientoDeposito.getDeposito());
    assertTrue(movimientoExtraccion.isExtraccion());
  }

  @Test
  @DisplayName("Se estima la fecha de depósito y extracción")
  void FechaDeDeposito() {
    cuenta.poner(1500);
    cuenta.sacar(1000);
    Movimiento movimientoDeposito = cuenta.getMovimientos().get(0);
    Movimiento movimientoExtraccion = cuenta.getMovimientos().get(1);
    assertTrue(movimientoDeposito.fueDepositado(LocalDate.now()));
    assertTrue(movimientoExtraccion.fueExtraido(LocalDate.now()));
  }

}