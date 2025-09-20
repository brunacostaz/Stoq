package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.PresetMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresetMaterialDao {

    // CREATE
    public void insert(PresetMaterial pm) {
        String sql = "INSERT INTO PRESET_MATERIAIS (PRESET_ID, MATERIAL_ID, QTDE_POR_EXAME) VALUES (?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pm.getIdPreset());
            stmt.setLong(2, pm.getIdItem()); // aqui é MATERIAL_ID no banco
            stmt.setFloat(3, pm.getQtdePorExame());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir PresetMaterial", e);
        }
    }

    // READ BY ID (preset + material)
    public PresetMaterial findById(Long idPreset, Long idMaterial) {
        String sql = "SELECT * FROM PRESET_MATERIAIS WHERE PRESET_ID=? AND MATERIAL_ID=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPreset);
            stmt.setLong(2, idMaterial);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PresetMaterial(
                        rs.getLong("PRESET_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getFloat("QTDE_POR_EXAME")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar PresetMaterial", e);
        }
        return null;
    }

    // READ ALL
    public List<PresetMaterial> findAll() {
        List<PresetMaterial> lista = new ArrayList<>();
        String sql = "SELECT * FROM PRESET_MATERIAIS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PresetMaterial pm = new PresetMaterial(
                        rs.getLong("PRESET_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getFloat("QTDE_POR_EXAME")
                );
                lista.add(pm);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar PresetMateriais", e);
        }
        return lista;
    }

    // UPDATE
    public void update(PresetMaterial pm) {
        String sql = "UPDATE PRESET_MATERIAIS SET QTDE_POR_EXAME=? WHERE PRESET_ID=? AND MATERIAL_ID=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, pm.getQtdePorExame());
            stmt.setLong(2, pm.getIdPreset());
            stmt.setLong(3, pm.getIdItem());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar PresetMaterial", e);
        }
    }

    // DELETE
    public void delete(Long idPreset, Long idMaterial) {
        String sql = "DELETE FROM PRESET_MATERIAIS WHERE PRESET_ID=? AND MATERIAL_ID=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPreset);
            stmt.setLong(2, idMaterial);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar PresetMaterial", e);
        }
    }
}
