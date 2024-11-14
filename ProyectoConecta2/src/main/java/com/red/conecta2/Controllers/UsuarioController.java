package com.red.conecta2.Controllers;

import com.red.conecta2.Models.Usuario;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {

    @RequestMapping(value = "Ami/{id}")
    public Usuario prueba(@PathVariable Long id){
        return new Usuario(11L,"Albieri", "Reyes","Albis400","M","Albieri@gmail.com",
                "Pedrito3030", "Chupa el perro", "Chupatelo", "04126146850",
               "2002-09-12");
    }

    @RequestMapping(value = "Amigos")
    public List<Usuario> prueba(){

        Usuario usuario1 = new Usuario(12L,"Albieri", "Alaña","Albis400","M","Albieri@gmail.com",
                "Pedrito3030", "Chupa el perro", "Chupatelo", "04126146850",
               "2002-12-09");

        Usuario usuario2 = new Usuario(13L,"Albieri", "Alaña","Albis400","M","Albieri@gmail.com",
                "Pedrito3030", "Chupa el perro", "Chupatelo", "04126146850",
               "2002-09-12");

        Usuario usuario3 = new Usuario(14L,"Albieri", "Alaña","Albis400","M","Albieri@gmail.com",
                "Pedrito3030", "Chupa el perro", "Chupatelo", "04126146850",
                "2002-09-12");
        List<Usuario> listaUsers = new ArrayList<>();
        listaUsers.add(usuario1);
        listaUsers.add(usuario2);
        listaUsers.add(usuario3);

        return listaUsers;
    }


}
