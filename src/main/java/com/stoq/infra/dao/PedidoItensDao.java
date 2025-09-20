package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.PedidoItens;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoItensDao {

    // CREATE
    public void insert(PedidoItens item) {
        String sql = "INSERT INTO PEDIDO_ITENS (PEDIDO_ID, MATERIAL_ID, QTDE_SOLICITADA, QTDE_RECEBIDA, PRECO_UNITARIO, LOTE, VALIDADE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, item.getIdPedido());
            stmt.setLong(2, item.getIdMaterial());
            stmt.setFloat(3, item.getQtdeSolicitada());
            stmt.setFloat(4, item.getQntdeRecebida());
            stmt.setFloat(5, item.getPrecoUnitario());
            stmt.setString(6, item.getLote());
            if (item.getValidador() != null) {
                stmt.setDate(7, Date.valueOf(item.getValidador()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item do pedido", e);
        }
    }

    // READ BY ID (chave composta)
    public PedidoItens findById(Long idPedido, Long idMaterial) {
        String sql = "SELECT * FROM PEDIDO_ITENS WHERE PEDIDO_ID = ? AND MATERIAL_ID = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPedido);
            stmt.setLong(2, idMaterial);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PedidoItens(
                        rs.getLong("PEDIDO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getFloat("QTDE_SOLICITADA"),
                        rs.getFloat("QTDE_RECEBIDA"),
                        rs.getFloat("PRECO_UNITARIO"),
                        rs.getString("LOTE"),
                        rs.getDate("VALIDADE") != null ? rs.getDate("VALIDADE").toLocalDate() : null
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item do pedido", e);
        }
        return null;
    }

    // READ ALL
    public List<PedidoItens> findAll() {
        List<PedidoItens> lista = new ArrayList<>();
        String sql = "SELECT * FROM PEDIDO_ITENS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PedidoItens item = new PedidoItens(
                        rs.getLong("PEDIDO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getFloat("QTDE_SOLICITADA"),
                        rs.getFloat("QTDE_RECEBIDA"),
                        rs.getFloat("PRECO_UNITARIO"),
                        rs.getString("LOTE"),
                        rs.getDate("VALIDADE") != null ? rs.getDate("VALIDADE").toLocalDate() : null
                );
                lista.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens de pedido", e);
        }
        return lista;
    }

    // UPDATE
    public void update(PedidoItens item) {
        String sql = "UPDATE PEDIDO_ITENS SET QTDE_SOLICITADA=?, QTDE_RECEBIDA=?, PRECO_UNITARIO=?, LOTE=?, VALIDADE=? " +
                "WHERE PEDIDO_ID=? AND MATERIAL_ID=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, item.getQtdeSolicitada());
            stmt.setFloat(2, item.getQntdeRecebida());
            stmt.setFloat(3, item.getPrecoUnitario());
            stmt.setString(4, item.getLote());
            if (item.getValidador() != null) {
                stmt.setDate(5, Date.valueOf(item.getValidador()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setLong(6, item.getIdPedido());
            stmt.setLong(7, item.getIdMaterial());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item do pedido", e);
        }
    }

    // DELETE
    public void delete(Long idPedido, Long idMaterial) {
        String sql = "DELETE FROM PEDIDO_ITENS WHERE PEDIDO_ID=? AND MATERIAL_ID=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPedido);
            stmt.setLong(2, idMaterial);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar item do pedido", e);
        }
    }
}
