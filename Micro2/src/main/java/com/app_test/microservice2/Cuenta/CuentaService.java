package com.app_test.microservice2.Cuenta;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.app_test.microservice2.Cliente.Cliente;
import com.app_test.microservice2.Cliente.ClienteServices;
import com.app_test.microservice2.Util.ErrorMsg;

import jakarta.transaction.Transactional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public ErrorMsg checkValidCuenta(CuentaDto nCuenta) {
        String errM = "";

        try {
            ClienteServices.getClienteByIDFromMicro1(nCuenta.getClienteId());
        } catch (ResourceAccessException raExc) {
            errM += "No se puede consultar el cliente, intente más tarde. ";
        } catch (HttpClientErrorException httpExc) {
            if (httpExc.getStatusCode().equals(HttpStatusCode.valueOf(404)))
                errM += "Cliente con id " + nCuenta.getClienteId() + " no existe. ";
            else
                errM += "No se puede consultar el cliente, intente más tarde. ";
        } catch (Exception exc) {
            return new ErrorMsg("Error: " + exc.getMessage());
        }

        if (nCuenta.getTipo() == null)
            errM += "Tipo no debe estar vacío. ";
        else if (!nCuenta.getTipo().equals("ahorros") && !nCuenta.getTipo().equals("corriente"))
            errM += "Tipo debe ser 'ahorros' o 'corriente'. ";

        if (nCuenta.getSaldo() < 0)
            errM += "Saldo no debe ser menor a cero. ";

        if (nCuenta.getEstado() == null)
            errM += "Estado no debe estar vacío. ";
        else if (!nCuenta.getEstado().equals("true") && !nCuenta.getEstado().equals("false"))
            errM += "Estado debe ser 'true' o 'false'. ";

        if (errM.length() > 0)
            return new ErrorMsg(errM);
        else
            return null;

    }

    public List<Cuenta> findAllCuentas() {
        return cuentaRepository.findAll();
    }

    public Cuenta findCuentaById(int id) {
        return cuentaRepository.findByIdCuenta(id);
    }

    public Cuenta addCuenta(CuentaDto nCuenta) {
        nCuenta.setSaldo(Math.floor(nCuenta.getSaldo() * 100) / 100);
        return cuentaRepository.save(CuentaMapper.CuentaDtoToCuenta(nCuenta));
    }

    public Cuenta updateCuenta(int id, CuentaDto eCuenta) {

        Cuenta uCuenta = cuentaRepository.findByIdCuenta(id);
        if (uCuenta == null)
            return null;

        try {
            ClienteServices.getClienteByIDFromMicro1(eCuenta.getClienteId());
            uCuenta.setClienteId(new Cliente(eCuenta.getClienteId()));
        } catch (Exception exc) {
            //pass
        }

        if (eCuenta.getTipo() != null &&
                (eCuenta.getTipo().equals("ahorros") || eCuenta.getTipo().equals("corriente")))
            uCuenta.setTipo(eCuenta.getTipo());

        if (eCuenta.getEstado() != null &&
                (eCuenta.getEstado().equals("true") || eCuenta.getEstado().equals("false")))
            uCuenta.setEstado(eCuenta.getEstado().equals("true"));

        if (eCuenta.getSaldo() >= 0)
            uCuenta.setSaldo(Math.floor(eCuenta.getSaldo() * 100) / 100);

        return cuentaRepository.save(uCuenta);

    }

    @Transactional
    public int deleteCuentaById(int id) {
        return cuentaRepository.deleteByIdCuenta(id);
    }
}
