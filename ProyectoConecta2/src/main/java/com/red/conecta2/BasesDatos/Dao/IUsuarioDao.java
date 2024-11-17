package com.red.conecta2.BasesDatos.Dao;

import com.red.conecta2.Models.Usuario;

import java.util.List;

public interface IUsuarioDao {
    List<Usuario> listarUsuarios();
    void agregarUsuarios(Usuario usuario);
    boolean eliminarUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
    Usuario loguearUsuario(Usuario usuario);
    Usuario buscarUsuario(Usuario usuario);
    Usuario recuperarPassword(Usuario usuario);
}
