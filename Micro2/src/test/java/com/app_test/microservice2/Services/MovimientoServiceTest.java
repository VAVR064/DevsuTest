package com.app_test.microservice2.Services;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app_test.microservice2.Cuenta.CuentaRepository;
import com.app_test.microservice2.Cuenta.CuentaService;
import com.app_test.microservice2.Movimiento.Movimiento;
import com.app_test.microservice2.Movimiento.MovimientoMapper;
import com.app_test.microservice2.Movimiento.MovimientoRepository;
import com.app_test.microservice2.Movimiento.MovimientoService;
import com.app_test.microservice2.Util.MockData;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceTest {
    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private MovimientoService movimientoService;

    @Test
    public void FindAllMovimientos_ReturnsListOfMovimientos(){
        
        when(movimientoRepository.findAll()).thenReturn(MockData.MockLM);

        List<Movimiento> dbMovimiento = movimientoService.findAllMovimientos();

        Assertions.assertEquals(dbMovimiento.size(), MockData.MockLM.size());

    }

    @Test
    public void FindMovimientoById_ReturnsOneMovimientoWithSameID(){

        when(movimientoRepository.findByIdMovimiento(Mockito.anyInt())).thenReturn(MockData.MockM1);

        Movimiento eMovimiento = movimientoService.findMovimientoById(1);

        Assertions.assertEquals(eMovimiento, MockData.MockM1);

    }

    @Test
    public void AddMovimiento_ReturnsLastMovimiento(){

        when(cuentaRepository.findByIdCuenta(anyInt())).thenReturn(MockData.MockCF1);
        when(movimientoRepository.save(Mockito.any(Movimiento.class))).thenReturn(MockData.MockM1);

        Movimiento eMovimiento = movimientoService.addMovimiento(MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM1));

        Assertions.assertEquals(eMovimiento, MockData.MockM1);
    }

    @Test
    public void UpdateMovimiento_ReturnsUpdatedMovimiento(){

        when(cuentaRepository.findByIdCuenta(anyInt())).thenReturn(MockData.MockCF1);
        when(movimientoRepository.findByIdMovimiento(Mockito.anyInt())).thenReturn(MockData.MockM3);
        when(movimientoRepository.save(Mockito.any(Movimiento.class))).thenReturn(MockData.MockM3);

        Movimiento eMovimiento = movimientoService.updateMovimiento(MockData.MockM3.getIdMovimiento(), MovimientoMapper.MovimientoToMovimientoDto(MockData.MockM2));

        Assertions.assertEquals(eMovimiento, MockData.MockM3);
    }
    
    @Test
    public void DeleteMovimientoById_ReturnsNumberOne(){

        when(movimientoRepository.findByIdMovimiento(Mockito.anyInt())).thenReturn(MockData.MockM2);
        when(movimientoRepository.deleteByIdMovimiento(Mockito.anyInt())).thenReturn(1);

        int eValue = movimientoService.deleteMovimientoById(MockData.MockM2.getIdMovimiento());

        Assertions.assertEquals(eValue, 1);

    } 
}
