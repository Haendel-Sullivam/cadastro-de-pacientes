package br.com.senai;

import br.com.senai.view.TelaPaciente;
import javax.swing.SwingUtilities;

public class CadastroDePacientesApplication {

    public static void main(String[] args) {
        // Executa a interface gráfica Swing na thread correta
        SwingUtilities.invokeLater(() -> {
            TelaPaciente tela = new TelaPaciente();
            tela.setVisible(true); // Exibe a janela na tela
        });
    }
}