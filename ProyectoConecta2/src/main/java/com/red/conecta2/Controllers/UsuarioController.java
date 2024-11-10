package com.red.conecta2.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    @RequestMapping(value = "prueba")
    public String prueba(){
        return "juguemos xd";
    }
}
