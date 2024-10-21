package com.app_test.microservice2.Movimiento;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovimientoDto {

    private int idMovimiento;

    private int idCuenta;

    private String fecha;

    private String tipo;

    private double valor;

    private double saldo;
}
