package com.app_test.microservice2.Cliente;

import org.springframework.web.client.RestTemplate;

public final class ClienteServices {

    private ClienteServices(){}

    private static String BASE_URL = "localhost";
    private static String MICRO1_PORT = "8080";

    public static Cliente getClienteByIDFromMicro1(int id){
        String url = "http://"+ BASE_URL +":"+ MICRO1_PORT +"/api/clientes/" + String.valueOf(id);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Cliente.class); 
    }
    
}
