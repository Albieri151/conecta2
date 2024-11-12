package com.red.conecta2.BasesDatos.Dao;

import com.red.conecta2.Models.Usuario;

import java.util.List;

public interface IUsuarioDao {
    List<Usuario> listarUsuarios();
    boolean agregarUsuarios(Usuario usuario);
    boolean eliminarUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
    boolean buscarUsuarioId(Usuario usuario);
}
