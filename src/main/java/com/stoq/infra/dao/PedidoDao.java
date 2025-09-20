package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {

    // CREATE
    public void insert(Pedido pedido) {
        String sql = "INSERT INTO PEDIDOS (NUMERO, LABORATORIO_ID, FUNCIONARIO_ID, STATUS, DT_CRIACAO, DT_RECEBIMENTO, FORNECEDOR_NOME) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getNumero());
            stmt.setLong(2, pedido.getIdLaboratorio());
            stmt.setLong(3, pedido.getIdFuncionario());
            stmt.setString(4, pedido.getStatus());
            stmt.setDate(5, Date.valueOf(pedido.getDtCriacao()));
            if (pedido.getDtRecebimento() != null) {
                stmt.setDate(6, Date.valueOf(pedido.getDtRecebimento()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            stmt.setString(7, pedido.getFornecedorNome());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pedido", e);
        }
    }

    // READ BY ID
    public Pedido findById(Long id) {
        String sql = "SELECT * FROM PEDIDOS WHERE ID_PEDIDO = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Pedido(
                        rs.getLong("ID_PEDIDO"),
                        rs.getString("NUMERO"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("FUNCIONARIO_ID"),
                        rs.getString("STATUS"),
                        rs.getDate("DT_CRIACAO").toLocalDate(),
                        rs.getDate("DT_RECEBIMENTO") != null ? rs.getDate("DT_RECEBIMENTO").toLocalDate() : null,
                        rs.getString("FORNECEDOR_NOME")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Pedido> findAll() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM PEDIDOS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getLong("ID_PEDIDO"),
                        rs.getString("NUMERO"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("FUNCIONARIO_ID"),
                        rs.getString("STATUS"),
                        rs.getDate("DT_CRIACAO").toLocalDate(),
                        rs.getDate("DT_RECEBIMENTO") != null ? rs.getDate("DT_RECEBIMENTO").toLocalDate() : null,
                        rs.getString("FORNECEDOR_NOME")
                );
                lista.add(pedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos", e);
        }
        return lista;
    }

    // UPDATE
    public void update(Pedido pedido) {
        String sql = "UPDATE PEDIDOS SET NUMERO=?, LABORATORIO_ID=?, FUNCIONARIO_ID=?, STATUS=?, DT_CRIACAO=?, DT_RECEBIMENTO=?, FORNECEDOR_NOME=? " +
                "WHERE ID_PEDIDO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getNumero());
            stmt.setLong(2, pedido.getIdLaboratorio());
            stmt.setLong(3, pedido.getIdFuncionario());
            stmt.setString(4, pedido.getStatus());
            stmt.setDate(5, Date.valueOf(pedido.getDtCriacao()));
            if (pedido.getDtRecebimento() != null) {
                stmt.setDate(6, Date.valueOf(pedido.getDtRecebimento()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            stmt.setString(7, pedido.getFornecedorNome());
            stmt.setLong(8, pedido.getIdPedido());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pedido", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM PEDIDOS WHERE ID_PEDIDO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pedido", e);
        }
    }
}
