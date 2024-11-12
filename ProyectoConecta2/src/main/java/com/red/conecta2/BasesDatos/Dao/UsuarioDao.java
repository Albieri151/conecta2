package com.red.conecta2.BasesDatos.Dao;

import com.red.conecta2.Models.Usuario;

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
                usuario.setId(rs.getLong("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setEdad(rs.getInt("edad"));
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
    public boolean agregarUsuarios(Usuario usuario) {

        String sql = "INSERT INTO usuarios" +
                "(nombre, email, edad, apellido, usuario,genero,password,preguntaDeSeguridad," +
                "respuestaDeSeguridad,phone,fechaDeNacimiento) " +
                "VALUES(?, ?, ?,?,?,?,?,?,?,?,?)";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getEdad());
            ps.setString(4, usuario.getApellido());
            ps.setString(5, usuario.getUsuario());
            ps.setString(6, usuario.getGenero());
            ps.setString(7, usuario.getPassword());
            ps.setString(8, usuario.getPreguntaDeSeguridad());
            ps.setString(9, usuario.getRespuestaDeSeguridad());
            ps.setLong(10,usuario.getPhone());
            ps.setDate(11, Date.valueOf(usuario.getFechaDeNacimiento()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar con la base de datos " + e.getMessage());
        }finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion " + e.getMessage());
            }
        }

        return false;
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
    public boolean buscarUsuarioId(Usuario usuario) {
        return false;
    }
}
