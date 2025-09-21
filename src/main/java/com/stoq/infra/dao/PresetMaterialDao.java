package main.java.com.stoq.infra.dao;

import main.java.com.stoq.domain.model.PedidoItens;
import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresetMaterialDao {

    /**
     * Retorna apenas IDs dos materiais (usado para listar no front)
     */
    public List<Long> findMateriaisByPresetId(Long presetId) {
        List<Long> materiais = new ArrayList<>();
        String sql = "SELECT ID_ITEM FROM PRESET_MATERIAL WHERE PRESET_ID = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, presetId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                materiais.add(rs.getLong("ID_ITEM"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar materiais do preset", e);
        }

        return materiais;
    }

    /**
     * Retorna materiais + quantidade configurada no preset
     */
    public List<PedidoItens> findMateriaisComQtde(Long presetId) {
        List<PedidoItens> itens = new ArrayList<>();
        String sql = "SELECT PRESET_ID, ID_ITEM, QTDE_POR_EXAME " +
                "FROM PRESET_MATERIAL WHERE PRESET_ID = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, presetId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PedidoItens item = new PedidoItens(
                        rs.getLong("PRESET_ID"),
                        rs.getLong("ID_ITEM"),
                        rs.getFloat("QTDE_POR_EXAME"),
                        0,
                        0,
                        null,
                        null
                );
                itens.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar materiais e quantidades do preset", e);
        }

        return itens;
    }
}
