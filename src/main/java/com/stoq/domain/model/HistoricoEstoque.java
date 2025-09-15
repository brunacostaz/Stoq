package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class HistoricoEstoque {

    private long idHist;
    private LocalDate dataEntrada;
    private long idLab;
    private int qtdeInicial;
    private int qtdeEntradas;
    private int qtdeSaidas;
    private int qtdeFinal;

    public HistoricoEstoque(long idHist, LocalDate dataEntrada, long idLab, int qtdeInicial, int qtdeEntradas, int qtdeSaidas, int qtdeFinal) {
        this.idHist = idHist;
        this.dataEntrada = dataEntrada;
        this.idLab = idLab;
        this.qtdeInicial = qtdeInicial;
        this.qtdeEntradas = qtdeEntradas;
        this.qtdeSaidas = qtdeSaidas;
        this.qtdeFinal = qtdeFinal;
    }

    public long getIdHist() {
        return idHist;
    }

    public void setIdHist(long idHist) {
        this.idHist = idHist;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public long getIdLab() {
        return idLab;
    }

    public void setIdLab(long idLab) {
        this.idLab = idLab;
    }

    public int getQtdeInicial() {
        return qtdeInicial;
    }

    public void setQtdeInicial(int qtdeInicial) {
        this.qtdeInicial = qtdeInicial;
    }

    public int getQtdeEntradas() {
        return qtdeEntradas;
    }

    public void setQtdeEntradas(int qtdeEntradas) {
        this.qtdeEntradas = qtdeEntradas;
    }

    public int getQtdeSaidas() {
        return qtdeSaidas;
    }

    public void setQtdeSaidas(int qtdeSaidas) {
        this.qtdeSaidas = qtdeSaidas;
    }

    public int getQtdeFinal() {
        return qtdeFinal;
    }

    public void setQtdeFinal(int qtdeFinal) {
        this.qtdeFinal = qtdeFinal;
    }
}
