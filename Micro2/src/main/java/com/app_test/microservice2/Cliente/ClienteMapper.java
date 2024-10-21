package com.app_test.microservice2.Cliente;

import com.app_test.microservice2.Persona.Persona;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteDto ClienteToClienteDto(Cliente cliente) {

        if (cliente == null) return null;

        return new ClienteDto(
                cliente.getClienteID(),
                cliente.getPersonaID().getIdentificacion(),
                cliente.getContrasena(),
                cliente.isEstado() ? "true" : "false");
    }

    public static Cliente ClienteDtoToCliente(ClienteDto clientedDto) {

        if (clientedDto == null) return null;

        return new Cliente(
                clientedDto.getClienteID(),
                new Persona(clientedDto.getPersonaID()),
                clientedDto.getContrasena(),
                clientedDto.getEstado().equals("true"));
    }

}
