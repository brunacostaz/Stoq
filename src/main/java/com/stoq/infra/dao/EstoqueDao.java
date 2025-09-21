package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;

public class EstoqueDao {

    // Atualiza o saldo do estoque (se não existir, cria uma linha nova)
    public void atualizarEstoque(long idLab, long idMaterial, float qtde) {
        String sqlUpdate = "UPDATE ESTOQUE SET QTDE = QTDE + ? WHERE LABORATORIO_ID = ? AND MATERIAL_ID = ? AND DIA = TRUNC(SYSDATE)";
        String sqlInsert = "INSERT INTO ESTOQUE (LABORATORIO_ID, MATERIAL_ID, DIA, QTDE) VALUES (?, ?, TRUNC(SYSDATE), ?)";

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
}
