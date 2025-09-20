package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Funcionario {

    private Long idFuncionario;   // agora é Long (pode ser null antes do insert)
    private String nome;
    private String cpf;
    private String email;
    private String cargo;
    private String ativo;
    private LocalDate dtCadastro;
    private long idLaboratorio;

    // Construtor SEM ID (usado para INSERT)
    public Funcionario(String nome, String cpf, String email, String cargo, String ativo, LocalDate dtCadastro, long idLaboratorio) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.ativo = ativo;
        this.dtCadastro = dtCadastro;
        this.idLaboratorio = idLaboratorio;
    }

    // Construtor COM ID (usado para SELECT)
    public Funcionario(Long idFuncionario, String nome, String cpf, String email, String cargo, String ativo, LocalDate dtCadastro, long idLaboratorio) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.ativo = ativo;
        this.dtCadastro = dtCadastro;
        this.idLaboratorio = idLaboratorio;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getCargo() {
        return cargo;
    }

    public String getAtivo() {
        return ativo;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public long getIdLaboratorio() {
        return idLaboratorio;
    }
}
