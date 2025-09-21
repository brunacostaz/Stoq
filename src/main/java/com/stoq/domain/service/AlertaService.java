package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.domain.model.Material;
import main.java.com.stoq.infra.dao.MaterialDao;
import main.java.com.stoq.infra.dao.EstoqueDao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por monitorar estoque e validade de materiais,
 * disparando alertas e acionando pedidos automáticos quando necessário
 */
public class AlertaService {

    private final EstoqueDao estoqueDao;
    private final MaterialDao materialDao;
    private final PedidoService pedidoService;

    public AlertaService(EstoqueDao estoqueDao, MaterialDao materialDao, PedidoService pedidoService) {
        this.estoqueDao = estoqueDao;
        this.materialDao = materialDao;
        this.pedidoService = pedidoService;
    }

    /**
     * Sobrecarga: dispara alerta usando um usuário "SISTEMA"
     * Útil para chamadas automáticas (ex.: EstoqueService)
     */
    public void monitorarBaixaEstoque(long laboratorioId) {
        Funcionario sistema = new Funcionario(
                0L,                     // idFuncionario fictício
                "Sistema",              // nome
                "",                     // cpf
                "sistema@stoq",         // email
                "SISTEMA",              // cargo
                "S",                    // ativo
                LocalDate.now(),        // dtCadastro simbólico
                laboratorioId           // laboratório relacionado
        );
        monitorarBaixaEstoque(laboratorioId, sistema);
    }

    /**
     * Monitora estoques e dispara pedidos quando o saldo atual está abaixo do mínimo
     * @param laboratorioId ID do laboratório
     * @param solicitante Usuário que disparou (ADMIN, ALMOX, SISTEMA, etc)
     */
    public void monitorarBaixaEstoque(long laboratorioId, Funcionario solicitante) {
        List<Material> materiaisCriticos = materialDao.findMateriaisAbaixoEstoqueMinimo(laboratorioId);

        if (materiaisCriticos.isEmpty()) {
            System.out.println("Estoque dentro dos limites mínimos.");
            return;
        }

        // Log/alerta
        for (Material m : materiaisCriticos) {
            System.out.println("⚠ Alerta: Material abaixo do mínimo → " + m.getNome());
        }

        // Monta lista de IDs para pedido automático
        List<Long> idsMateriais = materiaisCriticos.stream()
                .map(Material::getIdMaterial)
                .collect(Collectors.toList());

        // Chama PedidoService para abrir pedido
        Long idPedido = pedidoService.realizarPedido(idsMateriais, solicitante, laboratorioId);
        System.out.println("Pedido automático criado: ID " + idPedido);
    }

    /**
     * Monitora validade dos itens (puxando da tabela PEDIDO_ITENS)
     * Dispara alertas caso o prazo esteja próximo do vencimento
     * @param laboratorioId ID do laboratório
     * @param diasAviso Prazo de alerta em dias (padrão recomendado pelo Ministério da Saúde: 14 dias)
     */
    public void monitorarValidade(long laboratorioId, int diasAviso) {
        List<Material> materiaisVencendo = estoqueDao.findMateriaisProximosVencimento(laboratorioId, diasAviso);

        if (materiaisVencendo.isEmpty()) {
            System.out.println("Nenhum material próximo do vencimento.");
            return;
        }

        for (Material m : materiaisVencendo) {
            System.out.println("⚠ Alerta: Material próximo do vencimento → " + m.getNome());
        }
    }
}
