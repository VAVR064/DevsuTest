package com.app_test.microservice1.Services;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app_test.microservice1.Cliente.Cliente;
import com.app_test.microservice1.Cliente.ClienteMapper;
import com.app_test.microservice1.Cliente.ClienteRepository;
import com.app_test.microservice1.Cliente.ClienteService;
import com.app_test.microservice1.Persona.PersonaRepository;
import com.app_test.microservice1.Util.ErrorMsg;
import com.app_test.microservice1.Util.MockData;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private ClienteService clienteService;
    
    @Test
    public void CheckValidCliente_ReturnsFullErrMessage(){
        ErrorMsg eMsg = clienteService.checkValidCliente(ClienteMapper.ClienteToClienteDto(MockData.MockCW));

        Assertions.assertEquals(eMsg, MockData.MockCErr);
    }

    @Test
    public void FindAllClientes_ReturnsListOfClientes(){
        
        when(clienteRepository.findAll()).thenReturn(MockData.MockLC);

        List<Cliente> dbCliente = clienteService.findAllClientes();

        Assertions.assertEquals(dbCliente.size(), MockData.MockLC.size());

    }

    @Test
    public void FindClienteById_ReturnsOneClienteWithSameID(){

        when(clienteRepository.findByClienteID(Mockito.anyInt())).thenReturn(MockData.MockC1);

        Cliente eCliente = clienteService.findClienteById(1);

        Assertions.assertEquals(eCliente, MockData.MockC1);

    }

    @Test
    public void AddCliente_ReturnsLastCliente(){

        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(MockData.MockC2);

        Cliente eCliente = clienteService.addCliente(ClienteMapper.ClienteToClienteDto(MockData.MockC2));

        Assertions.assertEquals(eCliente, MockData.MockC2);
    }

    @Test
    public void UpdateCliente_ReturnsUpdatedCliente(){

        when(personaRepository.findByIdentificacion(Mockito.anyInt())).thenReturn(MockData.MockP3);
        when(clienteRepository.findByClienteID(Mockito.anyInt())).thenReturn(MockData.MockC3);
        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(MockData.MockC3);

        Cliente eCliente = clienteService.updateCliente(MockData.MockC3.getClienteID(), ClienteMapper.ClienteToClienteDto(MockData.MockC2));

        Assertions.assertEquals(eCliente, MockData.MockC3);
    }
    
    @Test
    public void DeleteClienteById_ReturnsNumberOne(){

        when(clienteRepository.deleteByClienteID(Mockito.anyInt())).thenReturn(1);

        int eValue = clienteService.deleteClienteById(MockData.MockC2.getClienteID());

        Assertions.assertEquals(eValue, 1);

    } 
}
