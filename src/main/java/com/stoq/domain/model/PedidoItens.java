package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class PedidoItens {

    private long idPedido;
    private long idMaterial;
    private float qtdeSolicitada;
    private float qntdeRecebida;
    private float precoUnitario;
    private String lote;
    private LocalDate validador;

    public PedidoItens(long idPedido, long idMaterial, float qtdeSolicitada, float qntdeRecebida, float precoUnitario, String lote, LocalDate validador) {
        this.idPedido = idPedido;
        this.idMaterial = idMaterial;
        this.qtdeSolicitada = qtdeSolicitada;
        this.qntdeRecebida = qntdeRecebida;
        this.precoUnitario = precoUnitario;
        this.lote = lote;
        this.validador = validador;
    }

    public PedidoItens(long idMaterial, float qtdeSolicitada, float qntdeRecebida, float precoUnitario, String lote, LocalDate validador) {
        this.idMaterial = idMaterial;
        this.qtdeSolicitada = qtdeSolicitada;
        this.qntdeRecebida = qntdeRecebida;
        this.precoUnitario = precoUnitario;
        this.lote = lote;
        this.validador = validador;
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public float getQtdeSolicitada() {
        return qtdeSolicitada;
    }

    public void setQtdeSolicitada(float qtdeSolicitada) {
        this.qtdeSolicitada = qtdeSolicitada;
    }

    public float getQntdeRecebida() {
        return qntdeRecebida;
    }

    public void setQntdeRecebida(float qntdeRecebida) {
        this.qntdeRecebida = qntdeRecebida;
    }

    public float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(float precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public LocalDate getValidador() {
        return validador;
    }

    public void setValidador(LocalDate validador) {
        this.validador = validador;
    }
}
