package com.app_test.microservice1.Cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDto {

    private int clienteID;

    private int personaID;

    private String contrasena;

    private String estado;
    
}
