package com.app_test.microservice1.Repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app_test.microservice1.Persona.Persona;
import com.app_test.microservice1.Persona.PersonaRepository;
import com.app_test.microservice1.Util.MockData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PersonaRepositoryTest {

    private PersonaRepository personaRepository;

    @Autowired
    public PersonaRepositoryTest(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Test
    public void SearchByID_ReturnsOnePersonaWithSameID() {

        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        Persona pE = personaRepository.findByIdentificacion(3);

        Assertions.assertEquals(pE, MockData.MockP3);

    }

    @Test
    public void SearchByID_NonExisting_ReturnsNull() {

        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        Persona pE = personaRepository.findByIdentificacion(5);

        Assertions.assertEquals(pE, null);

    }

    @Test
    public void DeleteByID_ReturnsNumberOne(){

        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        int pE = personaRepository.deleteByIdentificacion(1);

        Assertions.assertEquals(pE, 1);
    }

    @Test
    public void DeleteByID_NonExisting_ReturnsZero(){

        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        int pE = personaRepository.deleteByIdentificacion(5);

        Assertions.assertEquals(pE, 0);
    }

}
