package main.java.com.stoq.infra.dao;

import main.java.com.stoq.domain.model.MovimentacaoEstoque;
import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<MovimentacaoEstoque> findByLaboratorio(long laboratorioId) {
        List<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = """
        SELECT ID_MOVIMENTACAO, DIA_MOVIMENTACAO, LABORATORIO_ID, MATERIAL_ID, 
               TIPO, QTDE, QRCODE_ID, FUNCIONARIO_ID, OBS
        FROM MOVIMENTACAO
        WHERE LABORATORIO_ID = ?
        ORDER BY DIA_MOVIMENTACAO DESC
    """;

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, laboratorioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MovimentacaoEstoque mov = new MovimentacaoEstoque(
                        rs.getLong("ID_MOVIMENTACAO"),
                        rs.getDate("DIA_MOVIMENTACAO").toLocalDate(),
                        rs.getString("TIPO"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("MATERIAL_ID"),
                        rs.getLong("QRCODE_ID"),
                        rs.getLong("FUNCIONARIO_ID"),
                        rs.getFloat("QTDE"),
                        rs.getString("OBS")
                );
                lista.add(mov);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar movimentações", e);
        }

        return lista;
    }

}
