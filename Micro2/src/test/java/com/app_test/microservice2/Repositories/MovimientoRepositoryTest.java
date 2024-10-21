package com.app_test.microservice2.Repositories;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app_test.microservice2.Cuenta.CuentaRepository;
import com.app_test.microservice2.Movimiento.Movimiento;
import com.app_test.microservice2.Movimiento.MovimientoRepository;
import com.app_test.microservice2.Util.MockData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MovimientoRepositoryTest {

    private CuentaRepository cuentaRepository;
    private MovimientoRepository movimientoRepository;

    @Autowired
    public MovimientoRepositoryTest(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository){
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Test
    public void SearchByID_ReturnsOneMovimientoWithSameID(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);

        movimientoRepository.save(MockData.MockM1);
        Movimiento iM2 = movimientoRepository.save(MockData.MockM2);
        movimientoRepository.save(MockData.MockM3);

        Movimiento mE = movimientoRepository.findByIdMovimiento(iM2.getIdMovimiento());

        Assertions.assertEquals(mE, iM2);
    }

    @Test
    public void SearchByID_NonExisting_ReturnsNull(){

        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);

        movimientoRepository.save(MockData.MockM1);
        Movimiento iM2 = movimientoRepository.save(MockData.MockM2);
        movimientoRepository.save(MockData.MockM3);

        Movimiento mE = movimientoRepository.findByIdMovimiento(iM2.getIdMovimiento() + 2);

        Assertions.assertEquals(mE, null);
    }
    
    @Test
    public void DeleteByID_ReturnsNumberOne(){
        
        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);

        movimientoRepository.save(MockData.MockM1);
        movimientoRepository.save(MockData.MockM2);
        int iM3 = movimientoRepository.save(MockData.MockM3).getIdMovimiento();

        int mE = movimientoRepository.deleteByIdMovimiento(iM3);

        Assertions.assertEquals(mE, 1);
    }

    @Test
    public void DeleteByID_NonExisting_ReturnsNumberZero(){
        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);

        movimientoRepository.save(MockData.MockM1);
        movimientoRepository.save(MockData.MockM2);
        int iM3 = movimientoRepository.save(MockData.MockM3).getIdMovimiento();

        int mE = movimientoRepository.deleteByIdMovimiento(iM3 + 1);

        Assertions.assertEquals(mE, 0);
    }

    @Test
    public void FindByCuentaAndFechas_ReturnsListOfMovimientos(){
        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);

        movimientoRepository.save(MockData.MockM1);
        movimientoRepository.save(MockData.MockM2);
        movimientoRepository.save(MockData.MockM3);
        movimientoRepository.save(MockData.MockM4);
        movimientoRepository.save(MockData.MockM5);

        List<Movimiento> mE = movimientoRepository.findByIdCuentaAndFechaGreaterThanEqualAndFechaLessThanEqualOrderByFecha(MockData.MockC2,"2024-10-19","2024-10-20");

        Assertions.assertEquals(mE.size(), 2);
    }

    @Test
    public void FindByCuentaAndFechas_NonExisting_ReturnsEmptyListOfMovimientos(){
        cuentaRepository.save(MockData.MockC1);
        cuentaRepository.save(MockData.MockC2);

        movimientoRepository.save(MockData.MockM1);
        movimientoRepository.save(MockData.MockM2);
        movimientoRepository.save(MockData.MockM3);
        movimientoRepository.save(MockData.MockM4);
        movimientoRepository.save(MockData.MockM5);

        List<Movimiento> mE = movimientoRepository.findByIdCuentaAndFechaGreaterThanEqualAndFechaLessThanEqualOrderByFecha(MockData.MockC1,"2024-10-21","2024-10-31");

        Assertions.assertEquals(mE.size(), 0);
    }
}
