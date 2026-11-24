package br.com.senai.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    private Connection con;

    private final String DB_PATH = "jdbc:mysql://localhost:3306/cadastro_pacientes";
    private final String USERNAME = "root";
    private final String PASSWORD = "Canil21@@";

    // Construtor: Abre a conexão assim que a classe é instanciada
    public MySQLConnect() {
        openConnect();
    }

    public boolean openConnect() {
        try {
            con = DriverManager.getConnection(DB_PATH, USERNAME, PASSWORD);
            System.out.println("Conectado com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados!" + e.getMessage());
            return false;
        }
    }

    public void closeConnect() {
        try {
            con.close();
            System.out.println("Usuario desconectado!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados!" + e.getMessage());
            throw new RuntimeException(e);
        }

    }
    public Connection getCon() {
        try {
            // Se a conexão não existir ou tiver sido fechada, abre de novo!
            if (con == null || con.isClosed()) {
                openConnect();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao checar conexão: " + e.getMessage());
        }
        return con;
    }
}
