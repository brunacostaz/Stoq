package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;

public class HistoricoEstoqueDao {

    // Atualiza valores parciais do dia no histórico
    public void registrarMovimentacao(long idLab, long idMaterial, float qtde) {
        String sqlUpdate = "UPDATE HISTORICO_ESTOQUE " +
                "SET QTDE_FINAL = QTDE_FINAL + ?, " +
                "    QTDE_ENTRADAS = CASE WHEN ? > 0 THEN QTDE_ENTRADAS + ? ELSE QTDE_ENTRADAS END, " +
                "    QTDE_SAIDAS   = CASE WHEN ? < 0 THEN QTDE_SAIDAS   + ABS(?) ELSE QTDE_SAIDAS END, " +
                "    QTDE_AJUSTES  = CASE WHEN ? = 0 THEN QTDE_AJUSTES + 0 ELSE QTDE_AJUSTES END " +
                "WHERE DIA_HISTORICO = TRUNC(SYSDATE) AND LABORATORIO_ID = ? AND MATERIAL_ID = ?";

        String sqlInsert = "INSERT INTO HISTORICO_ESTOQUE " +
                "(DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID, QTDE_INICIAL, " +
                " QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL) " +
                "VALUES (TRUNC(SYSDATE), ?, ?, 0, ?, ?, 0, ?)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setFloat(1, qtde);
            stmt.setFloat(2, qtde);
            stmt.setFloat(3, qtde);
            stmt.setFloat(4, qtde);
            stmt.setFloat(5, qtde);
            stmt.setFloat(6, qtde);
            stmt.setLong(7, idLab);
            stmt.setLong(8, idMaterial);

            int rows = stmt.executeUpdate();

            if (rows == 0) { // se não existia histórico hoje, insere
                try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                    insertStmt.setLong(1, idLab);
                    insertStmt.setLong(2, idMaterial);
                    insertStmt.setFloat(3, qtde > 0 ? qtde : 0); // entradas
                    insertStmt.setFloat(4, qtde < 0 ? Math.abs(qtde) : 0); // saídas
                    insertStmt.setFloat(5, qtde); // saldo final
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar movimentação no histórico", e);
        }
    }

    // Fecha o dia (caso queira rodar em batch no final do dia)
    public void fecharDia(java.time.LocalDate dia) {
        // Aqui você pode implementar uma query que copia saldo do ESTOQUE para HISTORICO_ESTOQUE
        // Exemplo: INSERT INTO HISTORICO_ESTOQUE (...) SELECT ... FROM ESTOQUE WHERE DIA = ?
    }
}
