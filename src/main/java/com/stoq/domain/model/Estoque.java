package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Estoque {

    private long idEstoque;
    private long idLab;
    private long idMaterial;
    private LocalDate dia;
    private float quantidadeAtual;

    public Estoque(long idEstoque, long idLab, long idMaterial, LocalDate dia, float quantidadeAtual) {
        this.idEstoque = idEstoque;
        this.idLab = idLab;
        this.idMaterial = idMaterial;
        this.dia = dia;
        this.quantidadeAtual = quantidadeAtual;
    }

    public Estoque(long idLab, long idMaterial, LocalDate dia, float quantidadeAtual) {
        this.idLab = idLab;
        this.idMaterial = idMaterial;
        this.dia = dia;
        this.quantidadeAtual = quantidadeAtual;
    }

    public long getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(long idEstoque) {
        this.idEstoque = idEstoque;
    }

    public long getIdLab() {
        return idLab;
    }

    public void setIdLab(long idLab) {
        this.idLab = idLab;
    }

    public long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public float getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(float quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    @Override
    public String toString() {
        return "Estoque {\n" +
                "  idEstoque=" + idEstoque + ",\n" +
                "  idLab=" + idLab + ",\n" +
                "  idMaterial=" + idMaterial + ",\n" +
                "  dia=" + dia + ",\n" +
                "  quantidadeAtual=" + quantidadeAtual + "\n" +
                "}";
    }


}
