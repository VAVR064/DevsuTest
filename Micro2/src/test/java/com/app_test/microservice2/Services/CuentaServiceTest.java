package com.app_test.microservice2.Services;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app_test.microservice2.Cuenta.Cuenta;
import com.app_test.microservice2.Cuenta.CuentaMapper;
import com.app_test.microservice2.Cuenta.CuentaRepository;
import com.app_test.microservice2.Cuenta.CuentaService;
import com.app_test.microservice2.Util.MockData;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {
    
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    public void FindAllCuentas_ReturnsListOfCuentas(){
        
        when(cuentaRepository.findAll()).thenReturn(MockData.MockLC);

        List<Cuenta> dbCuenta = cuentaService.findAllCuentas();

        Assertions.assertEquals(dbCuenta.size(), MockData.MockLC.size());

    }

    @Test
    public void FindCuentaById_ReturnsOneCuentaWithSameID(){

        when(cuentaRepository.findByIdCuenta(Mockito.anyInt())).thenReturn(MockData.MockC1);

        Cuenta eCuenta = cuentaService.findCuentaById(1);

        Assertions.assertEquals(eCuenta, MockData.MockC1);

    }

    @Test
    public void AddCuenta_ReturnsLastCuenta(){

        when(cuentaRepository.save(Mockito.any(Cuenta.class))).thenReturn(MockData.MockCF2);

        Cuenta eCuenta = cuentaService.addCuenta(CuentaMapper.CuentaToCuentaDto(MockData.MockCF2));

        Assertions.assertEquals(eCuenta, MockData.MockCF2);
    }

    @Test
    public void UpdateCuenta_ReturnsUpdatedCuenta(){

        when(cuentaRepository.findByIdCuenta(Mockito.anyInt())).thenReturn(MockData.MockCF3);
        when(cuentaRepository.save(Mockito.any(Cuenta.class))).thenReturn(MockData.MockCF3);

        Cuenta eCuenta = cuentaService.updateCuenta(MockData.MockCF3.getIdCuenta(), CuentaMapper.CuentaToCuentaDto(MockData.MockCF2));

        Assertions.assertEquals(eCuenta, MockData.MockCF3);
    }
    
    @Test
    public void DeleteCuentaById_ReturnsNumberOne(){

        when(cuentaRepository.deleteByIdCuenta(Mockito.anyInt())).thenReturn(1);

        int eValue = cuentaService.deleteCuentaById(MockData.MockC2.getIdCuenta());

        Assertions.assertEquals(eValue, 1);

    } 

}
