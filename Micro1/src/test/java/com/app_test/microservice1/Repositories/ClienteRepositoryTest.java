package com.app_test.microservice1.Repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app_test.microservice1.Cliente.Cliente;
import com.app_test.microservice1.Cliente.ClienteRepository;
import com.app_test.microservice1.Persona.PersonaRepository;
import com.app_test.microservice1.Util.MockData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClienteRepositoryTest {

    private PersonaRepository personaRepository;
    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteRepositoryTest(PersonaRepository personaRepository, ClienteRepository clienteRepository) {
        this.personaRepository = personaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Test
    public void SearchByID_ReturnsOneClienteWithSameID() {
        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        Cliente iC1 = clienteRepository.save(MockData.MockC1);
        clienteRepository.save(MockData.MockC2);
        clienteRepository.save(MockData.MockC3);

        Cliente cE = clienteRepository.findByClienteID(iC1.getClienteID());

        Assertions.assertEquals(cE, iC1);

    }

    @Test
    public void SearchByID_NonExisting_ReturnsNull() {
        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        clienteRepository.save(MockData.MockC1);
        Cliente iC2 = clienteRepository.save(MockData.MockC2);
        clienteRepository.save(MockData.MockC3);

        Cliente cE = clienteRepository.findByClienteID(iC2.getClienteID() + 2);

        Assertions.assertEquals(cE, null);
    }

    @Test
    public void DeleteByID_ReturnsNumberOne() {
        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        int iC1 = clienteRepository.save(MockData.MockC1).getClienteID();
        clienteRepository.save(MockData.MockC2);
        clienteRepository.save(MockData.MockC3);

        int cE = clienteRepository.deleteByClienteID(iC1);

        Assertions.assertEquals(cE, 1);
    }

    @Test
    public void DeleteByID_NonExisting_ReturnsZero() {
        personaRepository.save(MockData.MockP1);
        personaRepository.save(MockData.MockP2);
        personaRepository.save(MockData.MockP3);

        clienteRepository.save(MockData.MockC1);
        int iC2 = clienteRepository.save(MockData.MockC2).getClienteID();
        clienteRepository.save(MockData.MockC3);

        int cE = clienteRepository.deleteByClienteID(iC2 + 2);

        Assertions.assertEquals(cE, 0);
    }

}
