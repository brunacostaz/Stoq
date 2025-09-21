package main.java.com.stoq.domain.service;

import main.java.com.stoq.infra.dao.EstoqueDao;
import main.java.com.stoq.infra.dao.HistoricoEstoqueDao;

import java.time.LocalDate;

public class FechamentoEstoqueService {

    private final EstoqueDao estoqueDao;
    private final HistoricoEstoqueDao historicoDao;

    public FechamentoEstoqueService(EstoqueDao estoqueDao, HistoricoEstoqueDao historicoDao) {
        this.estoqueDao = estoqueDao;
        this.historicoDao = historicoDao;
    }

    /**
     * Fecha o estoque do dia:
     * Copia o saldo atual do ESTOQUE para o HISTORICO_ESTOQUE
     * Limpa registros antigos no ESTOQUE
     */
    public void fecharDia(LocalDate dia) {
        // Copia os dados do estoque para o histórico
        historicoDao.copiarEstoqueParaHistorico(dia);

        // Remove registros de dias anteriores no estoque
        estoqueDao.limparDiasAntigos(dia);
    }
}
