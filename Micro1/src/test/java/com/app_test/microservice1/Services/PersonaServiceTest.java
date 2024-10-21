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

import com.app_test.microservice1.Persona.Persona;
import com.app_test.microservice1.Persona.PersonaRepository;
import com.app_test.microservice1.Persona.PersonaService;
import com.app_test.microservice1.Util.ErrorMsg;
import com.app_test.microservice1.Util.MockData;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {
    
    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    @Test
    public void CheckValidPersona_ReturnsFullErrMessage(){

        ErrorMsg eMsg = personaService.checkValidPersona(MockData.MockPW);

        Assertions.assertEquals(eMsg, MockData.MockPErr);

    }

    @Test
    public void FindAllPersonas_ReturnsListOfPersonas(){
        
        when(personaRepository.findAll()).thenReturn(MockData.MockLP);

        List<Persona> dbPersona = personaService.findAllPersonas();

        Assertions.assertEquals(dbPersona.size(), MockData.MockLP.size());

    }

    @Test
    public void FindPersonaById_ReturnsOnePersonaWithSameID(){

        when(personaRepository.findByIdentificacion(Mockito.anyInt())).thenReturn(MockData.MockP1);

        Persona ePersona = personaService.findPersonaById(1);

        Assertions.assertEquals(ePersona, MockData.MockP1);

    }

    @Test
    public void AddPersona_ReturnsLastPersona(){

        when(personaRepository.save(Mockito.any(Persona.class))).thenReturn(MockData.MockP2);

        Persona ePersona = personaService.addPersona(MockData.MockP2);

        Assertions.assertEquals(ePersona, MockData.MockP2);
    }

    @Test
    public void UpdatePersona_ReturnsUpdatedPersona(){

        when(personaRepository.findByIdentificacion(Mockito.anyInt())).thenReturn(MockData.MockP3);
        when(personaRepository.save(Mockito.any(Persona.class))).thenReturn(MockData.MockP3);

        Persona ePersona = personaService.updatePersona(MockData.MockP3.getIdentificacion(), MockData.MockP2);

        Assertions.assertEquals(ePersona, MockData.MockP3);
    }
    
    @Test
    public void DeletePersonaById_ReturnsNumberOne(){

        when(personaRepository.deleteByIdentificacion(Mockito.anyInt())).thenReturn(1);

        int eValue = personaService.deletePersonaById(MockData.MockP2.getIdentificacion());

        Assertions.assertEquals(eValue, 1);

    } 

}
