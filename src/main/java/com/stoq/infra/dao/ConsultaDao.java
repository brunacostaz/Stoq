package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Consulta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDao {

    // CREATE
    public void insert(Consulta consulta) {
        String sql = "INSERT INTO CONSULTAS " +
                "(PACIENTE_NOME, PACIENTE_DOC, DATA_HORA, STATUS, OBS, DT_CRIACAO, LABORATORIO_ID, PRESET_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, consulta.getPacienteNome());
            stmt.setString(2, consulta.getPacienteDoc());
            stmt.setDate(3, consulta.getDataHora() != null ? Date.valueOf(consulta.getDataHora()) : null);
            stmt.setString(4, consulta.getStatus());
            stmt.setString(5, consulta.getObs());
            stmt.setDate(6, consulta.getDtCriacao() != null ? Date.valueOf(consulta.getDtCriacao()) : null);
            stmt.setLong(7, consulta.getIdLab());
            stmt.setLong(8, consulta.getIdPreset());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir consulta", e);
        }
    }

    // READ BY ID
    public Consulta findById(Long id) {
        String sql = "SELECT * FROM CONSULTAS WHERE ID_CONSULTA = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Consulta(
                        rs.getLong("ID_CONSULTA"),
                        rs.getString("PACIENTE_NOME"),
                        rs.getString("PACIENTE_DOC"),
                        rs.getDate("DATA_HORA").toLocalDate(),
                        rs.getString("STATUS"),
                        rs.getString("OBS"),
                        rs.getDate("DT_CRIACAO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("PRESET_ID")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar consulta por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Consulta> findAll() {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT * FROM CONSULTAS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        rs.getLong("ID_CONSULTA"),
                        rs.getString("PACIENTE_NOME"),
                        rs.getString("PACIENTE_DOC"),
                        rs.getDate("DATA_HORA").toLocalDate(),
                        rs.getString("STATUS"),
                        rs.getString("OBS"),
                        rs.getDate("DT_CRIACAO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID"),
                        rs.getLong("PRESET_ID")
                );
                lista.add(consulta);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas", e);
        }
        return lista;
    }

    // UPDATE
    public void update(Consulta consulta) {
        String sql = "UPDATE CONSULTAS SET PACIENTE_NOME=?, PACIENTE_DOC=?, DATA_HORA=?, STATUS=?, OBS=?, DT_CRIACAO=?, LABORATORIO_ID=?, PRESET_ID=? " +
                "WHERE ID_CONSULTA=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, consulta.getPacienteNome());
            stmt.setString(2, consulta.getPacienteDoc());
            stmt.setDate(3, consulta.getDataHora() != null ? Date.valueOf(consulta.getDataHora()) : null);
            stmt.setString(4, consulta.getStatus());
            stmt.setString(5, consulta.getObs());
            stmt.setDate(6, consulta.getDtCriacao() != null ? Date.valueOf(consulta.getDtCriacao()) : null);
            stmt.setLong(7, consulta.getIdLab());
            stmt.setLong(8, consulta.getIdPreset());
            stmt.setLong(9, consulta.getIdConsulta());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar consulta", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM CONSULTAS WHERE ID_CONSULTA=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar consulta", e);
        }
    }
}
