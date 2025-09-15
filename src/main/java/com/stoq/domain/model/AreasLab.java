package main.java.com.stoq.domain.model;

public class AreasLab {

    private long idArea;
    private String nome;
    private String descricao;

    public AreasLab(long idArea, String nome, String descricao) {
        this.idArea = idArea;
        this.nome = nome;
        this.descricao = descricao;
    }

    public long getIdArea() {
        return idArea;
    }

    public void setIdArea(long idArea) {
        this.idArea = idArea;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
