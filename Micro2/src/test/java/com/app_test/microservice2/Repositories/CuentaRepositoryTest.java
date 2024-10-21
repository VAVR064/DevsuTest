package com.app_test.microservice2.Repositories;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app_test.microservice2.Cliente.Cliente;
import com.app_test.microservice2.Cuenta.Cuenta;
import com.app_test.microservice2.Cuenta.CuentaRepository;
import com.app_test.microservice2.Util.MockData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CuentaRepositoryTest {
    
    private CuentaRepository cuentaRepository;

    @Autowired
    public CuentaRepositoryTest(CuentaRepository cuentaRepository){
        this.cuentaRepository = cuentaRepository;
    }

    @Test
    public void SearchByID_ReturnsOneCuentaWithSameID(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);
        cuentaRepository.save(MockData.MockC3);

        Cuenta cE = cuentaRepository.findByIdCuenta(2);

        Assertions.assertEquals(cE, MockData.MockC2);
    }

    @Test
    public void SearchByID_NonExisting_ReturnsNull(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);
        cuentaRepository.save(MockData.MockC3);

        Cuenta cE = cuentaRepository.findByIdCuenta(4);

        Assertions.assertEquals(cE, null);
    }

    @Test
    public void DeleteByID_ReturnsNumberOne(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);
        cuentaRepository.save(MockData.MockC3);

        int cE = cuentaRepository.deleteByIdCuenta(2);

        Assertions.assertEquals(cE, 1);
    }

    @Test
    public void DeleteByID_NonExisting_ReturnsNumberZero(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);
        cuentaRepository.save(MockData.MockC3);

        int cE = cuentaRepository.deleteByIdCuenta(4);

        Assertions.assertEquals(cE, 0);
    }

    @Test
    public void SearchByClienteID_ReturnsListsOfCuentas(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);
        cuentaRepository.save(MockData.MockC3);

        List<Cuenta> cE = cuentaRepository.findByClienteId(null);

        Assertions.assertEquals(cE.size(), 3);
    }

    @Test
    public void SearchByClienteID_NonExisting_ReturnsEmptyListsOfCuentas(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);
        cuentaRepository.save(MockData.MockC3);

        List<Cuenta> cE = cuentaRepository.findByClienteId(new Cliente(1));

        Assertions.assertEquals(cE.size(), 0);
    }
    
}
