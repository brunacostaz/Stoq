package main.java.com.stoq.infra.dao;

import main.java.com.stoq.infra.db.OracleConnectionFactory;
import main.java.com.stoq.domain.model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDao {

    // CREATE
    public void insert(Material material) {
        String sql = "INSERT INTO MATERIAIS (NOME, ID_LOTE, UNIDADE_MEDIDA, ESTOQUE_MINIMO, DESCRICAO, ATIVO) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getIdLote());
            stmt.setString(3, material.getUnidadeMedida());
            stmt.setInt(4, material.getEstoqueMinimo());
            stmt.setString(5, material.getDescricao());
            stmt.setString(6, material.getAtivo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir material", e);
        }
    }

    // READ BY ID
    public Material findById(Long id) {
        String sql = "SELECT * FROM MATERIAIS WHERE ID_MATERIAL = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMaterial(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar material por id", e);
        }
        return null;
    }

    // READ ALL
    public List<Material> findAll() {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM MATERIAIS";
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar materiais", e);
        }
        return lista;
    }

    // Buscar vários materiais por IDs
    public List<Material> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String placeholders = String.join(",", ids.stream().map(id -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM MATERIAIS WHERE ID_MATERIAL IN (" + placeholders + ")";

        List<Material> materiais = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                stmt.setLong(i + 1, ids.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                materiais.add(mapResultSetToMaterial(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar materiais por IDs", e);
        }

        return materiais;
    }

    // UPDATE
    public void update(Material material) {
        String sql = "UPDATE MATERIAIS SET NOME=?, ID_LOTE=?, UNIDADE_MEDIDA=?, ESTOQUE_MINIMO=?, DESCRICAO=?, ATIVO=? " +
                "WHERE ID_MATERIAL=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getIdLote());
            stmt.setString(3, material.getUnidadeMedida());
            stmt.setInt(4, material.getEstoqueMinimo());
            stmt.setString(5, material.getDescricao());
            stmt.setString(6, material.getAtivo());
            stmt.setLong(7, material.getIdMaterial());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar material", e);
        }
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM MATERIAIS WHERE ID_MATERIAL=?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar material", e);
        }
    }

    // Helper para mapear ResultSet em Material
    private Material mapResultSetToMaterial(ResultSet rs) throws SQLException {
        return new Material(
                rs.getLong("ID_MATERIAL"),
                rs.getString("NOME"),
                rs.getString("ID_LOTE"),
                rs.getString("UNIDADE_MEDIDA"),
                rs.getInt("ESTOQUE_MINIMO"),
                rs.getString("DESCRICAO"),
                rs.getString("ATIVO")
        );
    }

    // Busca materiais que estão abaixo do estoque mínimo em um laboratório
    public List<Material> findMateriaisAbaixoEstoqueMinimo(long laboratorioId) {
        String sql = """
        SELECT DISTINCT m.* 
        FROM MATERIAIS m
        JOIN ESTOQUE e ON m.ID_MATERIAL = e.MATERIAL_ID
        WHERE e.LABORATORIO_ID = ? AND e.QTDE <= m.ESTOQUE_MINIMO
    """;

        List<Material> lista = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, laboratorioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Material material = new Material(
                        rs.getLong("ID_MATERIAL"),
                        rs.getString("NOME"),
                        rs.getString("ID_LOTE"),
                        rs.getString("UNIDADE_MEDIDA"),
                        rs.getInt("ESTOQUE_MINIMO"),
                        rs.getString("DESCRICAO"),
                        rs.getString("ATIVO")
                );
                lista.add(material);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar materiais abaixo do estoque mínimo", e);
        }

        return lista;
    }
}
