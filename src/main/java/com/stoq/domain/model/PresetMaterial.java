package main.java.com.stoq.domain.model;

public class PresetMaterial {

    private long idPreset;
    private long idItem;
    private float qtdePorExame;

    public PresetMaterial(long idPreset, long idItem, float qtdePorExame) {
        this.idPreset = idPreset;
        this.idItem = idItem;
        this.qtdePorExame = qtdePorExame;
    }

    public PresetMaterial(long idItem, float qtdePorExame) {
        this.idItem = idItem;
        this.qtdePorExame = qtdePorExame;
    }

    public long getIdPreset() {
        return idPreset;
    }

    public void setIdPreset(long idPreset) {
        this.idPreset = idPreset;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }

    public float getQtdePorExame() {
        return qtdePorExame;
    }

    public void setQtdePorExame(float qtdePorExame) {
        this.qtdePorExame = qtdePorExame;
    }
}
