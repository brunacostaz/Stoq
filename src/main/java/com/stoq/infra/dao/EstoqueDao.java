package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Estoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDao {

    // CREATE
    public void insert(Estoque estoque) {
        String sql = "INSERT INTO ESTOQUE (LABORATORIO_ID, MATERIAL_ID, DIA, QTDE) VALUES (?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, estoque.getIdLab());
            stmt.setLong(2, estoque.getIdMaterial());
            stmt.setDate(3, Date.valueOf(estoque.getDia()));
            stmt.setFloat(4, estoque.getQuantidadeAtual());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir estoque", e);
        }
    }

    // READ BY ID
    public Estoque findById(Long id) {
        String sql = "SELECT * FROM ESTOQUE WHERE ID_ESTOQUE = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Estoque(
                        rs.getLong("ID_ESTOQUE"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getDate("DIA").toLocalDate(),
                        rs.getFloat("QTDE")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar estoque por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Estoque> findAll() {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM ESTOQUE";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estoque est = new Estoque(
                        rs.getLong("ID_ESTOQUE"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getDate("DIA").toLocalDate(),
                        rs.getFloat("QTDE")
                );
                lista.add(est);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar estoques", e);
        }
        return lista;
    }

    // UPDATE
    public void update(Estoque estoque) {
        String sql = "UPDATE ESTOQUE SET LABORATORIO_ID=?, MATERIAL_ID=?, DIA=?, QTDE=? WHERE ID_ESTOQUE=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, estoque.getIdLab());
            stmt.setLong(2, estoque.getIdMaterial());
            stmt.setDate(3, Date.valueOf(estoque.getDia()));
            stmt.setFloat(4, estoque.getQuantidadeAtual());
            stmt.setLong(5, estoque.getIdEstoque());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM ESTOQUE WHERE ID_ESTOQUE=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar estoque", e);
        }
    }
}
