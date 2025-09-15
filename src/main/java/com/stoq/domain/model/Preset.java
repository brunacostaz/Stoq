package main.java.com.stoq.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Preset {

    private long idPreset;
    private String nome;
    private String codigo;
    private String descricao;
    private boolean ativo;
    private LocalDate dtCriacao;
    private LocalDate dtAtualizacao;
    //nao seria interessante ter uma coluna com autor da atualização?


    public Preset(long idPreset, String nome, String codigo, String descricao, boolean ativo, LocalDate dtCriacao, LocalDate dtAtualizacao) {
        this.idPreset = idPreset;
        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.ativo = ativo;
        this.dtCriacao = dtCriacao;
        this.dtAtualizacao = dtAtualizacao;
    }

    public long getIdPreset() {
        return idPreset;
    }

    public void setIdPreset(long idPreset) {
        this.idPreset = idPreset;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDate dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public LocalDate getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(LocalDate dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }
}
