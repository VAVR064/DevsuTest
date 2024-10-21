package com.app_test.microservice1.Persona;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, String>{
    Persona findByIdentificacion(int id);
    int deleteByIdentificacion(int id);
}
