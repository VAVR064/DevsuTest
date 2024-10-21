package com.app_test.microservice1.Persona;

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

import com.app_test.microservice1.Util.ErrorMsg;

@RestController
@RequestMapping(path = "/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        return ResponseEntity.status(HttpStatus.OK).body(personaService.findAllPersonas());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getPersonaByID(@PathVariable int id) {
        Persona check = personaService.findPersonaById(id);

        if (check != null) {
            return ResponseEntity.status(HttpStatus.OK).body(check);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Persona con id " + id + " no existe"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPersona(@RequestBody Persona nPersona) {
        ErrorMsg check = personaService.checkValidPersona(nPersona);
        if (check != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(check);

        Persona foundP = personaService.findPersonaById(nPersona.getIdentificacion());

        if (foundP != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("Persona con id " + nPersona.getIdentificacion() + " ya existe"));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(personaService.addPersona(nPersona));
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> editPersona(@PathVariable int id, @RequestBody Persona ePersona){
        Persona uPersona = personaService.updatePersona(id, ePersona);

        if (uPersona != null) {
            return ResponseEntity.status(HttpStatus.OK).body(uPersona);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Persona con id " + id + " no existe"));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removePersonaByID(@PathVariable int id) {
        int check = personaService.deletePersonaById(id);

        if (check > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Persona con id " + id + " no existe"));
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
