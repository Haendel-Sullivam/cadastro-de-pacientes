package br.com.senai.view;

import br.com.senai.controller.PacienteController;
import br.com.senai.driver.MySQLConnect;
import br.com.senai.model.Paciente;
import br.com.senai.model.PacienteDAO;
import br.com.senai.model.Genero;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class PacienteView {
    public static void mostrarMenu () throws SQLException {
        Scanner sc = new Scanner(System.in);
        MySQLConnect dbConnect = new MySQLConnect();
        if (!dbConnect.openConnect()) {
            System.out.println("O sistema nao esta conectado com o banco de dados!");
            return;
        }
        PacienteDAO amigoDAO = new PacienteDAO(dbConnect);
        PacienteController amigoController = new PacienteController(amigoDAO);

        while (true) {
            System.out.println("Cadastro de Pacientes:");
            System.out.println("Menu:");
            System.out.println("0 - Sair do Programa");
            System.out.println("1 - Cadastrar Paciente");
            System.out.println("2 - Listar Pacientes");
            System.out.println("3 - Atualizar Dados");
            System.out.println("4 - Excluir Paciente");
            System.out.println("5 - Detalhar Paciente");
            System.out.println("Digite sua Opcao:");
            int opcao = sc.nextInt();
            sc.nextLine();
            switch (opcao) {
                case 0 -> {
                    System.out.println("Programa Finalizado!");
                    dbConnect.closeConnect();
                    sc.close();
                    System.exit(0);
                }
                case 1 -> {
                    System.out.println("Informe o nome do Paciente:");
                    String nome = sc.nextLine();

                    System.out.println("Informe o telefone do Paciente:");
                    String telefone = sc.nextLine();

                    System.out.println("Informe o email do Paciente:");
                    String email = sc.next();
                    sc.nextLine();

                    System.out.println("Informe a data de nascimento do Paciente: (yyyy-MM-DD)");
                    Date dataNascimento = Date.valueOf(sc.next());
                    sc.nextLine();

                    System.out.println("Informe o genero do Paciente: (MASCULINO/FEMININO)");
                    Genero genero  = Genero.valueOf(sc.next().toUpperCase());
                    sc.nextLine();

                    Paciente amigo = new Paciente(null, nome, telefone, email, dataNascimento, genero);
                    amigoController.cadastrarAmigo(amigo);
                }
                case 2 -> amigoController.listarAmigos();
                case 3 -> {
                    System.out.println("Informe o id do Paciente que deseja atualizar:");
                    Integer id = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Atualize o nome do Paciente:");
                    String nome = sc.nextLine();

                    System.out.println("Atualize o telefone do Paciente:");
                    String telefone = sc.nextLine();

                    System.out.println("Atualize o email do Paciente:");
                    String email = sc.next();
                    sc.nextLine();

                    System.out.println("Atualize a data de nascimento do Paciente: (yyyy-MM-DD)");
                    Date dataNascimento = Date.valueOf(sc.next());
                    sc.nextLine();

                    System.out.println("Atualize o genero do Paciente: (MASCULINO/FEMININO)");
                    Genero genero  = Genero.valueOf(sc.next().toUpperCase());
                    sc.nextLine();

                    Paciente amigo = new Paciente(id, nome, telefone, email, dataNascimento, genero);
                    amigoController.atualizarAmigo(amigo);
                }
                case 4 -> {
                    System.out.println("Informe o id do Paciente que deseja Excluir");
                    Integer id = sc.nextInt();
                    sc.nextLine();

                    amigoController.excluirAmigo(id);
                }
                case 5 -> {
                    System.out.println("Informe o id do Paciente que deseja detalhar:");

                    Integer id = sc.nextInt();
                    sc.nextLine();

                    amigoController.detalharAmigo(id);

                }
            }
        }
    }

}
