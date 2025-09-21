package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class MovimentacaoEstoque {

    private long idMovimentacao;
    private LocalDate dataMovimentacao;
    private String tipoMovimentacao;
    private long idLab;
    private long idItem;
    private Long idQRCode;
    private long idFuncionario;
    private float qntde;
    private String obs;

    public MovimentacaoEstoque(long idMovimentacao, LocalDate dataMovimentacao, String tipoMovimentacao, long idLab, long idItem, Long idQRCode, long idFuncionario, float qntde, String obs) {
        this.idMovimentacao = idMovimentacao;
        this.dataMovimentacao = dataMovimentacao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.idLab = idLab;
        this.idItem = idItem;
        this.idQRCode = idQRCode;
        this.idFuncionario = idFuncionario;
        this.qntde = qntde;
        this.obs = obs;
    }

    public MovimentacaoEstoque(LocalDate dataMovimentacao, String tipoMovimentacao, long idLab, long idItem, Long idQRCode, long idFuncionario, float qntde, String obs) {
        this.idMovimentacao = idMovimentacao;
        this.dataMovimentacao = dataMovimentacao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.idLab = idLab;
        this.idItem = idItem;
        this.idQRCode = idQRCode;
        this.idFuncionario = idFuncionario;
        this.qntde = qntde;
        this.obs = obs;
    }

    public long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public LocalDate getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDate dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
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

    public Long getIdQRCode() {
        return idQRCode;
    }

    public void setIdQRCode(Long idQRCode) {
        this.idQRCode = idQRCode;
    }

    public long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public float getQntde() {
        return qntde;
    }

    public void setQntde(float qntde) {
        this.qntde = qntde;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
