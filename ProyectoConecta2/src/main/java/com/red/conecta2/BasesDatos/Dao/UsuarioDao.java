package com.red.conecta2.BasesDatos.Dao;

import com.red.conecta2.Models.Comentarios;
import com.red.conecta2.Models.Publicacion;
import com.red.conecta2.Models.Usuario;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.*;
import java.util.*;

import static com.red.conecta2.BasesDatos.Conexiones.ConexionSqlite.getConnection;


public class UsuarioDao implements IUsuarioDao {

    @Override
    public List<Publicacion> listaPublicacionesMias(Long idUser) {
        Connection connection = getConnection();
        List<Publicacion> publicacionesLogueado = new ArrayList<>();
        String sql = "SELECT p.pub_id, p.contenido, p.fecha_y_hora, u.username, " +
                "(SELECT COUNT(*) FROM UserLike WHERE pub_id = p.pub_id) AS cantidad_likes, " +
                "(SELECT COUNT(*) FROM Comentario WHERE pub_id = p.pub_id) AS cantidad_comentarios, " +
                "GROUP_CONCAT(c.com_id || ' - ' || c.fecha_y_hora || ' - ' || c.contenido || ' - ' || " +
                "c.user_id || ' - ' || u_com.username) AS comentarios FROM Publicacion p " +
                "JOIN Usuario u ON p.user_id = u.user_id LEFT JOIN Comentario c ON p.pub_id = c.pub_id " +
                "LEFT JOIN Usuario u_com ON c.user_id = u_com.user_id WHERE p.user_id = ? " +
                "GROUP BY p.pub_id ORDER BY p.fecha_y_hora DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idUser);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Publicacion publicacion = new Publicacion();
                publicacion.setId(rs.getLong("pub_id"));
                publicacion.setContenidoPublicacion(rs.getString("contenido"));
                publicacion.setFechaPublicacion(rs.getString("fecha_y_hora"));
                publicacion.setCreadorPublicacion(rs.getString("username"));
                publicacion.setCantidadMeGusta(rs.getLong("cantidad_likes"));
                publicacion.setCantidadComentarios(rs.getLong("cantidad_comentarios"));

                String comentariosStr = rs.getString("comentarios");

                List<Comentarios> comentarios = new ArrayList<>();
                if (comentariosStr != null) {
                    String[] comentariosArray = comentariosStr.split(",");
                    for (String comentarioStr : comentariosArray) {
                        Comentarios comentario = new Comentarios();
                        // Asumimos que el formato del comentario es 'com_id - fecha_y_hora - contenido - user_id - nombre_user'
                        String[] comentarioParts = comentarioStr.split(" - ");
                        comentario.setCom_id(Long.parseLong(comentarioParts[0]));
                        comentario.setFecha(comentarioParts[1]);
                        comentario.setContenido(comentarioParts[2]);
                        comentario.setUser_id(Long.parseLong(comentarioParts[3]));
                        comentario.setNombreUser(comentarioParts[4]);

                        comentarios.add(comentario);
                    }
                }
                publicacion.setComentarios(comentarios);


                publicacionesLogueado.add(publicacion);
            }

            return publicacionesLogueado;

        } catch (SQLException e) {
            System.out.println("Error trayendo mis publicaciones: "+e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion " + e.getMessage());
            }
        }
        return null;
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
            ps.setLong(9, Long.parseLong(usuario.getPhone()));
            ps.setString(10, usuario.getFechaDeNacimiento());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al registrar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public void eliminarSeguido(Long idUsuarioDejado, Long idUsuarioLogueado) {
        String sql = "DELETE FROM Seguir WHERE seguidor_id = ? AND siguiendo_id = ?";
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idUsuarioLogueado);
            ps.setLong(2, idUsuarioDejado);
            ps.execute();
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al eliminar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuario SET " + "nombre = ?, " + "email = ?, " + "apellido = ?, " +
                "username = ?, " + "genero = ?, " + "password = ?, " + "pregunta_de_seguridad = ?, " +
                "respuesta_de_seguridad = ?, " + "phone = ?, " + "fecha_de_nacimiento = ? " +
                "WHERE user_id = ?";

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
            ps.setLong(9, Long.parseLong(usuario.getPhone()));
            ps.setString(10, usuario.getFechaDeNacimiento());
            ps.setLong(11, usuario.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al actualizar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }


    @Override
    public Usuario loguearUsuario(Usuario usuario) {
        String sql = "SELECT user_id,username FROM Usuario WHERE email= ? AND password= ?";
        Connection connection = getConnection();
        List<Usuario> listaUsuario = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getPassword());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuario.setUsuario(rs.getString("username"));
                usuario.setId(rs.getLong("user_id"));
                listaUsuario.add(usuario);
            }
            if (!listaUsuario.isEmpty()) {
                return listaUsuario.getFirst();
            }
            return null;

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al consultar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public Usuario buscarUsuario(Long idUsuario) {
        String sql = "SELECT u.user_id, u.nombre, u.apellido, u.genero, u.email, u.username, " +
                "u.pregunta_de_seguridad, u.respuesta_de_seguridad, u.password, u.fecha_de_nacimiento, " +
                "u.phone, (SELECT COUNT(*) FROM Seguir WHERE siguiendo_id = u.user_id) AS seguidores, " +
                "(SELECT COUNT(*) FROM Seguir WHERE seguidor_id = u.user_id) AS seguidos, " +
                "(SELECT COUNT(*) FROM Publicacion WHERE user_id = u.user_id) AS publicaciones " +
                "FROM Usuario u WHERE u.user_id = ?";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            List<Usuario> listaUsuario = new ArrayList<>();
            while (rs.next()) {
                Usuario usuarioTraido = new Usuario();
                usuarioTraido.setNombre(rs.getString("nombre"));
                usuarioTraido.setApellido(rs.getString("apellido"));
                usuarioTraido.setGenero(rs.getString("genero"));
                usuarioTraido.setEmail(rs.getString("email"));
                usuarioTraido.setUsuario(rs.getString("username"));
                usuarioTraido.setPreguntaDeSeguridad(rs.getString("pregunta_de_seguridad"));
                usuarioTraido.setRespuestaDeSeguridad(rs.getString("respuesta_de_seguridad"));
                usuarioTraido.setPassword(rs.getString("password"));
                usuarioTraido.setFechaDeNacimiento(rs.getString("fecha_de_nacimiento"));
                usuarioTraido.setPhone(rs.getString("phone"));
                usuarioTraido.setPublicaciones(rs.getInt("publicaciones"));
                usuarioTraido.setSeguidores(rs.getInt("seguidores"));
                usuarioTraido.setSiguiendo(rs.getInt("seguidos"));
                listaUsuario.add(usuarioTraido);
            }
            if (!listaUsuario.isEmpty()) {
                return listaUsuario.getFirst();
            } else return null;

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al consultar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public Usuario buscarUsuario(String userUsuario) {
        String sql = "SELECT u.user_id, u.nombre, u.apellido, u.genero, u.email, u.username, " +
                "u.pregunta_de_seguridad, u.respuesta_de_seguridad, u.password, u.fecha_de_nacimiento, " +
                "u.phone, (SELECT COUNT(*) FROM Seguir WHERE siguiendo_id = u.user_id) AS seguidores, " +
                "(SELECT COUNT(*) FROM Seguir WHERE seguidor_id = u.user_id) AS seguidos, " +
                "(SELECT COUNT(*) FROM Publicacion WHERE user_id = u.user_id) AS publicaciones " +
                "FROM Usuario u WHERE u.username = ?";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String usuarioBuscado = userUsuario.substring(16, userUsuario.length() - 2);
            System.out.println(usuarioBuscado);
            ps.setString(1, usuarioBuscado);
            ResultSet rs = ps.executeQuery();
            List<Usuario> listaUsuario = new ArrayList<>();

            while (rs.next()) {
                Usuario usuarioTraido = new Usuario();
                usuarioTraido.setId(Long.parseLong(rs.getString("user_id")));
                usuarioTraido.setNombre(rs.getString("nombre"));
                usuarioTraido.setApellido(rs.getString("apellido"));
                usuarioTraido.setGenero(rs.getString("genero"));
                usuarioTraido.setEmail(rs.getString("email"));
                usuarioTraido.setUsuario(rs.getString("username"));
                usuarioTraido.setPreguntaDeSeguridad(rs.getString("pregunta_de_seguridad"));
                usuarioTraido.setRespuestaDeSeguridad(rs.getString("respuesta_de_seguridad"));
                usuarioTraido.setPassword(rs.getString("password"));
                usuarioTraido.setFechaDeNacimiento(rs.getString("fecha_de_nacimiento"));
                usuarioTraido.setPhone(rs.getString("phone"));
                usuarioTraido.setPublicaciones(rs.getInt("publicaciones"));
                usuarioTraido.setSeguidores(rs.getInt("seguidores"));
                usuarioTraido.setSiguiendo(rs.getInt("seguidos"));
                listaUsuario.add(usuarioTraido);
            }
            if (!listaUsuario.isEmpty()) {
                return listaUsuario.getFirst();
            } else return null;

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al consultar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public Usuario recuperarPassword(Usuario usuario) {
        String sql = "SELECT * FROM Usuario WHERE email = ?";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
            if (!listaUsuario.isEmpty()) {
                return listaUsuario.getFirst();
            } else return null;


        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al consultar usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexion para agg " + e.getMessage());
            }
        }
    }

    @Override
    public List<Usuario> buscadorUsuarios(String usuarios) {
        Connection connection = getConnection();
        List<Usuario> listaUsuarios = new ArrayList<>();


        String sql = "SELECT * FROM Usuario WHERE username LIKE ? " +
                "ORDER BY " +
                "CASE " +
                "WHEN username = ? THEN 1 " +
                "WHEN username LIKE ? THEN 2 " +
                "WHEN username LIKE ? THEN 3 " +
                "ELSE 4 END";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            String usuarioBuscado = usuarios.substring(21, usuarios.length() - 2);
            System.out.println(usuarioBuscado);
            ps.setString(1, "%" + usuarioBuscado + "%");
            ps.setString(2, usuarioBuscado);
            ps.setString(3, usuarioBuscado + "%");
            ps.setString(4, "%" + usuarioBuscado + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUsuario(rs.getString("username"));
                usuario.setId(rs.getLong("user_id"));
                listaUsuarios.add(usuario);
            }
            return listaUsuarios;
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar con la base de datos " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión " + e.getMessage());
            }
        }
        return listaUsuarios;
    }

    @Override
    public List<Usuario> listaSeguidos(Long idUsuarios) {
        Connection connection = getConnection();
        List<Usuario> listaUsuarios = new ArrayList<>();


        String sql = "SELECT Usuario.user_id, Usuario.nombre, Usuario.apellido, Usuario.username," +
                "Usuario.email, Usuario.fecha_de_nacimiento, Usuario.phone FROM Seguir " +
                "JOIN Usuario ON Seguir.siguiendo_id = Usuario.user_id WHERE " +
                "Seguir.seguidor_id = ?;";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, idUsuarios);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUsuario(rs.getString("username"));
                usuario.setPhone(rs.getString("phone"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setEmail(rs.getString("email"));
                usuario.setId(Long.parseLong(rs.getString("user_id")));
                usuario.setFechaDeNacimiento(rs.getString("fecha_de_nacimiento"));
                listaUsuarios.add(usuario);
            }
            return listaUsuarios;
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar con la base de datos " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión " + e.getMessage());
            }
        }
        return listaUsuarios;
    }

    public void seguirUsuario(Long idUsuarioSeguido, Long idUsuarioLogueado) {
        String verificarSql = "SELECT COUNT(*) FROM Seguir WHERE seguidor_id = ? AND siguiendo_id = ?";
        String insertarSql = "INSERT INTO Seguir (seguidor_id, siguiendo_id) VALUES (?, ?)";
        Connection connection = getConnection();
        try {
            // Verifico si ya existe la relación
            try (PreparedStatement verificarPs = connection.prepareStatement(verificarSql)) {
                verificarPs.setLong(1, idUsuarioLogueado);
                verificarPs.setLong(2, idUsuarioSeguido);
                ResultSet rs = verificarPs.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("El usuario ya está siguiendo a este usuario.");
                    return;
                }
            }


            try (PreparedStatement insertarPs = connection.prepareStatement(insertarSql)) {
                insertarPs.setLong(1, idUsuarioLogueado);
                insertarPs.setLong(2, idUsuarioSeguido);
                insertarPs.execute();
            }

        } catch (SQLException e) {
            throw new DataIntegrityViolationException("Error al seguir usuario: " + e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión: " + e.getMessage());
            }
        }
    }

    @Override
    public void realizarPublicacion(Long idUsuario, String contenido) {
        Connection connection = getConnection();
        String sql = "INSERT INTO Publicacion(contenido, user_id) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, contenido);
            ps.setLong(2, idUsuario);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al intentar conectar con la base de datos para publicar " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión " + e.getMessage());
            }
        }
    }

    @Override
    public List<Publicacion> listaPublicaciones(Long idUserLogueado) {
        List<Publicacion> publicaciones = new ArrayList<>();

        String sql = "SELECT p.pub_id, p.contenido, p.fecha_y_hora, u.username, " +
                "(SELECT COUNT(*) FROM UserLike WHERE pub_id = p.pub_id) AS cantidad_likes, " +
                "(SELECT COUNT(*) FROM Comentario WHERE pub_id = p.pub_id) AS cantidad_comentarios, " +
                "GROUP_CONCAT( c.com_id || ' - ' || c.fecha_y_hora || ' - ' || c.contenido || ' - ' || " +
                "c.user_id || ' - ' || u_com.username) AS comentarios FROM Publicacion p JOIN " +
                "Usuario u ON p.user_id = u.user_id LEFT JOIN Comentario c ON p.pub_id = c.pub_id " +
                "LEFT JOIN Usuario u_com ON c.user_id = u_com.user_id WHERE p.user_id IN ( " +
                "SELECT siguiendo_id FROM Seguir WHERE seguidor_id = ? UNION " +
                "SELECT ? ) GROUP BY " +
                "p.pub_id ORDER BY p.fecha_y_hora DESC";


        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, idUserLogueado);
            ps.setLong(2, idUserLogueado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Publicacion publicacion = new Publicacion();
                publicacion.setId(rs.getLong("pub_id"));
                publicacion.setContenidoPublicacion(rs.getString("contenido"));
                publicacion.setFechaPublicacion(rs.getString("fecha_y_hora"));
                publicacion.setCreadorPublicacion(rs.getString("username"));
                publicacion.setCantidadMeGusta(rs.getLong("cantidad_likes"));
                publicacion.setCantidadComentarios(rs.getLong("cantidad_comentarios"));

                String comentariosStr = rs.getString("comentarios");

                List<Comentarios> comentarios = new ArrayList<>();
                if (comentariosStr != null) {
                    String[] comentariosArray = comentariosStr.split(",");
                    for (String comentarioStr : comentariosArray) {
                        Comentarios comentario = new Comentarios();
                        // Asumimos que el formato del comentario es 'com_id - fecha_y_hora - contenido - user_id - nombre_user'
                        String[] comentarioParts = comentarioStr.split(" - ");
                        comentario.setCom_id(Long.parseLong(comentarioParts[0]));
                        comentario.setFecha(comentarioParts[1]);
                        comentario.setContenido(comentarioParts[2]);
                        comentario.setUser_id(Long.parseLong(comentarioParts[3]));
                        comentario.setNombreUser(comentarioParts[4]);

                        comentarios.add(comentario);
                    }
                }
                publicacion.setComentarios(comentarios);


                publicaciones.add(publicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicaciones;
    }


    @Override
    public void agregarComentario(Comentarios comentarios) {
        Connection connection = getConnection();
        String sql = "INSERT INTO Comentario (contenido, pub_id, user_id, fecha_y_hora) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, comentarios.getContenido());
            ps.setLong(2, comentarios.getPub_id());
            ps.setLong(3, comentarios.getUser_id());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al intentar Agg con la base de datos para comentar " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión " + e.getMessage());
            }
        }
    }

    @Override
    public void darLike(Long userLogueado, Long idPubli) {
        Connection connection = getConnection();
        String checkSql = "SELECT COUNT(*) FROM UserLike WHERE pub_id = ? AND user_id = ?";
        String insertSql = "INSERT INTO UserLike (pub_id, user_id) VALUES (?, ?)";

        try {
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setLong(1, idPubli);
            checkStmt.setLong(2, userLogueado);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("El usuario ya dio like a esta publicación.");
                return; // Salir si ya existe un like
            }

            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setLong(1, idPubli);
            insertStmt.setLong(2, userLogueado);
            insertStmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al dar like: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    @Override
    public void quitarLike(Long userLogueado, Long idPubli) {
        Connection connection = getConnection();
        String sql = "DELETE FROM UserLike WHERE pub_id = ? AND user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idPubli);
            ps.setLong(2, userLogueado);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al intentar eliminar el like: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión: " + e.getMessage());
            }
        }
    }

    @Override
    public Map<String, Object> cantidadLikes(Long idPubli) {
        Connection connection = getConnection();
        String sqlCantidad = "SELECT COUNT(*) AS total_likes FROM UserLike WHERE pub_id = ?";
        String sqlUsuarios = "SELECT u.username FROM UserLike ul " +
                "JOIN Usuario u ON ul.user_id = u.user_id " +
                "WHERE ul.pub_id = ?";

        Map<String, Object> resultado = new HashMap<>();

        try (PreparedStatement psCantidad = connection.prepareStatement(sqlCantidad);
             PreparedStatement psUsuarios = connection.prepareStatement(sqlUsuarios)) {

            // Obtener la cantidad de likes
            psCantidad.setLong(1, idPubli);
            try (ResultSet rsCantidad = psCantidad.executeQuery()) {
                if (rsCantidad.next()) {
                    resultado.put("totalLikes", rsCantidad.getInt("total_likes"));
                }
            }

            // Obtener los usernames de los usuarios que dieron like
            psUsuarios.setLong(1, idPubli);
            List<String> usernames = new ArrayList<>();
            try (ResultSet rsUsuarios = psUsuarios.executeQuery()) {
                while (rsUsuarios.next()) {
                    usernames.add(rsUsuarios.getString("username"));
                }
            }
            resultado.put("usernames", usernames);

        } catch (Exception e) {
            System.out.println("Error al obtener likes y usuarios: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }

        return resultado;
    }

    @Override
    public void eliminarComentario(Long comId) {
        Connection connection = getConnection();
        String sql = "DELETE FROM Comentario WHERE com_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, comId); // Establece el ID del comentario a eliminar
            ps.executeUpdate();
            System.out.println("Comentario eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al intentar eliminar el comentario: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error al intentar cerrar la conexión: " + e.getMessage());
            }
        }
    }


}

