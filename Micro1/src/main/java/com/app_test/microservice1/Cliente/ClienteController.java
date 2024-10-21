package com.app_test.microservice1.Cliente;

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
@RequestMapping(path = "/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findAllClientes());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getClienteByID(@PathVariable int id) {
        Cliente check = clienteService.findClienteById(id);

        if (check != null) {
            return ResponseEntity.status(HttpStatus.OK).body(check);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Cliente con id " + id + " no existe"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody ClienteDto nCliente) {
        ErrorMsg check = clienteService.checkValidCliente(nCliente);
        if (check != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(check);

        Cliente foundP = clienteService.findClienteById(nCliente.getClienteID());

        if (foundP != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMsg("Cliente con id " + nCliente.getClienteID() + " ya existe"));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.ClienteToClienteDto(clienteService.addCliente(nCliente)));
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> editCliente(@PathVariable int id, @RequestBody ClienteDto eCliente){
        ClienteDto uCliente = ClienteMapper.ClienteToClienteDto(clienteService.updateCliente(id, eCliente));

        if (uCliente != null) {
            return ResponseEntity.status(HttpStatus.OK).body(uCliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Cliente con id " + id + " no existe"));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeClienteByID(@PathVariable int id) {
        int check = clienteService.deleteClienteById(id);

        if (check > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorMsg("Cliente con id " + id + " no existe"));
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
