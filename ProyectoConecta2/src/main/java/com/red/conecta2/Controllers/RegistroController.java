package com.red.conecta2.Controllers;


import com.red.conecta2.BasesDatos.Dao.IUsuarioDao;
import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistroController {

    @RequestMapping(value = "registrar")
    public void registroUsuario(@RequestBody Usuario usuario){
        IUsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.agregarUsuarios(usuario);
    }
}
