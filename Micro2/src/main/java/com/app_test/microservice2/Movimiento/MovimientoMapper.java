package com.app_test.microservice2.Movimiento;

import com.app_test.microservice2.Cuenta.Cuenta;

public final class MovimientoMapper {

    private MovimientoMapper() {
    }

    public static Movimiento MovimientoDtoToMovimiento(MovimientoDto movimientoDto) {
        if (movimientoDto == null)
            return null;

        return new Movimiento(
                movimientoDto.getIdMovimiento(),
                new Cuenta(movimientoDto.getIdCuenta()),
                movimientoDto.getFecha(),
                movimientoDto.getTipo(),
                movimientoDto.getValor(),
                movimientoDto.getSaldo());
    }

    public static MovimientoDto MovimientoToMovimientoDto(Movimiento movimiento) {
        if (movimiento == null)
            return null;

        return new MovimientoDto(
                movimiento.getIdMovimiento(),
                movimiento.getIdCuenta().getIdCuenta(),
                movimiento.getFecha(),
                movimiento.getTipo(),
                movimiento.getValor(),
                movimiento.getSaldo());
    }
}
