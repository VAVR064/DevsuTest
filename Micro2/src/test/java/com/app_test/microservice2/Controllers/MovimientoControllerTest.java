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

import com.app_test.microservice2.Movimiento.MovimientoController;
import com.app_test.microservice2.Movimiento.MovimientoDto;
import com.app_test.microservice2.Movimiento.MovimientoMapper;
import com.app_test.microservice2.Movimiento.MovimientoService;
import com.app_test.microservice2.Util.ErrorMsg;
import com.app_test.microservice2.Util.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MovimientoController.class)
public class MovimientoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovimientoService movimientoService;

    @Test
    public void GetAllMovimientos_ReturnsListOfMovimientos () throws Exception{

        when(movimientoService.findAllMovimientos()).thenReturn(MockData.MockLM);

        MvcResult response = this.mockMvc.perform(get("/api/movimientos")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockLM));
    }

    @Test
    public void GetMovimientoByID_ReturnsOneMovimientoWithSameID () throws Exception{

        int testID = 1;
        when(movimientoService.findMovimientoById(anyInt())).thenReturn(MockData.MockM1);

        MvcResult response = this.mockMvc.perform(get("/api/movimientos/"+testID)).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockM1));
    }

    @Test
    public void GetMovimientoByID_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Movimiento con id "+testID+" no existe");

        when(movimientoService.findMovimientoById(anyInt())).thenReturn(null);

        MvcResult response = this.mockMvc.perform(get("/api/movimientos/"+testID)).andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void CreateMovimiento_ReturnsCreatedMovimiento () throws Exception{

        when(movimientoService.checkValidMovimientoJSON(Mockito.any(MovimientoDto.class))).thenReturn(null);
        when(movimientoService.addMovimiento(Mockito.any(MovimientoDto.class))).thenReturn(MockData.MockM2);

        MvcResult response = this.mockMvc.perform(post("/api/movimientos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM2))))
        .andDo(print()).andExpect(status().isCreated()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM2)));
    }

    @Test
    public void CreateMovimiento_AlreadyExists_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Movimiento con id "+testID+" ya existe");

        when(movimientoService.checkValidMovimientoJSON(Mockito.any(MovimientoDto.class))).thenReturn(errorMsg);
        
        MvcResult response = this.mockMvc.perform(post("/api/movimientos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM1))))
        .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }
 
    @Test
    public void EditMovimiento_ReturnsEditedMovimiento () throws Exception{

        int testID = 3;
        when(movimientoService.updateMovimiento(anyInt(), Mockito.any(MovimientoDto.class))).thenReturn(MockData.MockM3);

        MvcResult response = this.mockMvc.perform(patch("/api/movimientos/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM3))))
        .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM3)));
    }

    @Test
    public void EditMovimiento_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Movimiento con id "+testID+" no existe");

        when(movimientoService.updateMovimiento(anyInt(), Mockito.any(MovimientoDto.class))).thenReturn(null);

        MvcResult response = this.mockMvc.perform(patch("/api/movimientos/"+testID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM3))))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

    @Test
    public void RemoveMovimiento_ReturnsCorrectStatusCode () throws Exception{

        int testID = 1;
        when(movimientoService.deleteMovimientoById(anyInt())).thenReturn(1);

        MvcResult response = this.mockMvc.perform(delete("/api/movimientos/"+testID))
        .andDo(print()).andExpect(status().isNoContent()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), "");
    }

    @Test
    public void RemoveMovimiento_NonExisting_ReturnsErrorMsg () throws Exception{

        int testID = 5;
        ErrorMsg errorMsg = new ErrorMsg("Movimiento con id "+testID+" no existe");

        when(movimientoService.deleteMovimientoById(anyInt())).thenReturn(0);

        MvcResult response = this.mockMvc.perform(delete("/api/movimientos/"+testID))
        .andDo(print()).andExpect(status().isNotFound()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(errorMsg));
    }

}
