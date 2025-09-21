package main.java.com.stoq.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Preset {

    private long idPreset;
    private String nome;
    private String codigo;
    private String descricao;
    private String ativo;

    public Preset() {
    }

    public Preset(long idPreset, String nome, String codigo, String descricao, String ativo) {
        this.idPreset = idPreset;
        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Preset(String nome, String codigo, String descricao, String ativo) {
        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.ativo = ativo;
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

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Preset {\n" +
                "  idPreset=" + idPreset + ",\n" +
                "  nome='" + nome + "',\n" +
                "  codigo='" + codigo + "',\n" +
                "  descricao='" + descricao + "',\n" +
                "  ativo='" + ativo + "'\n" +
                "}";
    }



}
