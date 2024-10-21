package com.app_test.microservice2.Util;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.app_test.microservice2.Cliente.Cliente;
import com.app_test.microservice2.Cuenta.Cuenta;
import com.app_test.microservice2.Movimiento.Movimiento;
import com.app_test.microservice2.Reporte.ReporteDto;

public final class MockData {

    private MockData() {
    }

    public static Cuenta MockC1 = new Cuenta(1, null, "ahorros", 500, true);
    public static Cuenta MockC2 = new Cuenta(2, null, "corriente", 500, true);
    public static Cuenta MockC3 = new Cuenta(3, null, "ahorros", 500, true);

    public static Cuenta MockCF1 = new Cuenta(1, new Cliente(1), "ahorros", 500, true);
    public static Cuenta MockCF2 = new Cuenta(2, new Cliente(1), "corriente", 500, true);
    public static Cuenta MockCF3 = new Cuenta(3, new Cliente(1), "ahorros", 500, true);

    public static List<Cuenta> MockLC = new ArrayList<Cuenta>(Arrays.asList(MockC1, MockC2, MockC3));

    public static Movimiento MockM1 = new Movimiento(1, MockCF1, "2024-10-19", "deposito", 10, 23.50);
    public static Movimiento MockM2 = new Movimiento(2, MockCF1, "2024-10-20", "retiro", 10, 33.50);
    public static Movimiento MockM3 = new Movimiento(3, MockCF2, "2024-10-18", "deposito", 10, 50);
    public static Movimiento MockM4 = new Movimiento(4, MockCF2, "2024-10-19", "deposito", 10.50, 60);
    public static Movimiento MockM5 = new Movimiento(5, MockCF2, "2024-10-20", "retiro", 19, 70.50);

    public static List<Movimiento> MockLM = new ArrayList<Movimiento>(Arrays.asList(MockM1, MockM2, MockM3, MockM4, MockM5));

    public static ErrorMsg MockRErr = new ErrorMsg("Valor de día, mes o año incorrecto en fecha inicio. Valor de día, mes o año incorrecto en fecha fin. ");

    public static ReporteDto MockR1 = new ReporteDto("2024-10-18", "prueba1", 1, "ahorros", 100, 10, 110, true);
    public static ReporteDto MockR2 = new ReporteDto("2024-10-19", "prueba1", 1, "ahorros", 110, -20, 90, true);
    public static ReporteDto MockR3 = new ReporteDto("2024-10-20", "prueba1", 1, "ahorros", 90, -30, 60, true);
    public static ReporteDto MockR4 = new ReporteDto("2024-10-21", "prueba1", 1, "ahorros", 60, 100, 160, true);
    public static ReporteDto MockR5 = new ReporteDto("2024-10-22", "prueba1", 1, "ahorros", 160, -10, 150, true);

    public static List<ReporteDto> MockLR = new ArrayList<ReporteDto>(Arrays.asList(MockR1, MockR2, MockR3, MockR4, MockR5));

}
