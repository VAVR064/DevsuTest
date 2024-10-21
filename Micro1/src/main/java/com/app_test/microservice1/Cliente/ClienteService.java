package com.app_test.microservice1.Cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app_test.microservice1.Persona.Persona;
import com.app_test.microservice1.Persona.PersonaRepository;
import com.app_test.microservice1.Util.ErrorMsg;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public ErrorMsg checkValidCliente(ClienteDto nCliente) {
        String errM = "";

        if (String.valueOf(nCliente.getPersonaID()).length() != 10)
            errM += "Identificación de la persona debe contener 10 digitos. ";        
        else if (personaRepository.findByIdentificacion(nCliente.getPersonaID()) == null)
            errM += "Persona no existe. ";

        if (nCliente.getContrasena() == null || nCliente.getContrasena().length() == 0)
            errM += "Contraseña no debe estar vacío. ";

        if (nCliente.getEstado() == null)
            errM += "Estado no debe estar vacío. ";
        else if (!nCliente.getEstado().equals("true") && !nCliente.getEstado().equals("false"))
            errM += "Estado debe ser 'true' o 'false'. ";

        if (errM.length() > 0)
            return new ErrorMsg(errM);
        else
            return null;

    }

    public List<Cliente> findAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente findClienteById(int id) {
        return clienteRepository.findByClienteID(id);
    }

    public Cliente addCliente(ClienteDto nCliente) {
        return clienteRepository.save(ClienteMapper.ClienteDtoToCliente(nCliente));
    }

    public Cliente updateCliente(int id, ClienteDto eCliente) {

        Cliente uCliente = clienteRepository.findByClienteID(id);
        if (uCliente == null)
            return null;

        if (personaRepository.findByIdentificacion(eCliente.getPersonaID()) != null)
            uCliente.setPersonaID(new Persona(eCliente.getPersonaID()));

        if (eCliente.getContrasena() != null && eCliente.getContrasena().length() != 0)
            uCliente.setContrasena(eCliente.getContrasena());

        if (eCliente.getEstado() != null &&
                (eCliente.getEstado().equals("true") || eCliente.getEstado().equals("false")))
            uCliente.setEstado(eCliente.getEstado().equals("true"));

        return clienteRepository.save(uCliente);

    }

    @Transactional
    public int deleteClienteById(int id) {
        return clienteRepository.deleteByClienteID(id);
    }
}
