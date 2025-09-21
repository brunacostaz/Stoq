package main.java.com.stoq.domain.model;

import java.time.LocalDate;

public class Consulta {

    private Long idConsulta;
    private String pacienteNome;
    private String pacienteDoc;
    private LocalDate dataHora;
    private String status;
    private String  obs;
    private LocalDate dtCriacao;
    private long idLab;
    private long idPreset;

    public Consulta(Long idConsulta, String pacienteNome, String pacienteDoc, LocalDate dataHora, String status, String obs, LocalDate dtCriacao, long idLab, long idPreset) {
        this.idConsulta = idConsulta;
        this.pacienteNome = pacienteNome;
        this.pacienteDoc = pacienteDoc;
        this.dataHora = dataHora;
        this.status = status;
        this.obs = obs;
        this.dtCriacao = dtCriacao;
        this.idLab = idLab;
        this.idPreset = idPreset;
    }

    public Consulta(String pacienteNome, String pacienteDoc, LocalDate dataHora, String status, String obs, LocalDate dtCriacao, long idLab, long idPreset) {
        this.pacienteNome = pacienteNome;
        this.pacienteDoc = pacienteDoc;
        this.dataHora = dataHora;
        this.status = status;
        this.obs = obs;
        this.dtCriacao = dtCriacao;
        this.idLab = idLab;
        this.idPreset = idPreset;
    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public String getPacienteDoc() {
        return pacienteDoc;
    }

    public void setPacienteDoc(String pacienteDoc) {
        this.pacienteDoc = pacienteDoc;
    }

    public LocalDate getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDate dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public LocalDate getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDate dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public long getIdLab() {
        return idLab;
    }

    public void setIdLab(long idLab) {
        this.idLab = idLab;
    }

    public long getIdPreset() {
        return idPreset;
    }

    public void setIdPreset(long idPreset) {
        this.idPreset = idPreset;
    }

    @Override
    public String toString() {
        return "Consulta {\n" +
                "  idConsulta=" + idConsulta + ",\n" +
                "  pacienteNome='" + pacienteNome + "',\n" +
                "  pacienteDoc='" + pacienteDoc + "',\n" +
                "  dataHora=" + dataHora + ",\n" +
                "  status='" + status + "',\n" +
                "  obs='" + obs + "',\n" +
                "  dtCriacao=" + dtCriacao + ",\n" +
                "  idLab=" + idLab + ",\n" +
                "  idPreset=" + idPreset + "\n" +
                "}";
    }

}
