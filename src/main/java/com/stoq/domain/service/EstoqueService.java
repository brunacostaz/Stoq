package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.domain.model.MovimentacaoEstoque;
import main.java.com.stoq.domain.model.Pedido;
import main.java.com.stoq.domain.model.PedidoItens;
import main.java.com.stoq.infra.dao.EstoqueDao;
import main.java.com.stoq.infra.dao.HistoricoEstoqueDao;
import main.java.com.stoq.infra.dao.MovimentacaoEstoqueDao;
import main.java.com.stoq.infra.dao.PedidoDao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstoqueService {

    private final EstoqueDao estoqueDao;
    private final MovimentacaoEstoqueDao movimentacaoDao;
    private final HistoricoEstoqueDao historicoDao;
    private final PedidoDao pedidoDao;
    private final AlertaService alertaService;

    public EstoqueService(EstoqueDao estoqueDao, MovimentacaoEstoqueDao movimentacaoDao,
                          HistoricoEstoqueDao historicoDao, PedidoDao pedidoDao,
                          AlertaService alertaService) {
        this.estoqueDao = estoqueDao;
        this.movimentacaoDao = movimentacaoDao;
        this.historicoDao = historicoDao;
        this.pedidoDao = pedidoDao;
        this.alertaService = alertaService;
    }

    /**
     * Método responsável por realizar o desconto dos materiais retirados no estoque, registrando o evento na MovimentacaoEstoque e atualizando o saldo na tabela Estoque
     * @param laboratorioId
     * @param funcionarioId
     * @param qrcodeId
     * @param materiaisRetirados
     */
    public void retiradaEstoque(long laboratorioId, long funcionarioId, Long qrcodeId, List<PedidoItens> materiaisRetirados) {
        for (PedidoItens item : materiaisRetirados) {
            long materialId = item.getIdMaterial();
            float qtde = item.getQtdeSolicitada();

            // Atualiza estoque em tempo real
            estoqueDao.atualizarEstoque(laboratorioId, materialId, -qtde);

            // Registra movimentação
            MovimentacaoEstoque mov = new MovimentacaoEstoque(
                    0,
                    LocalDate.now(),
                    "SAIDA",
                    laboratorioId,
                    materialId,
                    qrcodeId,
                    funcionarioId,
                    qtde,
                    "Retirada via QRCode"
            );
            movimentacaoDao.insert(mov);
        }

        // Dispara alerta para checar se algum material ficou abaixo do mínimo
        alertaService.monitorarBaixaEstoque(laboratorioId);
    }

    /**
     * Método responsável por adicionar os itens que foram comprados na tabela estoque e resgistrar o evento na movimentaçãoEstoque
     * @param pedidoId
     * @param recebedor
     */
    public void entradaEstoquePorPedido(long pedidoId, Funcionario recebedor) {
        Pedido pedido = pedidoDao.findById(pedidoId);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado: " + pedidoId);
        }

        long laboratorioId = pedido.getIdLaboratorio();
        List<PedidoItens> itens = pedidoDao.buscarItensDoPedido(pedidoId);

        // Agrupa por material e soma quantidades recebidas
        Map<Long, Float> somaPorMaterial = new HashMap<>();
        for (PedidoItens item : itens) {
            somaPorMaterial.merge(item.getIdMaterial(), item.getQntdeRecebida(), Float::sum);
        }

        // Atualiza estoque e registra movimentação para cada material
        for (Map.Entry<Long, Float> entry : somaPorMaterial.entrySet()) {
            long materialId = entry.getKey();
            float qtde = entry.getValue();

            // Atualiza estoque
            estoqueDao.atualizarEstoque(laboratorioId, materialId, qtde);

            // Registra movimentação
            MovimentacaoEstoque mov = new MovimentacaoEstoque(
                    0,
                    LocalDate.now(),
                    "ENTRADA",
                    laboratorioId,
                    materialId,
                    null, // não há QRCode em reposição
                    recebedor.getIdFuncionario(),
                    qtde,
                    "Entrada consolidada de pedido"
            );
            movimentacaoDao.insert(mov);
        }

        // Após atualizar, dispara alerta
        alertaService.monitorarBaixaEstoque(laboratorioId, recebedor);
    }
}
