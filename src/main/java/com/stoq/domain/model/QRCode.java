package main.java.com.stoq.domain.model;

import java.time.LocalDateTime;

public class QRCode {

    private long idQRCode;
    private long idConsulta;
    private long idEnfermeiro;
    private long idAdminValidador;
    private long idLab;
    private long codigoQR;
    private String status;
    private LocalDateTime dtGeracao;
    private LocalDateTime dtValidacao;

    public QRCode(long idQRCode, long idConsulta, long idEnfermeiro, long idAdminValidador, long idLab, long codigoQR, String status, LocalDateTime dtGeracao, LocalDateTime dtValidacao) {
        this.idQRCode = idQRCode;
        this.idConsulta = idConsulta;
        this.idEnfermeiro = idEnfermeiro;
        this.idAdminValidador = idAdminValidador;
        this.idLab = idLab;
        this.codigoQR = codigoQR;
        this.status = status;
        this.dtGeracao = dtGeracao;
        this.dtValidacao = dtValidacao;
    }

    public long getIdQRCode() {
        return idQRCode;
    }

    public void setIdQRCode(long idQRCode) {
        this.idQRCode = idQRCode;
    }

    public long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public long getIdEnfermeiro() {
        return idEnfermeiro;
    }

    public void setIdEnfermeiro(long idEnfermeiro) {
        this.idEnfermeiro = idEnfermeiro;
    }

    public long getIdAdminValidador() {
        return idAdminValidador;
    }

    public void setIdAdminValidador(long idAdminValidador) {
        this.idAdminValidador = idAdminValidador;
    }

    public long getIdLab() {
        return idLab;
    }

    public void setIdLab(long idLab) {
        this.idLab = idLab;
    }

    public long getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(long codigoQR) {
        this.codigoQR = codigoQR;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDtGeracao() {
        return dtGeracao;
    }

    public void setDtGeracao(LocalDateTime dtGeracao) {
        this.dtGeracao = dtGeracao;
    }

    public LocalDateTime getDtValidacao() {
        return dtValidacao;
    }

    public void setDtValidacao(LocalDateTime dtValidacao) {
        this.dtValidacao = dtValidacao;
    }
}
