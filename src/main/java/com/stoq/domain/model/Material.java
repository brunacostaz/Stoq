package main.java.com.stoq.domain.model;

public class Material {

    private long idMaterial ;
    private String nome;
    private String idLote;
    private String unidadeMedida;
    private int estoqueMinimo;
    private String descricao;
    private String ativo;

    public Material(long idMaterial, String nome, String idLote, String unidadeMedida, int estoqueMinimo, String descricao, String ativo) {
        this.idMaterial = idMaterial;
        this.nome = nome;
        this.idLote = idLote;
        this.unidadeMedida = unidadeMedida;
        this.estoqueMinimo = estoqueMinimo;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Material(String nome, String idLote, String unidadeMedida, int estoqueMinimo, String descricao, String ativo) {
        this.nome = nome;
        this.idLote = idLote;
        this.unidadeMedida = unidadeMedida;
        this.estoqueMinimo = estoqueMinimo;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdLote() {
        return idLote;
    }

    public void setIdLote(String idLote) {
        this.idLote = idLote;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
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
}
