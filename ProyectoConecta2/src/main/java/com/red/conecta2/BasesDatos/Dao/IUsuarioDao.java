package com.red.conecta2.BasesDatos.Dao;

import com.red.conecta2.Models.Comentarios;
import com.red.conecta2.Models.Publicacion;
import com.red.conecta2.Models.Usuario;

import java.util.List;
import java.util.Map;

public interface IUsuarioDao {
    List<Publicacion> listaPublicacionesMias(Long idUsuario);
    void agregarUsuarios(Usuario usuario);
    void eliminarSeguido(Long idUsuarioDejado,Long idUsuarioLogueado);
    void actualizarUsuario(Usuario usuario);
    Usuario loguearUsuario(Usuario usuario);
    Usuario buscarUsuario(Long idUsuario);
    Usuario buscarUsuario(String userUsuario);
    Usuario recuperarPassword(Usuario usuario);
    List<Usuario> buscadorUsuarios(String usuarios);
    List<Usuario> listaSeguidos(Long idUsuarios);
    void seguirUsuario(Long idUsuarioSeguido, Long idUsuarioLogueado);
    void realizarPublicacion(Long idUsuario, String contenido);
    List<Publicacion> listaPublicaciones(Long idUserLogueado);
    void agregarComentario(Comentarios comentarios);
    void darLike(Long userLogueado, Long idPubli);
    void quitarLike(Long userLogueado, Long idPubli);
    Map<String, Object> cantidadLikes(Long id);
    void eliminarComentario(Long id);
}
