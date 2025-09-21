package main.java.com.stoq.infra.dao;

import main.java.com.stoq.domain.model.Material;
import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDao {

    // Atualiza o saldo do estoque (se não existir, cria uma linha nova)
    public void atualizarEstoque(long idLab, long idMaterial, float qtde) {
        String sqlUpdate = "UPDATE ESTOQUE SET QTDE = QTDE + ? " +
                "WHERE LABORATORIO_ID = ? AND MATERIAL_ID = ? AND DIA = TRUNC(SYSDATE)";
        String sqlInsert = "INSERT INTO ESTOQUE (LABORATORIO_ID, MATERIAL_ID, DIA, QTDE) " +
                "VALUES (?, ?, TRUNC(SYSDATE), ?)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setFloat(1, qtde);
            stmt.setLong(2, idLab);
            stmt.setLong(3, idMaterial);

            int rows = stmt.executeUpdate();

            if (rows == 0) { // se não tinha estoque hoje, insere
                try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                    insertStmt.setLong(1, idLab);
                    insertStmt.setLong(2, idMaterial);
                    insertStmt.setFloat(3, qtde);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque", e);
        }
    }

    // Consulta a quantidade atual de um material no laboratório
    public float getQtdeAtual(long idLab, long idMaterial) {
        String sql = "SELECT QTDE FROM ESTOQUE " +
                "WHERE LABORATORIO_ID = ? AND MATERIAL_ID = ? " +
                "ORDER BY DIA DESC FETCH FIRST 1 ROWS ONLY";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idLab);
            stmt.setLong(2, idMaterial);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("QTDE");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar quantidade atual do estoque", e);
        }

        return 0; // se não encontrar, considera 0
    }

    public void limparDiasAntigos(LocalDate diaAtual) {
        String sql = "DELETE FROM ESTOQUE WHERE DIA < ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(diaAtual));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar dias antigos do estoque", e);
        }
    }

    // Busca materiais que estão próximos do vencimento
    public List<Material> findMateriaisProximosVencimento(long laboratorioId, int diasAviso) {
        String sql = """
        SELECT m.* 
        FROM MATERIAIS m
        JOIN PEDIDO_ITENS pi ON m.ID_MATERIAL = pi.MATERIAL_ID
        JOIN PEDIDOS p ON pi.PEDIDO_ID = p.ID_PEDIDO
        WHERE p.LABORATORIO_ID = ?
          AND pi.VALIDADE IS NOT NULL
          AND pi.VALIDADE <= (SYSDATE + ?)
    """;

        List<Material> lista = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, laboratorioId);
            stmt.setInt(2, diasAviso);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Material material = new Material(
                        rs.getLong("ID_MATERIAL"),
                        rs.getString("NOME"),
                        rs.getString("ID_LOTE"),
                        rs.getString("UNIDADE_MEDIDA"),
                        rs.getInt("ESTOQUE_MINIMO"),
                        rs.getString("DESCRICAO"),
                        rs.getString("ATIVO")
                );
                lista.add(material);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar materiais próximos do vencimento", e);
        }

        return lista;
    }

}
