package com.red.conecta2.Controllers;

import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import com.red.conecta2.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BuscadorDeUsuario {

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/buscarUser", method = RequestMethod.POST)
    public Usuario buscarUsuarios(@RequestHeader(value = "Authorization") String token,
                                  @RequestBody String userBuscado){
        String idUsuarioLogueado = jwtUtil.getKey(token);
        UsuarioDao usuarioDao = new UsuarioDao();
        if (idUsuarioLogueado == null){
            return null;
        }
        return usuarioDao.buscarUsuario(userBuscado);
    }
}
