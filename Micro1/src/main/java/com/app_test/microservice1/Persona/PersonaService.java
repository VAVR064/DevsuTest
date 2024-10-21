package com.app_test.microservice1.Persona;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app_test.microservice1.Util.ErrorMsg;

import jakarta.transaction.Transactional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public ErrorMsg checkValidPersona(Persona nPersona) {
        String errM = "";

        if (String.valueOf(nPersona.getIdentificacion()).length() != 10)
            errM += "Identificacion debe contener 10 digitos. ";

        if (nPersona.getNombre() == null || nPersona.getNombre().length() == 0)
            errM += "Nombre no debe estar vacío. ";

        if (nPersona.getGenero() == null || nPersona.getGenero().length() == 0)
            errM += "Género no debe estar vacío. ";

        if (nPersona.getDireccion() == null || nPersona.getDireccion().length() == 0)
            errM += "Dirección no debe estar vacío. ";

        if (nPersona.getTelefono() == null || nPersona.getTelefono().length() == 0)
            errM += "Teléfono no debe estar vacío. ";

        if (nPersona.getEdad() <= 0)
            errM += "Edad debe ser mayor a cero. ";

        if (errM.length() > 0)
            return new ErrorMsg(errM);
        else
            return null;

    }

    public List<Persona> findAllPersonas() {
        return personaRepository.findAll();
    }

    public Persona findPersonaById(int id) {
        return personaRepository.findByIdentificacion(id);
    }

    public Persona addPersona(Persona nPersona) {
        return personaRepository.save(nPersona);
    }

    public Persona updatePersona(int id, Persona ePersona) {

        Persona uPersona = personaRepository.findByIdentificacion(id);
        if (uPersona == null)
            return null;

        if (ePersona.getNombre() != null && ePersona.getNombre().length() != 0)
            uPersona.setNombre(ePersona.getNombre());

        if (ePersona.getGenero() != null && ePersona.getGenero().length() != 0)
            uPersona.setGenero(ePersona.getGenero());

        if (ePersona.getDireccion() != null && ePersona.getDireccion().length() != 0)
            uPersona.setDireccion(ePersona.getDireccion());

        if (ePersona.getTelefono() != null && ePersona.getTelefono().length() != 0)
            uPersona.setTelefono(ePersona.getTelefono());

        if (ePersona.getEdad() > 0)
            uPersona.setEdad(ePersona.getEdad());

        return personaRepository.save(uPersona);

    }

    @Transactional
    public int deletePersonaById(int id) {
        return personaRepository.deleteByIdentificacion(id);
    }

}
