package br.com.senai.model;

import br.com.senai.driver.MySQLConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    private final MySQLConnect DB_CONNECT;

    public PacienteDAO(MySQLConnect DB_CONNECT) {
        this.DB_CONNECT = DB_CONNECT;
    }

    //criar amigo
    public boolean create(Paciente amigo) throws SQLException {
        Connection con = DB_CONNECT.getCon();
        if (con == null) {
            return false;
        }
        String query = "INSERT INTO pacientes (nome, telefone, email, data_nascimento, genero) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, amigo.getNome());
            ps.setString(2, amigo.getTelefone());
            ps.setString(3, amigo.getEmail());
            ps.setDate(4, amigo.getData_nascimento());
            ps.setString(5, amigo.getGenero().name());

            int linhasmodificadas = ps.executeUpdate();
            System.out.println("DAO: Cadastro realizado com sucesso!");

            return linhasmodificadas > 0;
        }
    }

    //ler lista de amigo
    public List<Paciente> read() throws SQLException {
        List<Paciente> amigos = new ArrayList<>();
        Connection con = DB_CONNECT.getCon();
        if (con == null) {
            return amigos;
        }
        String query = "SELECT * FROM pacientes";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                java.sql.Date dataNascimento = rs.getDate("data_nascimento");

                // Converte a String do banco para o Enum Genero com segurança
                String generoStr = rs.getString("genero");
                Genero genero = null;
                if (generoStr != null && !generoStr.isEmpty()) {
                    genero = Genero.valueOf(generoStr.toUpperCase());
                }

                Paciente paciente = new Paciente(id, nome, telefone, email, dataNascimento, genero);
                amigos.add(paciente); // ou pacientes.add(paciente);
            }
            return amigos;
        }
    }

    // editar amigo
    public boolean update(Paciente amigo) throws SQLException {
        Connection con = DB_CONNECT.getCon();
        if (con == null) {
            return false;
        }
        String query = "UPDATE pacientes SET nome = ?, telefone =?, email = ?, data_nascimento = ?, genero = ? WHERE id = ?;";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, amigo.getNome());
            ps.setString(2, amigo.getTelefone());
            ps.setString(3, amigo.getEmail());
            ps.setDate(4, amigo.getData_nascimento());
            ps.setString(5, amigo.getGenero().name());
            ps.setInt(6, amigo.getId());

            int linhasmodificadas = ps.executeUpdate();
            System.out.println("DAO: Atualização realizada com sucesso!");
            return linhasmodificadas > 0;
        }
    }

    // deletar amigo
    public boolean delete(int id) throws SQLException {
        Connection con = DB_CONNECT.getCon();
        if (con == null) {
            return false;
        }

        String query = "DELETE FROM pacientes WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            int linhasmodificadas = ps.executeUpdate();
            System.out.println("DAO: Exclusao realizada com sucesso!");
            return linhasmodificadas > 0;
        }
    }

    //Detalhar amigo
    // Detalhar / Buscar paciente por ID
    public Paciente readById(Integer id) throws SQLException {
        Connection con = DB_CONNECT.getCon();
        if (con == null) {
            return null;
        }

        String query = "SELECT id, nome, telefone, email, data_nascimento, genero FROM pacientes WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int pacienteId = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String telefone = rs.getString("telefone");
                    String email = rs.getString("email");
                    java.sql.Date dataNasc = rs.getDate("data_nascimento");

                    String generoStr = rs.getString("genero");
                    Genero genero = null;
                    if (generoStr != null && !generoStr.trim().isEmpty()) {
                        genero = Genero.valueOf(generoStr.toUpperCase());
                    }

                    return new Paciente(pacienteId, nome, telefone, email, dataNasc, genero);
                }
            }
        }

        // Retorna null caso não encontre nenhum paciente com o ID informado
        return null;
    }
}
