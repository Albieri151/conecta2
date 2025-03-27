package com.red.conecta2.Controllers;

import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import com.red.conecta2.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/profile", method = RequestMethod.GET)
    public Usuario getPerfil(@RequestHeader(value = "Authorization") String token){
        String idUsuarioLogueado = jwtUtil.getKey(token);
        UsuarioDao usuarioDao = new UsuarioDao();
        return usuarioDao.buscarUsuario(Long.parseLong(idUsuarioLogueado));
    }

    @RequestMapping(value = "api/seguidores", method = RequestMethod.POST)
    public List<Usuario> getSeguidores(@RequestHeader(value = "Authorization") String token){
        String idUsuarioLogueado = jwtUtil.getKey(token);
        UsuarioDao usuarioDao = new UsuarioDao();
        if (idUsuarioLogueado == null){
            return new ArrayList<>();
        }
        return usuarioDao.listaSeguidos(Long.parseLong(idUsuarioLogueado));
    }

    @RequestMapping(value = "api/miPerfil", method = RequestMethod.PUT)
    public void actualizarPerfil(@RequestHeader(value = "Authorization") String token,
                                 @RequestBody Usuario usuario){
        String idUsuarioLogueado = jwtUtil.getKey(token);
        UsuarioDao usuarioDao = new UsuarioDao();
        if (idUsuarioLogueado == null){
            return;
        }
        usuario.setId(Long.parseLong(idUsuarioLogueado));
        usuarioDao.actualizarUsuario(usuario);
    }

    @RequestMapping(value = "api/eliminarSeguido/{id}", method = RequestMethod.DELETE)
    public void eliminarSeguido(@RequestHeader(value = "Authorization") String token,
                                 @PathVariable Long id){
        String idUsuarioLogueado = jwtUtil.getKey(token);
        UsuarioDao usuarioDao = new UsuarioDao();
        if (idUsuarioLogueado == null){
            return;
        }
        usuarioDao.eliminarSeguido(id,Long.parseLong(idUsuarioLogueado));
    }

    @RequestMapping(value = "api/seguirUsuario", method = RequestMethod.POST)
    public ResponseEntity<String> empezarSeguir(@RequestHeader(value = "Authorization") String token,
                                                @RequestBody Long id) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado.");
        }
        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            usuarioDao.seguirUsuario(id, Long.parseLong(idUsuarioLogueado));
            return ResponseEntity.ok("Ahora estás siguiendo al usuario.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya estás siguiendo a este usuario.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error.");
        }
    }



}
