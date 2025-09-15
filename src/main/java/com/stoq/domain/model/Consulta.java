package main.java.com.stoq.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Consulta {

    private Long idConsulta;
    private String pacienteDoc;
    private LocalDateTime dataHora;
    private String status;
    private Long obs;
    private LocalDate dtCriacao;
    private long idLab;
    private long idPreset;

    public Consulta(Long idConsulta, String pacienteDoc, LocalDateTime dataHora, String status, Long obs, LocalDate dtCriacao, long idLab, long idPreset) {
        this.idConsulta = idConsulta;
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

    public String getPacienteDoc() {
        return pacienteDoc;
    }

    public void setPacienteDoc(String pacienteDoc) {
        this.pacienteDoc = pacienteDoc;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getObs() {
        return obs;
    }

    public void setObs(Long obs) {
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
}
