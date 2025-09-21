package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.QRCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QRCodeDao {

    // CREATE

    public void insert(QRCode qr) {
        String sql = "INSERT INTO QRCODE " +
                "(CONSULTA_ID, ENFERMEIRO_ID, ADMIN_VALIDADOR_ID, LABORATORIO_ID, CODIGO, STATUS, DT_GERACAO, DT_VALIDACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID_QRCODE INTO ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, qr.getIdConsulta());
            stmt.setLong(2, qr.getIdEnfermeiro());
            stmt.setObject(3, qr.getIdAdminValidador() == 0 ? null : qr.getIdAdminValidador());
            stmt.setLong(4, qr.getIdLaboratorio());
            stmt.setString(5, qr.getCodigo());
            stmt.setString(6, qr.getStatus());
            stmt.setDate(7, java.sql.Date.valueOf(qr.getDtGeracao()));
            stmt.setDate(8, qr.getDtValidacao() == null ? null : java.sql.Date.valueOf(qr.getDtValidacao()));

            // Registrar parâmetro de saída para pegar o ID gerado
            ((oracle.jdbc.OraclePreparedStatement) stmt).registerReturnParameter(9, java.sql.Types.BIGINT);

            stmt.executeUpdate();

            try (ResultSet rs = ((oracle.jdbc.OraclePreparedStatement) stmt).getReturnResultSet()) {
                if (rs.next()) {
                    qr.setIdQRCode(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir QRCode", e);
        }
    }

//    public void insert(QRCode qr) {
//        String sql = "INSERT INTO QRCODE (CONSULTA_ID, ENFERMEIRO_ID, ADMIN_VALIDADOR_ID, LABORATORIO_ID, CODIGO, STATUS, DT_GERACAO, DT_VALIDACAO) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//        try (Connection conn = OracleConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setLong(1, qr.getIdConsulta());
//            stmt.setLong(2, qr.getIdEnfermeiro());
//            if (qr.getIdAdminValidador() != 0) {
//                stmt.setLong(3, qr.getIdAdminValidador());
//            } else {
//                stmt.setNull(3, Types.BIGINT);
//            }
//            stmt.setLong(4, qr.getIdLaboratorio());
//            stmt.setString(5, qr.getCodigo());
//            stmt.setString(6, qr.getStatus());
//            stmt.setDate(7, qr.getDtGeracao() != null ? Date.valueOf(qr.getDtGeracao()) : null);
//            stmt.setDate(8, qr.getDtValidacao() != null ? Date.valueOf(qr.getDtValidacao()) : null);
//
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Erro ao inserir QRCode", e);
//        }
//    }

    // READ BY ID
    public QRCode findById(Long id) {
        String sql = "SELECT * FROM QRCODE WHERE ID_QRCODE=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new QRCode(
                        rs.getLong("ID_QRCODE"),
                        rs.getLong("CONSULTA_ID"),
                        rs.getLong("ENFERMEIRO_ID"),
                        rs.getLong("ADMIN_VALIDADOR_ID"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getString("CODIGO"),
                        rs.getString("STATUS"),
                        rs.getDate("DT_GERACAO").toLocalDate(),
                        rs.getDate("DT_VALIDACAO") != null ? rs.getDate("DT_VALIDACAO").toLocalDate() : null
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar QRCode por id", e);
        }
        return null;
    }

    // READ ALL
    public List<QRCode> findAll() {
        List<QRCode> lista = new ArrayList<>();
        String sql = "SELECT * FROM QRCODE";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                QRCode qr = new QRCode(
                        rs.getLong("ID_QRCODE"),
                        rs.getLong("CONSULTA_ID"),
                        rs.getLong("ENFERMEIRO_ID"),
                        rs.getLong("ADMIN_VALIDADOR_ID"),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getString("CODIGO"),
                        rs.getString("STATUS"),
                        rs.getDate("DT_GERACAO").toLocalDate(),
                        rs.getDate("DT_VALIDACAO") != null ? rs.getDate("DT_VALIDACAO").toLocalDate() : null
                );
                lista.add(qr);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar QRCodes", e);
        }
        return lista;
    }

    // UPDATE
    public void update(QRCode qr) {
        String sql = "UPDATE QRCODE SET CONSULTA_ID=?, ENFERMEIRO_ID=?, ADMIN_VALIDADOR_ID=?, LABORATORIO_ID=?, CODIGO=?, STATUS=?, DT_GERACAO=?, DT_VALIDACAO=? " +
                "WHERE ID_QRCODE=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, qr.getIdConsulta());
            stmt.setLong(2, qr.getIdEnfermeiro());
            if (qr.getIdAdminValidador() != 0) {
                stmt.setLong(3, qr.getIdAdminValidador());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }
            stmt.setLong(4, qr.getIdLaboratorio());
            stmt.setString(5, qr.getCodigo());
            stmt.setString(6, qr.getStatus());
            stmt.setDate(7, qr.getDtGeracao() != null ? Date.valueOf(qr.getDtGeracao()) : null);
            stmt.setDate(8, qr.getDtValidacao() != null ? Date.valueOf(qr.getDtValidacao()) : null);
            stmt.setLong(9, qr.getIdQRCode());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar QRCode", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM QRCODE WHERE ID_QRCODE=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar QRCode", e);
        }
    }
}
