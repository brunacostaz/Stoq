package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Administrador extends Funcionario {

    private String nivelAcesso;

    public Administrador(long idFuncionario, String nome, String cpf, String email, String cargo, String ativo, LocalDate dtCadastro, long idLab, String nivelAcesso) {
        super(idFuncionario, nome, cpf, email, cargo, ativo, dtCadastro, idLab);
        this.nivelAcesso = nivelAcesso;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}
