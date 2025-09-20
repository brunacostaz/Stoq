package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class HistoricoEstoque {

    private long idHistorico;
    private LocalDate diaHistorico;
    private long idLaboratorio;
    private float qtdeInicial;
    private float qtdeEntradas;
    private float qtdeSaidas;
    private float qtdeAjustes;
    private float qtdeFinal;

    public HistoricoEstoque(long idHistorico, LocalDate diaHistorico, long idLaboratorio, float qtdeInicial, float qtdeEntradas, float qtdeSaidas, float qtdeAjustes, float qtdeFinal) {
        this.idHistorico = idHistorico;
        this.diaHistorico = diaHistorico;
        this.idLaboratorio = idLaboratorio;
        this.qtdeInicial = qtdeInicial;
        this.qtdeEntradas = qtdeEntradas;
        this.qtdeSaidas = qtdeSaidas;
        this.qtdeAjustes = qtdeAjustes;
        this.qtdeFinal = qtdeFinal;
    }

    public long getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(long idHistorico) {
        this.idHistorico = idHistorico;
    }

    public LocalDate getDiaHistorico() {
        return diaHistorico;
    }

    public void setDiaHistorico(LocalDate diaHistorico) {
        this.diaHistorico = diaHistorico;
    }

    public long getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(long idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public float getQtdeInicial() {
        return qtdeInicial;
    }

    public void setQtdeInicial(float qtdeInicial) {
        this.qtdeInicial = qtdeInicial;
    }

    public float getQtdeEntradas() {
        return qtdeEntradas;
    }

    public void setQtdeEntradas(float qtdeEntradas) {
        this.qtdeEntradas = qtdeEntradas;
    }

    public float getQtdeSaidas() {
        return qtdeSaidas;
    }

    public void setQtdeSaidas(float qtdeSaidas) {
        this.qtdeSaidas = qtdeSaidas;
    }

    public float getQtdeAjustes() {
        return qtdeAjustes;
    }

    public void setQtdeAjustes(float qtdeAjustes) {
        this.qtdeAjustes = qtdeAjustes;
    }

    public float getQtdeFinal() {
        return qtdeFinal;
    }

    public void setQtdeFinal(float qtdeFinal) {
        this.qtdeFinal = qtdeFinal;
    }
}
