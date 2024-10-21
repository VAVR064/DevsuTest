package com.app_test.microservice1.Controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.app_test.microservice1.Persona.Persona;
import com.app_test.microservice1.Persona.PersonaController;
import com.app_test.microservice1.Persona.PersonaService;
import com.app_test.microservice1.Util.ErrorMsg;
import com.app_test.microservice1.Util.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonaController.class)
public class PersonaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonaService personaService;

    @Test
    public void GetAllPersonas_ReturnsListOfPersonas () throws Exception{

        when(personaService.findAllPersonas()).thenReturn(MockData.MockLP);

        MvcResult response = this.mockMvc.perform(get("/api/personas")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockLP));
    }

    @Test
    public void GetPersonaByID_ReturnsOnePersonaWithSameID () throws Exception{

        int testID = 1;
        when(personaService.findPersonaById(anyInt())).thenReturn(MockData.MockP1);

        MvcResult response = this.mockMvc.perform(get("/api/personas/"+testID)).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockP1));
    }

    @Test
    public void GetPersonaByID_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Persona con id "+testID+" no existe");

        when(personaService.findPersonaById(anyInt())).thenReturn(null);

        MvcResult response = this.mockMvc.perform(get("/api/personas/"+testID)).andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void CreatePersona_ReturnsCreatedPersona () throws Exception{

        when(personaService.checkValidPersona(Mockito.any(Persona.class))).thenReturn(null);
        when(personaService.addPersona(Mockito.any(Persona.class))).thenReturn(MockData.MockP2);

        MvcResult response = this.mockMvc.perform(post("/api/personas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MockData.MockP2)))
        .andDo(print()).andExpect(status().isCreated()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockP2));
    }

    @Test
    public void CreatePersona_AlreadyExists_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Persona con id "+testID+" ya existe");

        when(personaService.checkValidPersona(Mockito.any(Persona.class))).thenReturn(errorMsg);
        
        MvcResult response = this.mockMvc.perform(post("/api/personas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MockData.MockP1)))
        .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void EditPersona_ReturnsEditedPersona () throws Exception{

        int testID = 3;
        when(personaService.updatePersona(anyInt(), Mockito.any(Persona.class))).thenReturn(MockData.MockP3);

        MvcResult response = this.mockMvc.perform(patch("/api/personas/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MockData.MockP3)))
        .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockP3));
    }

    @Test
    public void EditPersona_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Persona con id "+testID+" no existe");

        when(personaService.updatePersona(anyInt(), Mockito.any(Persona.class))).thenReturn(null);

        MvcResult response = this.mockMvc.perform(patch("/api/personas/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MockData.MockP3)))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void RemovePersona_ReturnsCorrectStatusCode () throws Exception{

        int testID = 1;
        when(personaService.deletePersonaById(anyInt())).thenReturn(1);

        MvcResult response = this.mockMvc.perform(delete("/api/personas/"+testID))
        .andDo(print()).andExpect(status().isNoContent()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), "");
    }

    @Test
    public void RemovePersona_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Persona con id "+testID+" no existe");

        when(personaService.deletePersonaById(anyInt())).thenReturn(0);

        MvcResult response = this.mockMvc.perform(delete("/api/personas/"+testID))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

}
