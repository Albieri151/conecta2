package com.red.conecta2.Controllers;

import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import com.red.conecta2.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginUsuarioController {

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/loginUser", method = RequestMethod.POST)
    public String iniciarSesion(@RequestBody Usuario usuario){
        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuarioLogueado = usuarioDao.loguearUsuario(usuario);

        if(usuarioLogueado != null){
            String tokenJWT= jwtUtil.create(String.valueOf(usuarioLogueado.getId()),usuarioLogueado.getEmail());
            return tokenJWT;
        }else return"No esta registrado";
    }
}
