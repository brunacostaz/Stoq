package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Laboratorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioDao {

    // CREATE
    public void insert(Laboratorio lab) {
        String sql = "INSERT INTO LABORATORIO (NOME, CODIGO, ATIVO, DT_CADASTRO) VALUES (?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lab.getNome());
            stmt.setString(2, lab.getCodigo());
            stmt.setString(3, lab.getAtivo());
            stmt.setDate(4, lab.getDtCadastro() != null ? Date.valueOf(lab.getDtCadastro()) : null);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir laboratório", e);
        }
    }

    // READ BY ID
    public Laboratorio findById(Long id) {
        String sql = "SELECT * FROM LABORATORIO WHERE ID_LABORATORIO = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Laboratorio(
                        rs.getLong("ID_LABORATORIO"),
                        rs.getString("NOME"),
                        rs.getString("CODIGO"),
                        rs.getString("ATIVO"),
                        rs.getDate("DT_CADASTRO").toLocalDate()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar laboratório por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Laboratorio> findAll() {
        List<Laboratorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM LABORATORIO";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Laboratorio lab = new Laboratorio(
                        rs.getLong("ID_LABORATORIO"),
                        rs.getString("NOME"),
                        rs.getString("CODIGO"),
                        rs.getString("ATIVO"),
                        rs.getDate("DT_CADASTRO").toLocalDate()
                );
                lista.add(lab);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar laboratórios", e);
        }
        return lista;
    }

    // UPDATE
    public void update(Laboratorio lab) {
        String sql = "UPDATE LABORATORIO SET NOME=?, CODIGO=?, ATIVO=?, DT_CADASTRO=? WHERE ID_LABORATORIO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lab.getNome());
            stmt.setString(2, lab.getCodigo());
            stmt.setString(3, lab.getAtivo());
            stmt.setDate(4, lab.getDtCadastro() != null ? Date.valueOf(lab.getDtCadastro()) : null);
            stmt.setLong(5, lab.getIdLab());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar laboratório", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM LABORATORIO WHERE ID_LABORATORIO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar laboratório", e);
        }
    }
}
