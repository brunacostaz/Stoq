package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Funcionario {

    private long idFuncionario;
    private String nome;
    private String cpf;
    private String email;
    private String cargo;
    private String ativo;
    private LocalDate dtCadastro;
    private long idLaboratorio;

    public Funcionario(long idFuncionario, String nome, String cpf, String email, String cargo, String ativo, LocalDate dtCadastro, long idLaboratorio) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.ativo = ativo;
        this.dtCadastro = dtCadastro;
        this.idLaboratorio = idLaboratorio;
    }

    public long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public long getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(long idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }
}
