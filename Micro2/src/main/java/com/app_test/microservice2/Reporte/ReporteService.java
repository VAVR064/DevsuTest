package com.app_test.microservice2.Reporte;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app_test.microservice2.Cliente.Cliente;
import com.app_test.microservice2.Cuenta.Cuenta;
import com.app_test.microservice2.Cuenta.CuentaRepository;
import com.app_test.microservice2.Movimiento.Movimiento;
import com.app_test.microservice2.Movimiento.MovimientoRepository;
import com.app_test.microservice2.Util.ErrorMsg;

@Service
public class ReporteService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public ErrorMsg checkDateParams(String fInicio, String fFin) {
        String errM = "";

        LocalDate iDate = null;
        LocalDate fDate = null;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        dateFormat = dateFormat.withResolverStyle(ResolverStyle.STRICT);

        try {
            iDate = LocalDate.parse(fInicio, dateFormat);
        } catch (Exception exc) {
            String[] excM = exc.getMessage().split(":");
            if (excM.length > 1)
                errM += "Valor de día, mes o año incorrecto en fecha inicio. ";
            else
                errM += "Formato de fecha inicio debe ser 'aaaa-MM-dd'. ";
        }

        try {
            fDate = LocalDate.parse(fFin, dateFormat);
        } catch (Exception exc) {
            String[] excM = exc.getMessage().split(":");
            if (excM.length > 1)
                errM += "Valor de día, mes o año incorrecto en fecha fin. ";
            else
                errM += "Formato de fecha fin debe ser 'aaaa-MM-dd'. ";
        }

        if (iDate != null && fDate != null && iDate.isAfter(fDate))
            errM += "Fecha inicio debe ser antes o igual a fecha fin";

        if (errM.length() > 0)
            return new ErrorMsg(errM);
        else
            return null;

    }

    public List<ReporteDto> getReportByParams(String fInicio, String fFin, int idCliente) {

        List<Movimiento> lMovimientos = new ArrayList<Movimiento>();
        List<Cuenta> lCuentas = cuentaRepository.findByClienteId(new Cliente(idCliente));

        for (Cuenta cCuenta : lCuentas) {
            lMovimientos.addAll(movimientoRepository
                    .findByIdCuentaAndFechaGreaterThanEqualAndFechaLessThanEqualOrderByFecha(cCuenta, fInicio, fFin));
        }

        return ReporteMapper.LMovimientoToLReporte(lMovimientos);

    }

}
