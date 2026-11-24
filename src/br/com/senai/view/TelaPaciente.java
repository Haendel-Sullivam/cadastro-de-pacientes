package br.com.senai.view;

import br.com.senai.controller.PacienteController;
import br.com.senai.driver.MySQLConnect;
import br.com.senai.model.Genero;
import br.com.senai.model.Paciente;
import br.com.senai.model.PacienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class TelaPaciente extends JFrame {

    // Componentes do Formulário
    private JTextField txtId, txtNome, txtTelefone, txtEmail, txtDataNasc;
    private JComboBox<Genero> cbGenero;

    // Botões
    private JButton btnSalvar, btnBuscar, btnEditar, btnDeletar, btnListar, btnLimpar;

    // Tabela
    private JTable tabelaPacientes;
    private DefaultTableModel modelTabela;

    private PacienteController controller;

    public TelaPaciente() {
        MySQLConnect db = new MySQLConnect();
        PacienteDAO dao = new PacienteDAO(db);
        controller = new PacienteController(dao);

        // Configurações básicas da Janela
        setTitle("Sistema de Gestão de Pacientes - Clínica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Painel do Formulário (Topo)
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Dados do Paciente"));

        panelForm.add(new JLabel(" ID (para busca/edição/deleção):"));
        txtId = new JTextField();
        panelForm.add(txtId);

        panelForm.add(new JLabel(" Nome:"));
        txtNome = new JTextField();
        panelForm.add(txtNome);

        panelForm.add(new JLabel(" Telefone:"));
        txtTelefone = new JTextField();
        panelForm.add(txtTelefone);

        panelForm.add(new JLabel(" E-mail:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);

        panelForm.add(new JLabel(" Data Nasc. (YYYY-MM-DD):"));
        txtDataNasc = new JTextField();
        panelForm.add(txtDataNasc);

        panelForm.add(new JLabel(" Gênero:"));
        cbGenero = new JComboBox<>(Genero.values());
        panelForm.add(cbGenero);

        // 2. Painel dos Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnSalvar = new JButton("Salvar Novo");
        btnBuscar = new JButton("Buscar (ID)");
        btnEditar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar");
        btnListar = new JButton("Listar Todos");
        btnLimpar = new JButton("Limpar Campos");

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnBuscar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnDeletar);
        panelBotoes.add(btnListar);
        panelBotoes.add(btnLimpar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotoes, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // 3. Tabela de Pacientes (Centro)
        modelTabela = new DefaultTableModel(
                new String[]{"ID", "Nome", "Telefone", "E-mail", "Data Nasc.", "Gênero"}, 0
        );
        tabelaPacientes = new JTable(modelTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaPacientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pacientes Cadastrados"));
        add(scrollPane, BorderLayout.CENTER);

        // Ação: Selecionar linha da tabela preenche o formulário
        tabelaPacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaPacientes.getSelectedRow();
                if (linha != -1) {
                    txtId.setText(modelTabela.getValueAt(linha, 0).toString());
                    txtNome.setText(modelTabela.getValueAt(linha, 1).toString());
                    txtTelefone.setText(modelTabela.getValueAt(linha, 2).toString());
                    txtEmail.setText(modelTabela.getValueAt(linha, 3).toString());
                    txtDataNasc.setText(modelTabela.getValueAt(linha, 4).toString());

                    Object genObj = modelTabela.getValueAt(linha, 5);
                    if (genObj != null) {
                        cbGenero.setSelectedItem(Genero.valueOf(genObj.toString().toUpperCase()));
                    }
                }
            }
        });

        // 4. Ações dos Botões
        btnSalvar.addActionListener(e -> salvarPaciente());
        btnBuscar.addActionListener(e -> buscarPacientePorId());
        btnEditar.addActionListener(e -> editarPaciente());
        btnDeletar.addActionListener(e -> deletarPaciente());
        btnListar.addActionListener(e -> atualizarTabela());
        btnLimpar.addActionListener(e -> limparCampos());

        // NOTA: A chamada atualizarTabela() foi REMOVIDA daqui
        // para NÃO listar automaticamente ao abrir a janela!
    }

    private void salvarPaciente() {
        try {
            Paciente paciente = extrairPacienteDoFormulario(0);
            boolean sucesso = controller.cadastrarAmigo(paciente);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarPacientePorId() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID no campo ID para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idBusca = Integer.parseInt(txtId.getText().trim());
            List<Paciente> lista = controller.listarAmigos();

            modelTabela.setRowCount(0); // Limpa tabela
            boolean encontrado = false;

            if (lista != null) {
                for (Paciente p : lista) {
                    if (p.getId() == idBusca) {
                        adicionarLinhaTabela(p);
                        txtNome.setText(p.getNome());
                        txtTelefone.setText(p.getTelefone());
                        txtEmail.setText(p.getEmail());
                        txtDataNasc.setText(p.getData_nascimento().toString());
                        if (p.getGenero() != null) cbGenero.setSelectedItem(p.getGenero());
                        encontrado = true;
                        break;
                    }
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "Nenhum paciente encontrado com o ID " + idBusca);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPaciente() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do paciente que deseja atualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(txtId.getText().trim());
            Paciente paciente = extrairPacienteDoFormulario(id);

            controller.atualizarAmigo(paciente);
            JOptionPane.showMessageDialog(this, "Paciente atualizado com sucesso!");
            limparCampos();
            atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarPaciente() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do paciente que deseja deletar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o paciente ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirma == JOptionPane.YES_OPTION) {
                // Instancia um paciente só com ID para passar ao Controller/DAO ou ajusta a chamada
                Paciente p = new Paciente(id, "", "", "", null, null);

                // Se no seu Controller o método de deletar for por ID ou Objeto:
                // controller.deletarAmigo(p); ou controller.deletarAmigo(id);
                // Exemplo chamando o DAO/Controller:
                controller.atualizarAmigo(p); // Substitua pelo seu método de deletar do controller!

                JOptionPane.showMessageDialog(this, "Paciente deletado com sucesso!");
                limparCampos();
                atualizarTabela();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        try {
            modelTabela.setRowCount(0);
            List<Paciente> lista = controller.listarAmigos();

            if (lista != null) {
                for (Paciente p : lista) {
                    adicionarLinhaTabela(p);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tabela: " + e.getMessage());
        }
    }

    private void adicionarLinhaTabela(Paciente p) {
        modelTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getTelefone(),
                p.getEmail(),
                p.getData_nascimento(),
                p.getGenero()
        });
    }

    private Paciente extrairPacienteDoFormulario(int id) {
        String nome = txtNome.getText();
        String telefone = txtTelefone.getText();
        String email = txtEmail.getText();
        Date dataNasc = Date.valueOf(txtDataNasc.getText());
        Genero genero = (Genero) cbGenero.getSelectedItem();

        return new Paciente(id, nome, telefone, email, dataNasc, genero);
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtDataNasc.setText("");
        cbGenero.setSelectedIndex(0);
    }
}