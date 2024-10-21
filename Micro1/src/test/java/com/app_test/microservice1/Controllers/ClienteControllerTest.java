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

import com.app_test.microservice1.Cliente.ClienteController;
import com.app_test.microservice1.Cliente.ClienteDto;
import com.app_test.microservice1.Cliente.ClienteMapper;
import com.app_test.microservice1.Cliente.ClienteService;
import com.app_test.microservice1.Util.ErrorMsg;
import com.app_test.microservice1.Util.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    public void GetAllClientes_ReturnsListOfClientes () throws Exception{

        when(clienteService.findAllClientes()).thenReturn(MockData.MockLC);

        MvcResult response = this.mockMvc.perform(get("/api/clientes")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockLC));
    }

    @Test
    public void GetClienteByID_ReturnsOneClienteWithSameID () throws Exception{

        int testID = 1;
        when(clienteService.findClienteById(anyInt())).thenReturn(MockData.MockC1);

        MvcResult response = this.mockMvc.perform(get("/api/clientes/"+testID)).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockC1));
    }

    @Test
    public void GetClienteByID_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cliente con id "+testID+" no existe");

        when(clienteService.findClienteById(anyInt())).thenReturn(null);

        MvcResult response = this.mockMvc.perform(get("/api/clientes/"+testID)).andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void CreateCliente_ReturnsCreatedCliente () throws Exception{

        when(clienteService.checkValidCliente(Mockito.any(ClienteDto.class))).thenReturn(null);
        when(clienteService.addCliente(Mockito.any(ClienteDto.class))).thenReturn(MockData.MockC2);

        MvcResult response = this.mockMvc.perform(post("/api/clientes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ClienteMapper.ClienteToClienteDto(MockData.MockC2))))
        .andDo(print()).andExpect(status().isCreated()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString((ClienteMapper.ClienteToClienteDto(MockData.MockC2))));
    }

    @Test
    public void CreateCliente_AlreadyExists_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cliente con id "+testID+" ya existe");

        when(clienteService.checkValidCliente(Mockito.any(ClienteDto.class))).thenReturn(errorMsg);
        
        MvcResult response = this.mockMvc.perform(post("/api/clientes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ClienteMapper.ClienteToClienteDto(MockData.MockC1))))
        .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void EditCliente_ReturnsEditedCliente () throws Exception{

        int testID = 3;
        when(clienteService.updateCliente(anyInt(), Mockito.any(ClienteDto.class))).thenReturn(MockData.MockC3);

        MvcResult response = this.mockMvc.perform(patch("/api/clientes/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ClienteMapper.ClienteToClienteDto(MockData.MockC3))))
        .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString((ClienteMapper.ClienteToClienteDto(MockData.MockC3))));
    }

    @Test
    public void EditCliente_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cliente con id "+testID+" no existe");

        when(clienteService.updateCliente(anyInt(), Mockito.any(ClienteDto.class))).thenReturn(null);

        MvcResult response = this.mockMvc.perform(patch("/api/clientes/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ClienteMapper.ClienteToClienteDto(MockData.MockC3))))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void RemoveCliente_ReturnsCorrectStatusCode () throws Exception{

        int testID = 1;
        when(clienteService.deleteClienteById(anyInt())).thenReturn(1);

        MvcResult response = this.mockMvc.perform(delete("/api/clientes/"+testID))
        .andDo(print()).andExpect(status().isNoContent()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), "");
    }

    @Test
    public void RemoveCliente_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cliente con id "+testID+" no existe");

        when(clienteService.deleteClienteById(anyInt())).thenReturn(0);

        MvcResult response = this.mockMvc.perform(delete("/api/clientes/"+testID))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

}