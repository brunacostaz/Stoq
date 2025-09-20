package main.java.com.stoq.infra.dao;

import main.java.com.stoq.domain.model.HistoricoEstoque;
import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoEstoqueDao {

    // CREATE (registrar qualquer movimentação)
    public void insert(HistoricoEstoque historico) {
        String sql = "INSERT INTO HISTORICO_ESTOQUE " +
                "(DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID, " +
                "QTDE_INICIAL, QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(historico.getDiaHistorico()));
            stmt.setLong(2, historico.getIdLaboratorio());
            stmt.setLong(3, historico.getIdMaterial());
            stmt.setFloat(4, historico.getQtdeInicial());
            stmt.setFloat(5, historico.getQtdeEntradas());
            stmt.setFloat(6, historico.getQtdeSaidas());
            stmt.setFloat(7, historico.getQtdeAjustes());
            stmt.setFloat(8, historico.getQtdeFinal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir histórico de estoque", e);
        }
    }

    // READ ID
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
                        rs.getLong("MATERIAL_ID"),
                        rs.getFloat("QTDE_INICIAL"),
                        rs.getFloat("QTDE_ENTRADAS"),
                        rs.getFloat("QTDE_SAIDAS"),
                        rs.getFloat("QTDE_AJUSTES"),
                        rs.getFloat("QTDE_FINAL")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico por ID", e);
        }
        return null;
    }

    // READ ALL
    public List<HistoricoEstoque> findAll() {
        List<HistoricoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM HISTORICO_ESTOQUE ORDER BY DIA_HISTORICO DESC";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HistoricoEstoque h = new HistoricoEstoque(
                        rs.getLong("ID_HISTORICO"),
                        rs.getDate("DIA_HISTORICO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getFloat("QTDE_INICIAL"),
                        rs.getFloat("QTDE_ENTRADAS"),
                        rs.getFloat("QTDE_SAIDAS"),
                        rs.getFloat("QTDE_AJUSTES"),
                        rs.getFloat("QTDE_FINAL")
                );
                lista.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos de estoque", e);
        }
        return lista;
    }

    // DELETE (se precisar remover algo)
    public void delete(Long id) {
        String sql = "DELETE FROM HISTORICO_ESTOQUE WHERE ID_HISTORICO = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar histórico de estoque", e);
        }
    }
}
