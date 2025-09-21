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
    private Long idLaboratorio;

    // Construtor SEM ID (usado para INSERT)
    public Funcionario(String nome, String cpf, String email, String cargo, String ativo, LocalDate dtCadastro, Long idLaboratorio) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.ativo = ativo;
        this.dtCadastro = dtCadastro;
        this.idLaboratorio = idLaboratorio;
    }

    // Construtor COM ID (usado para SELECT)
    public Funcionario(Long idFuncionario, String nome, String cpf, String email, String cargo, String ativo, LocalDate dtCadastro, Long idLaboratorio) {
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

    public Long getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(Long idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    @Override
    public String toString() {
        return "Funcionario {\n" +
                "  idFuncionario=" + idFuncionario + ",\n" +
                "  nome='" + nome + "',\n" +
                "  cpf='" + cpf + "',\n" +
                "  email='" + email + "',\n" +
                "  cargo='" + cargo + "',\n" +
                "  ativo='" + ativo + "',\n" +
                "  dtCadastro=" + dtCadastro + ",\n" +
                "  idLaboratorio=" + idLaboratorio + "\n" +
                "}";
    }


}
