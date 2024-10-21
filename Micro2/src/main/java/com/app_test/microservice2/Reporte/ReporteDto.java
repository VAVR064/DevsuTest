package com.app_test.microservice2.Reporte;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteDto {
    
    private String fecha;

    private String nameCliente;

    private int idCuenta;

    private String tipoCuenta;

    private double pSaldo;

    private double cMovimiento;

    private double nSaldo;

    private boolean estado;

}
