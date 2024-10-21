package com.app_test.microservice1.Util;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.app_test.microservice1.Cliente.Cliente;
import com.app_test.microservice1.Persona.Persona;

public final class MockData {

    private MockData() {
    }

    //Persona
    public static Persona MockP1 = new Persona(1, "prueba1", "masculino", 35, "Quito", "0999000000");
    public static Persona MockP2 = new Persona(2, "prueba2", "femenino", 45, "Guayaquil", "0999000001");
    public static Persona MockP3 = new Persona(3, "prueba3", "femenino", 55, "Cuenca", "0999000002");
    
    public static Persona MockPW = new Persona(0, "", "", -1, "", "");
    public static ErrorMsg MockPErr = new ErrorMsg("Identificacion debe contener 10 digitos. Nombre no debe estar vacío. Género no debe estar vacío. Dirección no debe estar vacío. Teléfono no debe estar vacío. Edad debe ser mayor a cero. ");

    public static List<Persona> MockLP = new ArrayList<Persona>(Arrays.asList(MockP1, MockP2, MockP3));

    //Cliente
    public static Cliente MockC1 = new Cliente(1, MockP1, "pswd123", true);
    public static Cliente MockC2 = new Cliente(2, MockP2, "pswd456", true);
    public static Cliente MockC3 = new Cliente(3, MockP3, "pswd789", true);

    public static Cliente MockCW = new Cliente(0, MockP1, "", false);
    public static ErrorMsg MockCErr = new ErrorMsg("Identificación de la persona debe contener 10 digitos. Contraseña no debe estar vacío. ");

    public static List<Cliente> MockLC = new ArrayList<Cliente>(Arrays.asList(MockC1, MockC2, MockC3));

    
}
