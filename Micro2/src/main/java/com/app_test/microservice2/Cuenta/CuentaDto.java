package com.app_test.microservice2.Cuenta;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CuentaDto {

    private int idCuenta;

    private int clienteId;

    private String tipo;

    private double saldo;

    private String estado;
}
