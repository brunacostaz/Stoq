package main.java.com.stoq.domain.service;

import main.java.com.stoq.infra.db.OracleConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Serviço responsável por realizar agregações de valores e consultas no banco de dados, criando pacotes consolidados com as informações coletadas (estoque, materiais etc), para enviar ao front e gerar gráficos, tabelas e métricas. Dessa forma, esses dados poderão orientar as análises e tomada de decisão dos gestores
 */

public class AnalyticsService {

    // Map é uma interface no java com coleções de pares chave (como um dicionário no python ou arquivo json)
    // HashMap é uma das formas de implementar a interface Map. Nele, os valores são colocados sem ordem, mas a busca é extremamente rápida e eficiente ( O(1))

    // Retirada e reposição ao longo do tempo
    public Map<LocalDate, Map<String, Float>> getMovimentacoesAoLongoDoTempo() {
        Map<LocalDate, Map<String, Float>> resultado = new HashMap<>();
        String sql = "SELECT DIA_MOVIMENTACAO, TIPO, SUM(QTDE) AS TOTAL " +
                "FROM MOVIMENTACAO " +
                "GROUP BY DIA_MOVIMENTACAO, TIPO";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate dia = rs.getDate("DIA_MOVIMENTACAO").toLocalDate();
                String tipo = rs.getString("TIPO");
                float total = rs.getFloat("TOTAL");

                resultado.putIfAbsent(dia, new HashMap<>());
                resultado.get(dia).put(tipo, total);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar movimentações ao longo do tempo", e);
        }
        return resultado;
    }

    // Frequência de retirada por enfermeiro
    public Map<Long, Integer> getFrequenciaPorEnfermeiro() {
        Map<Long, Integer> resultado = new HashMap<>();
        String sql = "SELECT FUNCIONARIO_ID, COUNT(*) AS TOTAL " +
                "FROM MOVIMENTACAO " +
                "WHERE TIPO = 'SAIDA' " +
                "GROUP BY FUNCIONARIO_ID";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultado.put(rs.getLong("FUNCIONARIO_ID"), rs.getInt("TOTAL"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar frequência de retiradas", e);
        }
        return resultado;
    }

    // Materiais mais utilizados na semana
    public Map<Long, Float> getMateriaisMaisUsadosNaSemana() {
        Map<Long, Float> resultado = new HashMap<>();
        String sql = "SELECT MATERIAL_ID, SUM(QTDE) AS TOTAL " +
                "FROM MOVIMENTACAO " +
                "WHERE TIPO = 'SAIDA' " +
                "AND DIA_MOVIMENTACAO >= SYSDATE - 7 " +
                "GROUP BY MATERIAL_ID " +
                "ORDER BY TOTAL DESC";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultado.put(rs.getLong("MATERIAL_ID"), rs.getFloat("TOTAL"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar materiais mais utilizados", e);
        }
        return resultado;
    }

    // Dias/horários de maior movimento
    public Map<String, Integer> getMovimentoPorDiaDaSemana() {
        Map<String, Integer> resultado = new HashMap<>();
        String sql = "SELECT TO_CHAR(DIA_MOVIMENTACAO, 'DY') AS DIA, COUNT(*) AS TOTAL " +
                "FROM MOVIMENTACAO " +
                "GROUP BY TO_CHAR(DIA_MOVIMENTACAO, 'DY')";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultado.put(rs.getString("DIA").trim(), rs.getInt("TOTAL"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar movimentação por dia da semana", e);
        }
        return resultado;
    }

    // Taxa de materiais vencidos descartados por mês
    public Map<String, Integer> getMateriaisVencidosPorMes() {
        Map<String, Integer> resultado = new HashMap<>();
        String sql = "SELECT TO_CHAR(VALIDADE, 'MM-YYYY') AS MES, COUNT(*) AS TOTAL " +
                "FROM PEDIDO_ITENS " +
                "WHERE VALIDADE < SYSDATE " +
                "GROUP BY TO_CHAR(VALIDADE, 'MM-YYYY') " +
                "ORDER BY MES";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultado.put(rs.getString("MES"), rs.getInt("TOTAL"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar taxa de materiais vencidos", e);
        }
        return resultado;
    }

    // Tempo médio entre pedido e recebimento
    public Double getTempoMedioEntregaPedidos() {
        String sql = "SELECT AVG(DT_RECEBIMENTO - DT_CRIACAO) AS MEDIA " +
                "FROM PEDIDOS " +
                "WHERE DT_RECEBIMENTO IS NOT NULL";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("MEDIA");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular tempo médio de entrega de pedidos", e);
        }
        return null;
    }

    // Comparação entre estoque mínimo e uso real
    public Map<Long, Float> getComparacaoEstoqueMinimoUso() {
        Map<Long, Float> resultado = new HashMap<>();
        String sql = "SELECT m.ID_MATERIAL, m.ESTOQUE_MINIMO, NVL(SUM(mov.QTDE),0) AS USO_REAL " +
                "FROM MATERIAIS m " +
                "LEFT JOIN MOVIMENTACAO mov ON m.ID_MATERIAL = mov.MATERIAL_ID AND mov.TIPO = 'SAIDA' " +
                "GROUP BY m.ID_MATERIAL, m.ESTOQUE_MINIMO";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                long idMaterial = rs.getLong("ID_MATERIAL");
                float estoqueMinimo = rs.getFloat("ESTOQUE_MINIMO");
                float usoReal = rs.getFloat("USO_REAL");

                // Se quiser pode calcular a diferença
                resultado.put(idMaterial, usoReal - estoqueMinimo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao comparar estoque mínimo com uso real", e);
        }
        return resultado;
    }
}
