package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.MovimentacaoEstoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoEstoqueDao {

    // CREATE
    public void insert(MovimentacaoEstoque mov) {
        String sql = "INSERT INTO MOVIMENTACAO (DIA_MOVIMENTACAO, LABORATORIO_ID, MATERIAL_ID, TIPO, QTDE, QRCODE_ID, FUNCIONARIO_ID, OBS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(mov.getDataMovimentacao()));
            stmt.setLong(2, mov.getIdLab());
            stmt.setLong(3, mov.getIdItem());
            stmt.setString(4, mov.getTipoMovimentacao());
            stmt.setFloat(5, mov.getQntde());
            stmt.setLong(6, mov.getIdQRCode());
            stmt.setLong(7, mov.getIdFuncionario());
            stmt.setString(8, mov.getObs());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir movimentação", e);
        }
    }

    // READ BY ID
    public MovimentacaoEstoque findById(Long id) {
        String sql = "SELECT * FROM MOVIMENTACAO WHERE ID_MOVIMENTACAO = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new MovimentacaoEstoque(
                        rs.getLong("ID_MOVIMENTACAO"),
                        rs.getDate("DIA_MOVIMENTACAO").toLocalDate(),
                        rs.getString("TIPO"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getLong("QRCODE_ID"),
                        rs.getLong("FUNCIONARIO_ID"),
                        rs.getFloat("QTDE"),
                        rs.getString("OBS")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar movimentação por id", e);
        }
        return null;
    }

    // READ ALL
    public List<MovimentacaoEstoque> findAll() {
        List<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM MOVIMENTACAO";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MovimentacaoEstoque mov = new MovimentacaoEstoque(
                        rs.getLong("ID_MOVIMENTACAO"),
                        rs.getDate("DIA_MOVIMENTACAO").toLocalDate(),
                        rs.getString("TIPO"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getLong("QRCODE_ID"),
                        rs.getLong("FUNCIONARIO_ID"),
                        rs.getFloat("QTDE"),
                        rs.getString("OBS")
                );
                lista.add(mov);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar movimentações", e);
        }
        return lista;
    }

    // UPDATE
    public void update(MovimentacaoEstoque mov) {
        String sql = "UPDATE MOVIMENTACAO SET DIA_MOVIMENTACAO=?, LABORATORIO_ID=?, MATERIAL_ID=?, TIPO=?, QTDE=?, QRCODE_ID=?, FUNCIONARIO_ID=?, OBS=? " +
                "WHERE ID_MOVIMENTACAO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(mov.getDataMovimentacao()));
            stmt.setLong(2, mov.getIdLab());
            stmt.setLong(3, mov.getIdItem());
            stmt.setString(4, mov.getTipoMovimentacao());
            stmt.setFloat(5, mov.getQntde());
            stmt.setLong(6, mov.getIdQRCode());
            stmt.setLong(7, mov.getIdFuncionario());
            stmt.setString(8, mov.getObs());
            stmt.setLong(9, mov.getIdMovimentacao());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar movimentação", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM MOVIMENTACAO WHERE ID_MOVIMENTACAO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar movimentação", e);
        }
    }
}
