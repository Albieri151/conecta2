package com.red.conecta2.Controllers;

import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Comentarios;
import com.red.conecta2.Models.Publicacion;
import com.red.conecta2.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class PublicacionController {

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/publicaciones", method = RequestMethod.POST)
    public List<Publicacion> crearPublicacion(@RequestHeader(value = "Authorization") String token) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null){
            return new ArrayList<>();
        }
        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            return usuarioDao.listaPublicaciones(Long.parseLong(idUsuarioLogueado));
        } catch (DataIntegrityViolationException e) {
            System.out.println("Hubo un error al intentar conectar con la base de datos y traer las publicaciones " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Hubo un error al intentar traer las publicaciones inesperado " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "api/nuevoComentario", method = RequestMethod.POST)
    public void crearComentario(@RequestHeader(value = "Authorization") String token,
                                 @RequestBody Comentarios comentarios) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null){
            return ;
        }

        comentarios.setUser_id(Long.parseLong(idUsuarioLogueado));
        UsuarioDao usuarioDao = new UsuarioDao();
        try {
             usuarioDao.agregarComentario(comentarios);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Hubo un error al intentar conectar con la base de datos y agg los comentarios " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hubo un error al intentar traer las publicaciones inesperado " + e.getMessage());
        }
    }

    @RequestMapping(value = "api/nuevoComentario", method = RequestMethod.DELETE)
    public void eliminarComentario(@RequestHeader(value = "Authorization") String token,
                                @RequestBody Long comentarios) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null){
            return ;
        }
        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            usuarioDao.eliminarComentario(comentarios);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Hubo un error al intentar conectar con la base de datos y agg los comentarios " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hubo un error al intentar traer las publicaciones inesperado " + e.getMessage());
        }
    }

    @RequestMapping(value = "api/nuevoLike", method = RequestMethod.POST)
    public void darLike(@RequestHeader(value = "Authorization") String token,
                        @RequestHeader(value = "pub_id") Long id) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null){
            return ;
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            usuarioDao.darLike(Long.parseLong(idUsuarioLogueado),id);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Hubo un error al intentar conectar con la base de datos y agg los like " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hubo un error al intentar traer las publicaciones inesperado " + e.getMessage());
        }
    }

    @RequestMapping(value = "api/nuevoLike", method = RequestMethod.DELETE)
    public void quitarLike(@RequestHeader(value = "Authorization") String token,
                        @RequestHeader(value = "pub_id") Long id) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null){
            return ;
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            usuarioDao.quitarLike(Long.parseLong(idUsuarioLogueado),id);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Hubo un error al intentar conectar con la base de datos y agg los like " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hubo un error al intentar traer las publicaciones inesperado " + e.getMessage());
        }
    }

    @RequestMapping(value = "api/nuevoLike", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> detalleLikes(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "pub_id") Long id) {
        // Verificar el token y obtener el usuario logueado
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Retorna 401 si no está autorizado
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            // Llamar al DAO para obtener los datos
            Map<String, Object> datos = usuarioDao.cantidadLikes(id);
            return ResponseEntity.ok(datos); // Retorna los datos con un status 200
        } catch (Exception e) {
            System.out.println("Error al obtener detalles de likes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 para errores genéricos
        }
    }

    @RequestMapping(value = "api/publicacionesNuevas", method = RequestMethod.POST)
    public void realizarPublicacion(@RequestHeader(value = "Authorization") String token,
                                    @RequestBody String contenido) {
        String idUsuarioLogueado = jwtUtil.getKey(token);
        if (idUsuarioLogueado == null){
            System.out.println("Su sesion expiro");
            return;
        }
        UsuarioDao usuarioDao = new UsuarioDao();
        try {
            String publicacion = contenido.substring(14,contenido.length()-4);
            usuarioDao.realizarPublicacion(Long.parseLong(idUsuarioLogueado), publicacion);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Hubo un error al intentar conectar con la base de datos y traer las publicaciones " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hubo un error al intentar traer las publicaciones inesperado " + e.getMessage());
        }
    }



}
