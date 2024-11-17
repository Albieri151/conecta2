package com.red.conecta2.Controllers;

import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecuperarUsuarioController {

    @RequestMapping(value = "api/recuperarUsuario", method = RequestMethod.POST)
    public Usuario recuperarPassword(@RequestBody Usuario usuario) {
        UsuarioDao usuarioDao = new UsuarioDao();
        return usuarioDao.recuperarPassword(usuario);
    }
}
