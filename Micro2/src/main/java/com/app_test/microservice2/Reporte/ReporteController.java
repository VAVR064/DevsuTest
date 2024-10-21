package com.app_test.microservice2.Reporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.app_test.microservice2.Util.ErrorMsg;

@RestController
@RequestMapping(path = "/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<?> getReporte(@RequestParam String fecha_inicio, @RequestParam String fecha_fin,
            @RequestParam String cliente) {

        try {
            ErrorMsg check = reporteService.checkDateParams(fecha_inicio, fecha_fin);
            if (check != null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(check);
            else
                return ResponseEntity.status(HttpStatus.OK)
                        .body(reporteService.getReportByParams(fecha_inicio, fecha_fin, Integer.parseInt(cliente)));
        } catch (NumberFormatException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("Cliente debe ser un número"));
        }
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMsg> errorType(MethodArgumentTypeMismatchException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMsg("Cliente debe ser un número"));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMsg> errorType(MissingServletRequestParameterException err) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMsg("La solicitud debe contener los siguientes parámetros: fecha_inicio, fecha_fin, cliente"));
    }

}
