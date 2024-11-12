package com.red.conecta2.BasesDatos.Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionSqlite {
    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Conecta2.db");
        } catch (Exception e) {
            System.out.println("Error al intentar conectarte a la base de datos " + e.getMessage());
        }
        return connection;
    };
}
