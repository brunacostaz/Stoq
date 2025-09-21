package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;

public class MovimentacaoEstoqueDao {

    public void insert(long idLab, long idMaterial, String tipo, float qtde,
                       Long idQRCode, long idFuncionario, String obs) {

        String sql = "INSERT INTO MOVIMENTACAO (LABORATORIO_ID, MATERIAL_ID, TIPO, QTDE, " +
                "QRCODE_ID, FUNCIONARIO_ID, OBS, DIA_MOVIMENTACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idLab);
            stmt.setLong(2, idMaterial);
            stmt.setString(3, tipo);
            stmt.setFloat(4, qtde);

            if (idQRCode != null) {
                stmt.setLong(5, idQRCode);
            } else {
                stmt.setNull(5, Types.BIGINT);
            }

            stmt.setLong(6, idFuncionario);
            stmt.setString(7, obs);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir movimentação", e);
        }
    }
}

