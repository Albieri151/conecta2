package com.red.conecta2.BasesDatos.Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConexionSqlite {
    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Conecta2.db");

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (Exception e) {
            System.out.println("Error al intentar conectarte a la base de datos jodete" + e.getMessage());
        }
        return connection;
    };
}
