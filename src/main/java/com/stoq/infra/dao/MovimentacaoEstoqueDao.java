package main.java.com.stoq.infra.dao;

import main.java.com.stoq.domain.model.MovimentacaoEstoque;
import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;

public class MovimentacaoEstoqueDao {

    public void insert(MovimentacaoEstoque mov) {
        String sql = "INSERT INTO MOVIMENTACAO (LABORATORIO_ID, MATERIAL_ID, TIPO, QTDE, " +
                "QRCODE_ID, FUNCIONARIO_ID, OBS, DIA_MOVIMENTACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, mov.getIdLab());
            stmt.setLong(2, mov.getIdItem());
            stmt.setString(3, mov.getTipoMovimentacao());
            stmt.setFloat(4, mov.getQntde());

            if (mov.getIdQRCode() != null) {
                stmt.setLong(5, mov.getIdQRCode());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }

            stmt.setLong(6, mov.getIdFuncionario());
            stmt.setString(7, mov.getObs());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir movimentação", e);
        }
    }
}
