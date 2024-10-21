package com.app_test.microservice2.Cuenta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app_test.microservice2.Cliente.Cliente;

public interface CuentaRepository extends JpaRepository<Cuenta, String>{
    Cuenta findByIdCuenta(int id);
    int deleteByIdCuenta(int id);
    List<Cuenta> findByClienteId(Cliente id);
}
