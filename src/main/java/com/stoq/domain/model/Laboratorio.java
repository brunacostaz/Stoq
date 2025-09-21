package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Laboratorio {

    private Long idLab;
    private String nome;
    private String codigo;
    private String ativo;   // 'S' ou 'N'
    private LocalDate dtCadastro;

    public Laboratorio(Long idLab, String nome, String codigo, String ativo, LocalDate dtCadastro) {
        this.idLab = idLab;
        this.nome = nome;
        this.codigo = codigo;
        this.ativo = ativo;
        this.dtCadastro = dtCadastro;
    }

    public Laboratorio(String nome, String codigo, String ativo, LocalDate dtCadastro) {
        this.nome = nome;
        this.codigo = codigo;
        this.ativo = ativo;
        this.dtCadastro = dtCadastro;
    }

    public Long getIdLab() {
        return idLab;
    }

    public void setIdLab(Long idLab) {
        this.idLab = idLab;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    @Override
    public String toString() {
        return "{\n" +
                "  \"idLab\": " + idLab + ",\n" +
                "  \"nome\": \"" + nome + "\",\n" +
                "  \"codigo\": \"" + codigo + "\",\n" +
                "  \"ativo\": \"" + ativo + "\",\n" +
                "  \"dtCadastro\": \"" + dtCadastro + "\"\n" +
                "}";
    }

}
