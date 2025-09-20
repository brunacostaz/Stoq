package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Preset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresetDao {

    // CREATE
    public void insert(Preset preset) {
        String sql = "INSERT INTO PRESETS (NOME, CODIGO, DESCRICAO, ATIVO) VALUES (?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, preset.getNome());
            stmt.setString(2, preset.getCodigo());
            stmt.setString(3, preset.getDescricao());
            stmt.setString(4, preset.getAtivo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir preset", e);
        }
    }

    // READ BY ID
    public Preset findById(Long id) {
        String sql = "SELECT * FROM PRESETS WHERE ID_PRESET = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Preset(
                        rs.getLong("ID_PRESET"),
                        rs.getString("NOME"),
                        rs.getString("CODIGO"),
                        rs.getString("DESCRICAO"),
                        rs.getString("ATIVO")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar preset por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Preset> findAll() {
        List<Preset> lista = new ArrayList<>();
        String sql = "SELECT * FROM PRESETS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Preset preset = new Preset(
                        rs.getLong("ID_PRESET"),
                        rs.getString("NOME"),
                        rs.getString("CODIGO"),
                        rs.getString("DESCRICAO"),
                        rs.getString("ATIVO")
                );
                lista.add(preset);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar presets", e);
        }
        return lista;
    }

    // UPDATE
    public void update(Preset preset) {
        String sql = "UPDATE PRESETS SET NOME=?, CODIGO=?, DESCRICAO=?, ATIVO=? WHERE ID_PRESET=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, preset.getNome());
            stmt.setString(2, preset.getCodigo());
            stmt.setString(3, preset.getDescricao());
            stmt.setString(4, preset.getAtivo());
            stmt.setLong(5, preset.getIdPreset());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar preset", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM PRESETS WHERE ID_PRESET=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar preset", e);
        }
    }
}
