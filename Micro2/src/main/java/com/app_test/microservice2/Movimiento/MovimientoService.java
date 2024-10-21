package com.app_test.microservice2.Movimiento;

import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app_test.microservice2.Cuenta.Cuenta;
import com.app_test.microservice2.Cuenta.CuentaMapper;
import com.app_test.microservice2.Cuenta.CuentaRepository;
import com.app_test.microservice2.Cuenta.CuentaService;
import com.app_test.microservice2.Util.ErrorMsg;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private CuentaService cuentaService;

    private boolean hasEnoughFunds(int idCuenta, double valor) {
        double cSaldo = cuentaRepository.findByIdCuenta(idCuenta).getSaldo();
        return valor <= cSaldo;
    }

    private void updatedFunds(Cuenta uCuenta, double valor, boolean isDeposito) {

        if (isDeposito)
            uCuenta.setSaldo(uCuenta.getSaldo() + valor);
        else {
            if (hasEnoughFunds(uCuenta.getIdCuenta(), valor))
                uCuenta.setSaldo(uCuenta.getSaldo() - valor);
            else
                throw new ArithmeticException("No se pudo actualizar el saldo");
        }

        cuentaService.updateCuenta(uCuenta.getIdCuenta(), CuentaMapper.CuentaToCuentaDto(uCuenta));

    }

    public ErrorMsg checkValidMovimientoJSON(MovimientoDto nMovimiento) {
        String errM = "";

        if (cuentaRepository.findByIdCuenta(nMovimiento.getIdCuenta()) == null)
            errM += "Cuenta con id " + nMovimiento.getIdCuenta() + " no existe. ";

        if (nMovimiento.getTipo() == null)
            errM += "Tipo no debe estar vacío. ";
        else if (!nMovimiento.getTipo().equals("deposito") && !nMovimiento.getTipo().equals("retiro"))
            errM += "Tipo debe ser 'deposito' o 'retiro'. ";

        if (nMovimiento.getValor() <= 0)
            errM += "Valor debe ser mayor a cero. ";

        if (errM.length() > 0)
            return new ErrorMsg(errM);
        else {
            double rValor = Math.floor(nMovimiento.getValor() * 100) / 100;
            ;

            if (nMovimiento.getTipo().equals("retiro") && !hasEnoughFunds(nMovimiento.getIdCuenta(), rValor))
                return new ErrorMsg("Cuenta con id " + nMovimiento.getIdCuenta() + " no tiene suficientes fondos. ");
            else
                return null;
        }

    }

    public List<Movimiento> findAllMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento findMovimientoById(int id) {
        return movimientoRepository.findByIdMovimiento(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Movimiento addMovimiento(MovimientoDto nMovimiento) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        double rValor = Math.floor(nMovimiento.getValor() * 100) / 100;

        Cuenta uCuenta = cuentaRepository.findByIdCuenta(nMovimiento.getIdCuenta());
        nMovimiento.setSaldo(uCuenta.getSaldo());

        // Update Cuenta
        updatedFunds(uCuenta, rValor, nMovimiento.getTipo().equals("deposito"));

        // Save Movimiento
        nMovimiento.setFecha(dateFormat.format(new Date()));
        nMovimiento.setValor(rValor);

        return movimientoRepository.save(MovimientoMapper.MovimientoDtoToMovimiento(nMovimiento));
    }

    @Transactional(rollbackFor = Exception.class)
    public Movimiento updateMovimiento(int id, MovimientoDto eMovimiento) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        double eValor = Math.floor(eMovimiento.getValor() * 100) / 100;

        Movimiento uMovimiento = movimientoRepository.findByIdMovimiento(id);
        if (uMovimiento == null)
            return null;

        Cuenta uCuenta = cuentaRepository.findByIdCuenta(uMovimiento.getIdCuenta().getIdCuenta()); // Antigua cuenta
        Cuenta eCuenta = cuentaRepository.findByIdCuenta(eMovimiento.getIdCuenta()); // Actual cuenta

        // Misma cuenta
        if (eCuenta == null || uCuenta.getIdCuenta() == eCuenta.getIdCuenta()) {
            // Mismo tipo de movimiento
            if (eMovimiento.getTipo().equals(null) || uMovimiento.getTipo().equals(eMovimiento.getTipo())) {
                // Diferente valor de movimiento
                if (uMovimiento.getValor() != eValor) {
                    updatedFunds(uCuenta, uMovimiento.getValor() - eValor,
                            !uMovimiento.getTipo().equals("deposito"));
                }
            } else {// Diferente tipo de movimiento
                updatedFunds(uCuenta, uMovimiento.getValor(), !uMovimiento.getTipo().equals("deposito"));
                updatedFunds(uCuenta, eMovimiento.getValor(), eMovimiento.getTipo().equals("deposito"));
            }
        } else { // Diferente cuenta
            updatedFunds(uCuenta, uMovimiento.getValor(), !uMovimiento.getTipo().equals("deposito"));
            uMovimiento.setIdCuenta(eCuenta);
            uMovimiento.setSaldo(eCuenta.getSaldo());
            updatedFunds(eCuenta, eMovimiento.getValor(), eMovimiento.getTipo().equals("deposito"));
        }

        uMovimiento.setTipo(eMovimiento.getTipo());
        uMovimiento.setValor(eValor);
        uMovimiento.setFecha(dateFormat.format(new Date()));

        return movimientoRepository.save(uMovimiento);

    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteMovimientoById(int id) {

        Movimiento cMovimiento = findMovimientoById(id);

        if (cMovimiento == null)
            return 0;

        updatedFunds(cMovimiento.getIdCuenta(), cMovimiento.getValor(), !cMovimiento.getTipo().equals("deposito"));

        return movimientoRepository.deleteByIdMovimiento(id);
    }

}
