package br.com.senai.controller;

import br.com.senai.model.Paciente;
import br.com.senai.model.PacienteDAO;

import java.sql.SQLException;
import java.util.List;

public class PacienteController {

    private final PacienteDAO AMIGO_DAO;

    public PacienteController(PacienteDAO AMIGO_DAO) {
        this.AMIGO_DAO = AMIGO_DAO;
    }

    // Cadastrar
    public boolean cadastrarAmigo(Paciente amigo) throws SQLException {
        return AMIGO_DAO.create(amigo);
    }

    // Listar
    public List<Paciente> listarAmigos() throws SQLException {
        return AMIGO_DAO.read();
    }

    // Atualizar
    public boolean atualizarAmigo(Paciente amigo) throws SQLException {
        return AMIGO_DAO.update(amigo);
    }

    // Excluir
    public boolean excluirAmigo(int id) throws SQLException {
        return AMIGO_DAO.delete(id);
    }

    // Detalhar (Buscar por ID)
    public Paciente detalharAmigo(Integer id) throws SQLException {
        return AMIGO_DAO.readById(id);
    }
}
