package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.HistoricoEstoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoEstoqueDao {

    // CREATE
    public void insert(HistoricoEstoque hist) {
        String sql = "INSERT INTO HISTORICO_ESTOQUE " +
                "(DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID, QTDE_INICIAL, QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(hist.getDiaHistorico()));
            stmt.setLong(2, hist.getIdLaboratorio());
            // ⚠️ Aqui precisa do getIdMaterial na classe HistoricoEstoque
            // stmt.setLong(3, hist.getIdMaterial());
            stmt.setFloat(4, hist.getQtdeInicial());
            stmt.setFloat(5, hist.getQtdeEntradas());
            stmt.setFloat(6, hist.getQtdeSaidas());
            stmt.setFloat(7, hist.getQtdeAjustes());
            stmt.setFloat(8, hist.getQtdeFinal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir histórico de estoque", e);
        }
    }

    // READ BY ID
    public HistoricoEstoque findById(Long id) {
        String sql = "SELECT * FROM HISTORICO_ESTOQUE WHERE ID_HISTORICO = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new HistoricoEstoque(
                        rs.getLong("ID_HISTORICO"),
                        rs.getDate("DIA_HISTORICO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getFloat("QTDE_INICIAL"),
                        rs.getFloat("QTDE_ENTRADAS"),
                        rs.getFloat("QTDE_SAIDAS"),
                        rs.getFloat("QTDE_AJUSTES"),
                        rs.getFloat("QTDE_FINAL")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico de estoque por id", e);
        }
        return null;
    }

    // READ ALL
    public List<HistoricoEstoque> findAll() {
        List<HistoricoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM HISTORICO_ESTOQUE";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HistoricoEstoque hist = new HistoricoEstoque(
                        rs.getLong("ID_HISTORICO"),
                        rs.getDate("DIA_HISTORICO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getFloat("QTDE_INICIAL"),
                        rs.getFloat("QTDE_ENTRADAS"),
                        rs.getFloat("QTDE_SAIDAS"),
                        rs.getFloat("QTDE_AJUSTES"),
                        rs.getFloat("QTDE_FINAL")
                );
                lista.add(hist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos de estoque", e);
        }
        return lista;
    }

    // UPDATE
    public void update(HistoricoEstoque hist) {
        String sql = "UPDATE HISTORICO_ESTOQUE SET DIA_HISTORICO=?, LABORATORIO_ID=?, MATERIAL_ID=?, " +
                "QTDE_INICIAL=?, QTDE_ENTRADAS=?, QTDE_SAIDAS=?, QTDE_AJUSTES=?, QTDE_FINAL=? " +
                "WHERE ID_HISTORICO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(hist.getDiaHistorico()));
            stmt.setLong(2, hist.getIdLaboratorio());
            // ⚠️ Precisa de getIdMaterial na classe
            // stmt.setLong(3, hist.getIdMaterial());
            stmt.setFloat(4, hist.getQtdeInicial());
            stmt.setFloat(5, hist.getQtdeEntradas());
            stmt.setFloat(6, hist.getQtdeSaidas());
            stmt.setFloat(7, hist.getQtdeAjustes());
            stmt.setFloat(8, hist.getQtdeFinal());
            stmt.setLong(9, hist.getIdHistorico());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar histórico de estoque", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM HISTORICO_ESTOQUE WHERE ID_HISTORICO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar histórico de estoque", e);
        }
    }
}
