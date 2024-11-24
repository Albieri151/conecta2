package com.red.conecta2.Controllers;

import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import com.red.conecta2.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class BarraBuscadorController {

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/buscador")
    public ArrayList<Usuario> buscadorUsuarios(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody String usuariosBuscados){
        String idUsuarioLogueado = jwtUtil.getKey(token);
        UsuarioDao usuarioDao = new UsuarioDao();
        if (idUsuarioLogueado == null){
            return new ArrayList<>();
        }
        return new ArrayList<>(usuarioDao.buscadorUsuarios(usuariosBuscados));
    }
}
