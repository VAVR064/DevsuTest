package com.app_test.microservice2.Controllers;

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

import com.app_test.microservice2.Cuenta.CuentaController;
import com.app_test.microservice2.Cuenta.CuentaDto;
import com.app_test.microservice2.Cuenta.CuentaMapper;
import com.app_test.microservice2.Cuenta.CuentaService;
import com.app_test.microservice2.Util.ErrorMsg;
import com.app_test.microservice2.Util.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuentaController.class)
public class CuentaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CuentaService cuentaService;

    @Test
    public void GetAllCuentas_ReturnsListOfCuentas () throws Exception{

        when(cuentaService.findAllCuentas()).thenReturn(MockData.MockLC);

        MvcResult response = this.mockMvc.perform(get("/api/cuentas")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockLC));
    }

    @Test
    public void GetCuentaByID_ReturnsOneCuentaWithSameID () throws Exception{

        int testID = 1;
        when(cuentaService.findCuentaById(anyInt())).thenReturn(MockData.MockC1);

        MvcResult response = this.mockMvc.perform(get("/api/cuentas/"+testID)).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockC1));
    }

    @Test
    public void GetCuentaByID_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cuenta con id "+testID+" no existe");

        when(cuentaService.findCuentaById(anyInt())).thenReturn(null);

        MvcResult response = this.mockMvc.perform(get("/api/cuentas/"+testID)).andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void CreateCuenta_ReturnsCreatedCuenta () throws Exception{

        when(cuentaService.checkValidCuenta(Mockito.any(CuentaDto.class))).thenReturn(null);
        when(cuentaService.addCuenta(Mockito.any(CuentaDto.class))).thenReturn(MockData.MockCF2);

        MvcResult response = this.mockMvc.perform(post("/api/cuentas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(CuentaMapper.CuentaToCuentaDto(MockData.MockCF2))))
        .andDo(print()).andExpect(status().isCreated()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(CuentaMapper.CuentaToCuentaDto(MockData.MockCF2)));
    }

    @Test
    public void CreateCuenta_AlreadyExists_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cuenta con id "+testID+" ya existe");

        when(cuentaService.checkValidCuenta(Mockito.any(CuentaDto.class))).thenReturn(errorMsg);
        
        MvcResult response = this.mockMvc.perform(post("/api/cuentas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(CuentaMapper.CuentaToCuentaDto(MockData.MockCF1))))
        .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }
 
    @Test
    public void EditCuenta_ReturnsEditedCuenta () throws Exception{

        int testID = 3;
        when(cuentaService.updateCuenta(anyInt(), Mockito.any(CuentaDto.class))).thenReturn(MockData.MockCF3);

        MvcResult response = this.mockMvc.perform(patch("/api/cuentas/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(CuentaMapper.CuentaToCuentaDto(MockData.MockCF3))))
        .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(CuentaMapper.CuentaToCuentaDto(MockData.MockCF3)));
    }

    @Test
    public void EditCuenta_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cuenta con id "+testID+" no existe");

        when(cuentaService.updateCuenta(anyInt(), Mockito.any(CuentaDto.class))).thenReturn(null);

        MvcResult response = this.mockMvc.perform(patch("/api/cuentas/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(CuentaMapper.CuentaToCuentaDto(MockData.MockCF3))))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void RemoveCuenta_ReturnsCorrectStatusCode () throws Exception{

        int testID = 1;
        when(cuentaService.deleteCuentaById(anyInt())).thenReturn(1);

        MvcResult response = this.mockMvc.perform(delete("/api/cuentas/"+testID))
        .andDo(print()).andExpect(status().isNoContent()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), "");
    }

    @Test
    public void RemoveCuenta_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Cuenta con id "+testID+" no existe");

        when(cuentaService.deleteCuentaById(anyInt())).thenReturn(0);

        MvcResult response = this.mockMvc.perform(delete("/api/cuentas/"+testID))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

}
