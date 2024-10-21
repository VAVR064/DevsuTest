package com.app_test.microservice2.Reporte;

import java.util.List;
import java.util.ArrayList;
import com.app_test.microservice2.Movimiento.Movimiento;

public final class ReporteMapper {

    private ReporteMapper() {
    }

    public static ReporteDto MovimientoToReporte(Movimiento movimiento) {
        if (movimiento == null)
            return null;

        return new ReporteDto(
                movimiento.getFecha(),
                movimiento.getIdCuenta().getClienteId().getPersonaID().getNombre(),
                movimiento.getIdCuenta().getIdCuenta(),
                movimiento.getIdCuenta().getTipo(),
                movimiento.getSaldo(),
                movimiento.getTipo().equals("deposito") ? movimiento.getValor() : movimiento.getValor() * -1,
                movimiento.getIdCuenta().getSaldo(),
                movimiento.getIdCuenta().isEstado());
    }

    public static List<ReporteDto> LMovimientoToLReporte(List<Movimiento> lmovimiento) {

        List<ReporteDto> uLReporteDto = new ArrayList<ReporteDto>();

        if (lmovimiento != null && !lmovimiento.isEmpty()) {
            for (Movimiento cMovimiento : lmovimiento) {
                uLReporteDto.add(MovimientoToReporte(cMovimiento));
            }
        }
        return uLReporteDto;

    }
}
