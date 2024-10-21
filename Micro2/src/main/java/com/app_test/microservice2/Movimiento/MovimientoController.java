package com.app_test.microservice2.Movimiento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path = "/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<Movimiento>> getAllMovimientos() {
        return ResponseEntity.status(HttpStatus.OK).body(movimientoService.findAllMovimientos());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getMovimientoByID(@PathVariable int id) {
        Movimiento check = movimientoService.findMovimientoById(id);

        if (check != null) {
            return ResponseEntity.status(HttpStatus.OK).body(check);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Movimiento con id " + id + " no existe"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createMovimiento(@RequestBody MovimientoDto nMovimiento) {
        try {
            ErrorMsg check = movimientoService.checkValidMovimientoJSON(nMovimiento);
            if (check != null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(check);

            Movimiento foundP = movimientoService.findMovimientoById(nMovimiento.getIdMovimiento());

            if (foundP != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorMsg("Movimiento con id " + nMovimiento.getIdMovimiento() + " ya existe"));
            } else {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(MovimientoMapper.MovimientoToMovimientoDto(movimientoService.addMovimiento(nMovimiento)));
            }
        } catch (Exception exc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorMsg("No se pudo crear el movimiento."));
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> editMovimiento(@PathVariable int id, @RequestBody MovimientoDto eMovimiento) {
        try {
            MovimientoDto uMovimiento = MovimientoMapper
                    .MovimientoToMovimientoDto(movimientoService.updateMovimiento(id, eMovimiento));

            if (uMovimiento != null) {
                return ResponseEntity.status(HttpStatus.OK).body(uMovimiento);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorMsg("Movimiento con id " + id + " no existe"));
            }
        } catch (Exception exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("Fondos insuficientes"));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeMovimientoByID(@PathVariable int id) {
        try {
            int check = movimientoService.deleteMovimientoById(id);

            if (check > 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorMsg("Movimiento con id " + id + " no existe"));
            }
        } catch (Exception exc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorMsg("No se pudo realizar el borrado."));
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

}
