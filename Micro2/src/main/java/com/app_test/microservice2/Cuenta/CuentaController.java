package com.app_test.microservice2.Cuenta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.app_test.microservice2.Util.ErrorMsg;

@RestController
@RequestMapping(path = "/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> getAllCuentas() {
        return ResponseEntity.status(HttpStatus.OK).body(cuentaService.findAllCuentas());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getCuentaByID(@PathVariable int id) {
        Cuenta check = cuentaService.findCuentaById(id);

        if (check != null) {
            return ResponseEntity.status(HttpStatus.OK).body(check);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Cuenta con id " + id + " no existe"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCuenta(@RequestBody CuentaDto nCuenta) {
        ErrorMsg check = cuentaService.checkValidCuenta(nCuenta);
        if (check != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(check);

        Cuenta foundP = cuentaService.findCuentaById(nCuenta.getIdCuenta());

        if (foundP != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("Cuenta con id " + nCuenta.getIdCuenta() + " ya existe"));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(CuentaMapper.CuentaToCuentaDto(cuentaService.addCuenta(nCuenta)));
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> editCuenta(@PathVariable int id, @RequestBody CuentaDto eCuenta){
        CuentaDto uCuenta = CuentaMapper.CuentaToCuentaDto(cuentaService.updateCuenta(id, eCuenta));

        if (uCuenta != null) {
            return ResponseEntity.status(HttpStatus.OK).body(uCuenta);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Cuenta con id " + id + " no existe"));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeCuentaByID(@PathVariable int id) {
        int check = cuentaService.deleteCuentaById(id);

        if (check > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Cuenta con id " + id + " no existe"));
        }
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMsg> errorType(MethodArgumentTypeMismatchException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("ID debe ser un número"));
    }    

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMsg> errorFormat(HttpMessageNotReadableException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("Formato JSON no válido"));
    }   
    
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMsg> errorData(DataIntegrityViolationException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("No se puede borrar debido a que otra tabla depende de esta entrada"));
    } 

}
