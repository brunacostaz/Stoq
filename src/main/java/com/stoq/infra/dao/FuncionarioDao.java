package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDao {

    // CREATE
    public void insert(Funcionario func) {
        String sql = "INSERT INTO FUNCIONARIOS (NOME, CPF, EMAIL, CARGO, LABORATORIO_ID, ATIVO, DT_CADASTRO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, func.getNome());
            stmt.setString(2, func.getCpf());
            stmt.setString(3, func.getEmail());
            stmt.setString(4, func.getCargo());
            stmt.setLong(5, func.getIdLaboratorio());
            stmt.setString(6, func.getAtivo());
            stmt.setDate(7, func.getDtCadastro() != null ? Date.valueOf(func.getDtCadastro()) : null);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir funcionário", e);
        }
    }

    // READ BY ID
    public Funcionario findById(Long id) {
        String sql = "SELECT * FROM FUNCIONARIOS WHERE ID_FUNCIONARIO = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Funcionario(
                        rs.getLong("ID_FUNCIONARIO"),
                        rs.getString("NOME"),
                        rs.getString("CPF"),
                        rs.getString("EMAIL"),
                        rs.getString("CARGO"),
                        rs.getString("ATIVO"),
                        rs.getDate("DT_CADASTRO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM FUNCIONARIOS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Funcionario func = new Funcionario(
                        rs.getLong("ID_FUNCIONARIO"),
                        rs.getString("NOME"),
                        rs.getString("CPF"),
                        rs.getString("EMAIL"),
                        rs.getString("CARGO"),
                        rs.getString("ATIVO"),
                        rs.getDate("DT_CADASTRO").toLocalDate(),
                        rs.getLong("LABORATORIO_ID")
                );
                lista.add(func);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários", e);
        }
        return lista;
    }

    // UPDATE
    public void update(Funcionario func) {
        String sql = "UPDATE FUNCIONARIOS SET NOME=?, CPF=?, EMAIL=?, CARGO=?, LABORATORIO_ID=?, ATIVO=?, DT_CADASTRO=? " +
                "WHERE ID_FUNCIONARIO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, func.getNome());
            stmt.setString(2, func.getCpf());
            stmt.setString(3, func.getEmail());
            stmt.setString(4, func.getCargo());
            stmt.setLong(5, func.getIdLaboratorio());
            stmt.setString(6, func.getAtivo());
            stmt.setDate(7, func.getDtCadastro() != null ? Date.valueOf(func.getDtCadastro()) : null);
            stmt.setLong(8, func.getIdFuncionario());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar funcionário", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM FUNCIONARIOS WHERE ID_FUNCIONARIO=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário", e);
        }
    }
}
