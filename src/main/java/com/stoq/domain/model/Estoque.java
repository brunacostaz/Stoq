package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Estoque {

    private long idEstoque;
    private long idLab;
    private long idItem;
    private LocalDate dia;
    private int quantidadeAtual;
    private String idLote;

    public Estoque(long idEstoque, long idLab, long idItem, LocalDate dia, int quantidadeAtual, String idLote) {
        this.idEstoque = idEstoque;
        this.idLab = idLab;
        this.idItem = idItem;
        this.dia = dia;
        this.quantidadeAtual = quantidadeAtual;
        this.idLote = idLote;
    }

    public long getId() {
        return idEstoque;
    }

    public void setId(long id) {
        this.idEstoque = id;
    }

    public long getIdLab() {
        return idLab;
    }

    public void setIdLab(long idLab) {
        this.idLab = idLab;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getIdLote() {
        return idLote;
    }

    public void setIdLote(String idLote) {
        this.idLote = idLote;
    }
}
