package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.domain.model.Material;
import main.java.com.stoq.domain.model.Pedido;
import main.java.com.stoq.domain.model.PedidoItens;
import main.java.com.stoq.infra.dao.MaterialDao;
import main.java.com.stoq.infra.dao.PedidoDao;
import main.java.com.stoq.infra.dao.PedidoItensDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço de Pedidos integrado às tabelas PEDIDOS e PEDIDO_ITENS.
 * Status utilizados conforme modelagem: PENDENTE, RECEBIDO, CANCELADO.
 */
public class PedidoService {

    private final PedidoDao pedidoDao;
    private final PedidoItensDao pedidoItensDao;
    private final MaterialDao materialDao;
    private final EstoqueService estoqueService;

    public PedidoService(PedidoDao pedidoDao,
                         PedidoItensDao pedidoItensDao,
                         MaterialDao materialDao,
                         EstoqueService estoqueService) {
        this.pedidoDao = pedidoDao;
        this.pedidoItensDao = pedidoItensDao;
        this.materialDao = materialDao;
        this.estoqueService = estoqueService;
    }

    /**
     * Acionado pelo AlertaService.monitorarBaixaEstoque.
     * Cria cabeçalho em PEDIDOS (PENDENTE) + itens sugeridos em PEDIDO_ITENS.
     *
     * @param materiaisEmBaixa IDs de materiais abaixo do mínimo
     * @param solicitante      funcionário que disparou (SISTEMA/ALMOX/ADMIN)
     * @param idLaboratorio    laboratório do pedido
     * @return ID do pedido criado
     */
    public Long realizarPedido(List<Long> materiaisEmBaixa, Funcionario solicitante, Long idLaboratorio) {
        validarSolicitante(solicitante);
        if (materiaisEmBaixa == null || materiaisEmBaixa.isEmpty())
            throw new RuntimeException("Não há materiais em baixa para gerar pedido.");

        //Monta itens sugeridos
        List<PedidoItens> itens = new ArrayList<>();
        for (Long idMat : materiaisEmBaixa) {
            Material m = materialDao.findById(idMat);
            if (m == null) continue;

            float qtdSugerida = calcularQtdCompraSugerida(m); // baseado em ESTOQUE_MINIMO
            PedidoItens item = new PedidoItens(
                    idMat,                    // idMaterial
                    qtdSugerida,              // qtdeSolicitada
                    0f,                       // qntdeRecebida
                    0f,                       // precoUnitario (gestor pode ajustar depois)
                    null,                     // lote (definido no recebimento)
                    null                      // validade (definida no recebimento)
            );
            itens.add(item);
        }
        if (itens.isEmpty())
            throw new RuntimeException("IDs de materiais inválidos; não foi possível montar o pedido.");

        //Cria cabeçalho do pedido
        String numero = gerarNumero();
        Pedido cabecalho = new Pedido(
                numero,               // numero
                idLaboratorio,        // laboratorio_id
                solicitante.getIdFuncionario(), // funcionario_id
                "PENDENTE",           // status
                LocalDate.now(),      // dt_criacao
                null,                 // dt_recebimento
                "Exemplo Fornecedor"                  // fornecedor_nome
        );

        pedidoDao.insert(cabecalho);

        // ID do pedido recém-criado pelo NUMERO
        Long idPedido = resolverIdPorNumero(numero);
        if (idPedido == null)
            throw new RuntimeException("Falha ao resolver ID do pedido recém-criado.");

        // Persiste itens em PEDIDO_ITENS
        for (PedidoItens it : itens) {

            it.setIdPedido(idPedido);
            // DEBUG para verificar duplicação
            System.out.printf(
                    "[DEBUG] Inserindo PedidoItens -> Pedido_ID: %d | Material_ID: %d | QtdeSolicitada: %.2f | QtdeRecebida: %.2f%n",
                    it.getIdPedido(),
                    it.getIdMaterial(),
                    it.getQtdeSolicitada(),
                    it.getQntdeRecebida()
            );
            pedidoItensDao.insert(it);

        }



        return idPedido;
    }

    /**
     * “Aprovar/Enviar”: define o fornecedor e mantém status PENDENTE.
     * (o modelo não tem status APROVADO/ENVIADO; consideramos PENDENTE até RECEBIDO/CANCELADO.)
     */
    public void enviarPedido(Long idPedido, Funcionario gestor, String fornecedorNome) {
        validarGestor(gestor);
        if (fornecedorNome == null || fornecedorNome.isBlank())
            throw new RuntimeException("Informe fornecedor_nome.");

        Pedido p = pedidoDao.findById(idPedido);
        if (p == null) throw new RuntimeException("Pedido não encontrado.");
        if (!"PENDENTE".equalsIgnoreCase(p.getStatus()))
            throw new RuntimeException("Somente pedidos PENDENTE podem ser enviados.");

        p.setFornecedorNome(fornecedorNome);
        // status permanece PENDENTE até o recebimento
        pedidoDao.update(p);
        System.out.println("Pedido enviado ao fornecedor com sucesso!");
    }

    /**
     * Edição completa dos itens antes do envio (substituição total):
     * remove todos os itens do pedido e insere a nova lista.
     */
    public void editarItens(Long idPedido, Funcionario gestor, List<PedidoItens> novosItens) {
        validarGestor(gestor);
        if (novosItens == null || novosItens.isEmpty())
            throw new RuntimeException("Lista de itens não pode ser vazia.");

        Pedido p = pedidoDao.findById(idPedido);
        if (p == null) throw new RuntimeException("Pedido não encontrado.");
        if (!"PENDENTE".equalsIgnoreCase(p.getStatus()))
            throw new RuntimeException("Somente pedidos PENDENTE podem ser editados.");

        // Deleta itens atuais
        List<PedidoItens> atuais = listarItensPorPedido(idPedido);
        for (PedidoItens it : atuais) {
            pedidoItensDao.delete(idPedido, it.getIdMaterial());
        }
        // Insere substitutos
        for (PedidoItens it : novosItens) {
            it.setIdPedido(idPedido);
            pedidoItensDao.insert(it);
        }
    }

    /**
     * Recebimento do pedido:
     * - atualiza PEDIDO_ITENS.qtde_recebida/lote/validade
     * - marca PEDIDOS.dt_recebimento e status = RECEBIDO
     * - chama EstoqueService para registrar a entrada e as movimentações.
     *
     * @param recebimentos mapa material_id -> dados recebidos (qtde/lote/validade/preço)
     */
    public void receberPedido(Long idPedido,
                              Funcionario recebedor,
                              Map<Long, PedidoItens> recebimentos) {
        validarRecebedor(recebedor);

        Pedido p = pedidoDao.findById(idPedido);
        if (p == null) throw new RuntimeException("Pedido não encontrado.");
        if (!"PENDENTE".equalsIgnoreCase(p.getStatus()))
            throw new RuntimeException("Somente pedidos PENDENTE podem ser recebidos.");

        // Atualiza itens conforme o que chegou
        List<PedidoItens> itensAtuais = listarItensPorPedido(idPedido);
        Map<Long, PedidoItens> porMaterial = itensAtuais.stream()
                .collect(Collectors.toMap(PedidoItens::getIdMaterial, x -> x, (a, b) -> a));

        for (Map.Entry<Long, PedidoItens> e : recebimentos.entrySet()) {
            Long idMat = e.getKey();
            PedidoItens dados = e.getValue();

            PedidoItens original = porMaterial.get(idMat);
            if (original == null) {
                throw new RuntimeException("Material " + idMat + " não pertence a este pedido.");
            }

            original.setQntdeRecebida(naoNegativo(dados.getQntdeRecebida()));
            original.setLote(dados.getLote());
            original.setValidador(dados.getValidador());

            if (dados.getPrecoUnitario() > 0) {
                original.setPrecoUnitario(dados.getPrecoUnitario());
            }
            pedidoItensDao.update(original);
        }

        // Marca cabeçalho como RECEBIDO
        p.setStatus("RECEBIDO");
        p.setDtRecebimento(LocalDate.now());
        pedidoDao.update(p);

        // Atualiza estoque/movimentação
        // Implemente no EstoqueService a leitura de PEDIDO_ITENS para somar qtde_recebida por material.
        estoqueService.entradaEstoquePorPedido(idPedido, recebedor);
    }

    public void cancelarPedido(Long idPedido, Funcionario gestorOuAdmin) {
        validarGestorOuAdmin(gestorOuAdmin);

        Pedido p = pedidoDao.findById(idPedido);
        if (p == null) throw new RuntimeException("Pedido não encontrado.");
        if (!"PENDENTE".equalsIgnoreCase(p.getStatus()))
            throw new RuntimeException("Somente pedidos PENDENTE podem ser cancelados.");

        p.setStatus("CANCELADO");
        pedidoDao.update(p);
    }

    // =================== Helpers ===================

    private void validarSolicitante(Funcionario f) {
        if (f == null) throw new RuntimeException("Solicitante inválido.");
        // Se quiser restringir por cargo, faça aqui (ex.: ALMOX/ADMIN/SISTEMA)
    }

    private void validarGestor(Funcionario f) {
        if (f == null) throw new RuntimeException("Gestor inválido.");
        if (!"GESTOR".equalsIgnoreCase(f.getCargo()))
            throw new RuntimeException("Apenas GESTOR pode aprovar/editar/enviar pedidos.");
    }

    private void validarRecebedor(Funcionario f) {
        if (f == null) throw new RuntimeException("Recebedor inválido.");
        // Pode permitir ADMIN/ALMOX recebendo; ajuste conforme sua regra:
        if (!("ADMIN".equalsIgnoreCase(f.getCargo()) || "ALMOX".equalsIgnoreCase(f.getCargo()) || "GESTOR".equalsIgnoreCase(f.getCargo()))) {
            throw new RuntimeException("Cargo não autorizado para receber pedido.");
        }
    }

    private void validarGestorOuAdmin(Funcionario f) {
        if (f == null) throw new RuntimeException("Funcionário inválido.");
        if (!("GESTOR".equalsIgnoreCase(f.getCargo()) || "ADMIN".equalsIgnoreCase(f.getCargo())))
            throw new RuntimeException("Apenas GESTOR ou ADMIN podem cancelar pedido.");
    }

    private String gerarNumero() {
        return "PED-" + LocalDate.now() + "-" + System.currentTimeMillis();
    }

    private Long resolverIdPorNumero(String numeroGerado) {
        return pedidoDao.findAll().stream()
                .filter(p -> numeroGerado.equals(p.getNumero()))
                .map(Pedido::getIdPedido)
                .findFirst()
                .orElse(null);
    }

    private List<PedidoItens> listarItensPorPedido(Long idPedido) {
        // Sem método dedicado no DAO, filtramos o findAll()
        return pedidoItensDao.findAll().stream()
                .filter(it -> Objects.equals(it.getIdPedido(), idPedido))
                .collect(Collectors.toList());
    }

    private float calcularQtdCompraSugerida(Material m) {
        Integer min = m.getEstoqueMinimo();
        int base = (min != null ? min : 0);
        return Math.max(base, 1);
    }

    private float naoNegativo(float v) {
        return Math.max(v, 0f);
    }
}
