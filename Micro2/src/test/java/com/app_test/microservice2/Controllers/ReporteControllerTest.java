package com.app_test.microservice2.Controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.app_test.microservice2.Reporte.ReporteController;
import com.app_test.microservice2.Reporte.ReporteService;
import com.app_test.microservice2.Util.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReporteService reporteService;

    @Test
    public void GetAllMovimientos_ReturnsListOfMovimientos () throws Exception{

        when(reporteService.getReportByParams(anyString(), anyString(), anyInt())).thenReturn(MockData.MockLR);
        when(reporteService.checkDateParams(anyString(), anyString())).thenReturn(null);

        MvcResult response = this.mockMvc.perform(get("/api/reportes")
        .param("fecha_inicio", "2024-10-18")
        .param("fecha_fin", "2024-10-22")
        .param("cliente", "1")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals(response.getResponse().getContentAsString(), objectMapper.writeValueAsString(MockData.MockLR));
    }
    
}
