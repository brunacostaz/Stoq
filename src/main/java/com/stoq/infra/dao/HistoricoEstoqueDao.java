package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class HistoricoEstoqueDao {

    /**
     * Copia os saldos atuais da tabela ESTOQUE para o HISTORICO_ESTOQUE,
     * gerando um consolidado diário
     *
     * @param dia Data do fechamento (normalmente LocalDate.now()).
     */
    public void copiarEstoqueParaHistorico(LocalDate dia) {
        String sql = "INSERT INTO HISTORICO_ESTOQUE (" +
                "DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID, " +
                "QTDE_INICIAL, QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL) " +
                "SELECT ?, e.LABORATORIO_ID, e.MATERIAL_ID, " +
                "0, 0, 0, 0, e.QTDE " +
                "FROM ESTOQUE e " +
                "WHERE e.DIA = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.sql.Date sqlDate = java.sql.Date.valueOf(dia);
            stmt.setDate(1, sqlDate); // DIA_HISTORICO
            stmt.setDate(2, sqlDate); // WHERE DIA = ?

            int rows = stmt.executeUpdate();
            System.out.println("Histórico consolidado: " + rows + " linhas inseridas para o dia " + dia);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consolidar estoque no histórico", e);
        }
    }
}
