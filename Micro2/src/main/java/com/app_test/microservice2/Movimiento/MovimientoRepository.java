package com.app_test.microservice2.Movimiento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app_test.microservice2.Cuenta.Cuenta;

public interface MovimientoRepository extends JpaRepository<Movimiento, String> {
    Movimiento findByIdMovimiento(int id);
    int deleteByIdMovimiento(int id);
    List<Movimiento> findByIdCuentaAndFechaGreaterThanEqualAndFechaLessThanEqualOrderByFecha(Cuenta idCuenta, String iFecha, String fFecha);
}
