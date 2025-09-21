package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class QRCode {

    private long idQRCode;
    private long idConsulta;
    private long idEnfermeiro;
    private long idAdminValidador;
    private long idLaboratorio;
    private String codigo;
    private String status;
    private LocalDate dtGeracao;
    private LocalDate dtValidacao;

    public QRCode(long idQRCode, long idConsulta, long idEnfermeiro, long idAdminValidador, long idLaboratorio, String codigo, String status, LocalDate dtGeracao, LocalDate dtValidacao) {
        this.idQRCode = idQRCode;
        this.idConsulta = idConsulta;
        this.idEnfermeiro = idEnfermeiro;
        this.idAdminValidador = idAdminValidador;
        this.idLaboratorio = idLaboratorio;
        this.codigo = codigo;
        this.status = status;
        this.dtGeracao = dtGeracao;
        this.dtValidacao = dtValidacao;
    }

    public QRCode(long idConsulta, long idEnfermeiro, long idAdminValidador, long idLaboratorio, String codigo, String status, LocalDate dtGeracao, LocalDate dtValidacao) {
        this.idConsulta = idConsulta;
        this.idEnfermeiro = idEnfermeiro;
        this.idAdminValidador = idAdminValidador;
        this.idLaboratorio = idLaboratorio;
        this.codigo = codigo;
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

    public long getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(long idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDtGeracao() {
        return dtGeracao;
    }

    public void setDtGeracao(LocalDate dtGeracao) {
        this.dtGeracao = dtGeracao;
    }

    public LocalDate getDtValidacao() {
        return dtValidacao;
    }

    public void setDtValidacao(LocalDate dtValidacao) {
        this.dtValidacao = dtValidacao;
    }

    @Override
    public String toString() {
        return "QRCode {\n" +
                "  idQRCode=" + idQRCode + ",\n" +
                "  idConsulta=" + idConsulta + ",\n" +
                "  idEnfermeiro=" + idEnfermeiro + ",\n" +
                "  idAdminValidador=" + idAdminValidador + ",\n" +
                "  idLaboratorio=" + idLaboratorio + ",\n" +
                "  codigo='" + codigo + "',\n" +
                "  status='" + status + "',\n" +
                "  dtGeracao=" + dtGeracao + ",\n" +
                "  dtValidacao=" + dtValidacao + "\n" +
                "}";
    }

}
