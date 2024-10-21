package com.app_test.microservice1.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String>{
    Cliente findByClienteID(int id);
    int deleteByClienteID(int id);
}
