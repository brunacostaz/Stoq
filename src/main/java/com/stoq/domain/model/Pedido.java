package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Pedido {

    private long idPedido;
    private String numero;
    private long idLaboratorio;
    private long idFuncionario;
    private String status;
    private LocalDate dtCriacao;
    private LocalDate dtRecebimento;
    private String fornecedorNome;

    public Pedido(long idPedido, String numero, long idLaboratorio, long idFuncionario, String status, LocalDate dtCriacao, LocalDate dtRecebimento, String fornecedorNome) {
        this.idPedido = idPedido;
        this.numero = numero;
        this.idLaboratorio = idLaboratorio;
        this.idFuncionario = idFuncionario;
        this.status = status;
        this.dtCriacao = dtCriacao;
        this.dtRecebimento = dtRecebimento;
        this.fornecedorNome = fornecedorNome;
    }

    public Pedido( String numero, long idLaboratorio, long idFuncionario, String status, LocalDate dtCriacao, LocalDate dtRecebimento, String fornecedorNome) {
        this.idPedido = idPedido;
        this.numero = numero;
        this.idLaboratorio = idLaboratorio;
        this.idFuncionario = idFuncionario;
        this.status = status;
        this.dtCriacao = dtCriacao;
        this.dtRecebimento = dtRecebimento;
        this.fornecedorNome = fornecedorNome;
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public long getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(long idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDate dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public LocalDate getDtRecebimento() {
        return dtRecebimento;
    }

    public void setDtRecebimento(LocalDate dtRecebimento) {
        this.dtRecebimento = dtRecebimento;
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }

    @Override
    public String toString() {
        return "Pedido {\n" +
                "  idPedido=" + idPedido + ",\n" +
                "  numero='" + numero + "',\n" +
                "  idLaboratorio=" + idLaboratorio + ",\n" +
                "  idFuncionario=" + idFuncionario + ",\n" +
                "  status='" + status + "',\n" +
                "  dtCriacao=" + dtCriacao + ",\n" +
                "  dtRecebimento=" + dtRecebimento + ",\n" +
                "  fornecedorNome='" + fornecedorNome + "'\n" +
                "}";
    }


}
