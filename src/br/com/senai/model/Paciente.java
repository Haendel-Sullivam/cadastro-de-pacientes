package br.com.senai.model;


import java.sql.Date;

public class Paciente {

    private Integer id;
    private String nome;
    private String telefone;
    private String email;
    private Date data_nascimento;
    private Genero genero;

    public Paciente() {

    }
    public Paciente(Integer id, String nome, String telefone, String email, Date data_nascimento, Genero genero) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.genero = genero;

    }

    public Paciente(Integer id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public Genero getGenero() {
        return genero;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Amigo: " +
                "\nId: " + id +
                "\nNome: " + nome +
                "\nTelefone: " + telefone +
                "\nEmail: " + email +
                "\nData nascimento: " + data_nascimento +
                "\nGenero: " + genero + "\n";
    }
}
