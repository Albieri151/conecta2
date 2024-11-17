package com.red.conecta2.BasesDatos.Dao;

import com.red.conecta2.Models.Usuario;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.red.conecta2.BasesDatos.Conexiones.ConexionSqlite.getConnection;

public class UsuarioDao implements IUsuarioDao {

    @Override
    public List<Usuario> listarUsuarios() {

        Connection connection = getConnection();
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar con la base de datos " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion " + e.getMessage());
            }
        }

        return usuarios;
    }

    @Override
    public void agregarUsuarios(Usuario usuario) {

        String sql = "INSERT INTO Usuario" +
                "(nombre, email, apellido, username,genero,password,pregunta_de_seguridad," +
                "respuesta_de_seguridad,phone,fecha_de_nacimiento) " +
                "VALUES(?, ?, ?,?,?,?,?,?,?,?)";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getUsuario());
            ps.setString(5, usuario.getGenero());
            ps.setString(6, usuario.getPassword());
            ps.setString(7, usuario.getPreguntaDeSeguridad());
            ps.setString(8, usuario.getRespuestaDeSeguridad());
            ps.setLong(9,Long.parseLong(usuario.getPhone()));
            ps.setDate(10, Date.valueOf(usuario.getFechaDeNacimiento()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al registrar usuario: " + e.getMessage(), e);
        }finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public boolean eliminarUsuario(Usuario usuario) {
        return false;
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        return false;
    }


    @Override
    public Usuario loguearUsuario(Usuario usuario) {
        String sql = "SELECT user_id FROM Usuario WHERE email= ? AND password= ?";
        Connection connection = getConnection();
        List<Usuario> listaUsuario = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getPassword());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuario.setId(rs.getLong("user_id"));
                listaUsuario.add(usuario);
            }
            if (!listaUsuario.isEmpty()){
                return listaUsuario.getFirst();
            }
            return null;

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al consultar usuario: " + e.getMessage(), e);
        }finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public Usuario buscarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario recuperarPassword(Usuario usuario) {
        String sql = "SELECT * FROM Usuario WHERE email = ?";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, usuario.getEmail());
            ResultSet rs = ps.executeQuery();
            List<Usuario> listaUsuario = new ArrayList<>();
            while (rs.next()) {
                Usuario usuarioTraido = new Usuario();
                usuarioTraido.setPreguntaDeSeguridad(rs.getString("pregunta_de_seguridad"));
                usuarioTraido.setRespuestaDeSeguridad(rs.getString("respuesta_de_seguridad"));
                usuarioTraido.setPassword(rs.getString("password"));
                listaUsuario.add(usuarioTraido);
            }
            if (!listaUsuario.isEmpty()){
                return listaUsuario.getFirst();
            }else return null;


        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al consultar usuario: " + e.getMessage(), e);
        }finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }
}
