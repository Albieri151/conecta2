package com.red.conecta2.Controllers;


import com.red.conecta2.BasesDatos.Dao.IUsuarioDao;
import com.red.conecta2.BasesDatos.Dao.UsuarioDao;
import com.red.conecta2.Models.Usuario;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistroController {

    @RequestMapping(value = "registrar", method = RequestMethod.POST)
    public ResponseEntity<String> registroUsuario(@RequestBody Usuario usuario) {
        System.out.println("Datos recibidos en el backend: " + usuario);

        IUsuarioDao usuarioDao = new UsuarioDao();

        try {
            usuarioDao.agregarUsuarios(usuario);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (DataIntegrityViolationException e) {
            String mensajeError = e.getMessage();
            System.out.println("Error de integridad: " + mensajeError);

            if (mensajeError.contains("Usuario.email")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El email proporcionado ya está registrado.");
            } else if (mensajeError.contains("Usuario.phone")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El número de teléfono proporcionado ya está registrado.");
            } else if (mensajeError.contains("Usuario.username")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre de usuario proporcionado ya está registrado.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ya existe un usuario con un valor único duplicado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No se pudo registrar el usuario. Por favor, intente más tarde.");
        }
    }
}


