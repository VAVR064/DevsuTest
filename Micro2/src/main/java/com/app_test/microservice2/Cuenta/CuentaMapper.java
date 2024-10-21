package com.app_test.microservice2.Cuenta;

import com.app_test.microservice2.Cliente.Cliente;

public final class CuentaMapper {

    private CuentaMapper() {
    }

    public static Cuenta CuentaDtoToCuenta(CuentaDto cuentaDto) {
        if (cuentaDto == null)
            return null;

        return new Cuenta(
                cuentaDto.getIdCuenta(),
                new Cliente(cuentaDto.getClienteId()),
                cuentaDto.getTipo(),
                cuentaDto.getSaldo(),
                cuentaDto.getEstado().equals("true"));
    }

    public static CuentaDto CuentaToCuentaDto(Cuenta cuenta) {
        if (cuenta == null)
            return null;

        return new CuentaDto(
                cuenta.getIdCuenta(),
                cuenta.getClienteId().getClienteID(),
                cuenta.getTipo(),
                cuenta.getSaldo(),
                cuenta.isEstado() ? "true" : "false");
    }
}
