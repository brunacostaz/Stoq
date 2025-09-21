package main.java.com.stoq.infra.dao;

import main.java.com.stoq.domain.model.HistoricoEstoque;
import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoricoEstoqueDao {

    /**
     * Copia os saldos atuais da tabela ESTOQUE para o HISTORICO_ESTOQUE,
     * gerando um consolidado diário
     *
     * @param dia Data do fechamento (normalmente LocalDate.now()).
     */
    public void copiarEstoqueParaHistorico(LocalDate dia) {
        String sql = """
        MERGE INTO HISTORICO_ESTOQUE h
        USING (
            SELECT 
                ? AS DIA_HISTORICO,
                e.LABORATORIO_ID,
                e.MATERIAL_ID,
                COALESCE(SUM(CASE WHEN m.TIPO = 'ENTRADA' THEN m.QTDE END), 0) AS QTDE_ENTRADAS,
                COALESCE(SUM(CASE WHEN m.TIPO = 'SAIDA'   THEN m.QTDE END), 0) AS QTDE_SAIDAS,
                MAX(e.QTDE) AS QTDE_FINAL
            FROM ESTOQUE e
            LEFT JOIN MOVIMENTACAO m
                   ON m.LABORATORIO_ID = e.LABORATORIO_ID
                  AND m.MATERIAL_ID    = e.MATERIAL_ID
                  AND TRUNC(m.DIA_MOVIMENTACAO) = ?
            WHERE e.DIA = ?
            GROUP BY e.LABORATORIO_ID, e.MATERIAL_ID
        ) src
        ON (h.DIA_HISTORICO = src.DIA_HISTORICO
            AND h.LABORATORIO_ID = src.LABORATORIO_ID
            AND h.MATERIAL_ID    = src.MATERIAL_ID)
        WHEN MATCHED THEN
          UPDATE SET h.QTDE_FINAL   = src.QTDE_FINAL,
                     h.QTDE_ENTRADAS = src.QTDE_ENTRADAS,
                     h.QTDE_SAIDAS   = src.QTDE_SAIDAS
        WHEN NOT MATCHED THEN
          INSERT (DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID,
                  QTDE_INICIAL, QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL)
          VALUES (src.DIA_HISTORICO, src.LABORATORIO_ID, src.MATERIAL_ID,
                  0, src.QTDE_ENTRADAS, src.QTDE_SAIDAS, 0, src.QTDE_FINAL)
    """;

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.sql.Date sqlDate = java.sql.Date.valueOf(dia);

            stmt.setDate(1, sqlDate);
            stmt.setDate(2, sqlDate);
            stmt.setDate(3, sqlDate);

            int rows = stmt.executeUpdate();
            System.out.println("Histórico consolidado: " + rows + " linhas inseridas/atualizadas para o dia " + dia);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consolidar estoque no histórico", e);
        }
    }

    public List<HistoricoEstoque> findByLaboratorio(long laboratorioId) {
        List<HistoricoEstoque> lista = new ArrayList<>();
        String sql = """
        SELECT ID_HISTORICO, DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID,
               QTDE_INICIAL, QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL
        FROM HISTORICO_ESTOQUE
        WHERE LABORATORIO_ID = ?
        ORDER BY DIA_HISTORICO DESC
    """;

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, laboratorioId);
            ResultSet rs = stmt.executeQuery();

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
            throw new RuntimeException("Erro ao buscar histórico de estoque", e);
        }

        return lista;
    }


//    public void copiarEstoqueParaHistorico(LocalDate dia) {
//        String sql = "INSERT INTO HISTORICO_ESTOQUE (" +
//                "DIA_HISTORICO, LABORATORIO_ID, MATERIAL_ID, " +
//                "QTDE_INICIAL, QTDE_ENTRADAS, QTDE_SAIDAS, QTDE_AJUSTES, QTDE_FINAL) " +
//                "SELECT ?, e.LABORATORIO_ID, e.MATERIAL_ID, " +
//                "0, 0, 0, 0, e.QTDE " +
//                "FROM ESTOQUE e " +
//                "WHERE e.DIA = ?";
//
//        try (Connection conn = OracleConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            java.sql.Date sqlDate = java.sql.Date.valueOf(dia);
//            stmt.setDate(1, sqlDate); // DIA_HISTORICO
//            stmt.setDate(2, sqlDate); // WHERE DIA = ?
//
//            int rows = stmt.executeUpdate();
//            System.out.println("Histórico consolidado: " + rows + " linhas inseridas para o dia " + dia);
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Erro ao consolidar estoque no histórico", e);
//        }
//    }


}
