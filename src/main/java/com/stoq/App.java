package main.java.com.stoq;

import main.java.com.stoq.domain.model.*;
import main.java.com.stoq.domain.service.*;
import main.java.com.stoq.infra.dao.*;

import java.time.LocalDate;
import java.util.*;

// Utilizamos essa classe para realizar os testes mocados de todas as classes e serviços criados
public class App {

    public static void main(String[] args) {

        // Instanciando as DAOs
        MaterialDao materialDao = new MaterialDao();
        PresetDao presetDao = new PresetDao();
        PresetMaterialDao presetMaterialDao = new PresetMaterialDao();
        PedidoDao pedidoDao = new PedidoDao();
        PedidoItensDao pedidoItensDao = new PedidoItensDao();
        EstoqueDao estoqueDao = new EstoqueDao();
        HistoricoEstoqueDao historicoDao = new HistoricoEstoqueDao();
        MovimentacaoEstoqueDao movimentacaoDao = new MovimentacaoEstoqueDao();
        QRCodeDao qrCodeDao = new QRCodeDao();
        ConsultaDao consultaDao = new ConsultaDao();
        FuncionarioDao funcionarioDao = new FuncionarioDao();


        // Criando os serviços
        EstoqueService estoqueService = new EstoqueService(
                estoqueDao,
                movimentacaoDao,
                historicoDao,
                pedidoDao
        );

        PedidoService pedidoService = new PedidoService(
                pedidoDao,
                pedidoItensDao,
                materialDao,
                estoqueService
        );

        AlertaService alertaService = new AlertaService(
                estoqueDao,
                materialDao,
                pedidoService
        );

        // injetando alerta no EstoqueService
        estoqueService.setAlertaService(alertaService);


        CadastroPresetService cadastroPresetService = new CadastroPresetService(
                presetDao,
                presetMaterialDao,
                materialDao
        );

        QRCodeService qrCodeService = new QRCodeService(
                qrCodeDao,
                consultaDao,
                presetMaterialDao,
                materialDao,
                estoqueService
        );

        System.out.println("\n=== TESTE: CONSULTANDO OS LABORATORIOS CADASTRADOS NO BANCO ===");
        LaboratorioDao labDao = new LaboratorioDao();
        List<Laboratorio> labs = labDao.findAll();
        System.out.println(labs);

        System.out.println("\n=== TESTE: CONSULTANDO AS CONSULTAS MÉDICAS CADASTRADOS NO BANCO ===");
        List<Consulta> consultas = consultaDao.findAll();
        System.out.println(consultas);

        // instanciando os objetos de funcionarios que foram previamente cadastrados no banco de dados
        Funcionario enfermeiro = funcionarioDao.findById(26L);
        Funcionario admin = funcionarioDao.findById(24L);
        Funcionario gestor = funcionarioDao.findById(25L);

        System.out.println("\n=== TESTE: GERANDO QRCODE ===");
        QRCode qrCodemodel = qrCodeService.gerarQRCode(consultas.getFirst().getIdConsulta(), enfermeiro);
        System.out.println(qrCodemodel);

        System.out.println("\n=== TESTE: VALIDANDO QRCODE ===");
        qrCodeService.validarQRCode(qrCodemodel.getIdQRCode(), admin);
        QRCode atualizado = qrCodeDao.findById(qrCodemodel.getIdQRCode());
        System.out.println(atualizado);

        System.out.println("\n=== TESTE: CONSULTANDO PEDIDOS PENDENTES ===");
        List<Pedido> pedidosPendentes = pedidoDao.findAll().stream()
                .filter(p -> "PENDENTE".equalsIgnoreCase(p.getStatus()))
                .toList();

        if (pedidosPendentes.isEmpty()) {
            System.out.println("Nenhum pedido pendente encontrado.");
        } else {
            for (Pedido ped : pedidosPendentes) {
                System.out.println("Pedido encontrado: " + ped);

                System.out.println("\n=== TESTE: ENVIANDO PEDIDO PARA FORNECEDOR ===");
                pedidoService.enviarPedido(ped.getIdPedido(), gestor, "Fornecedor XPTO");
            }
        }

        System.out.println("\n=== TESTE: CONFIRMANDO RECEBIMENTO DO PEDIDO ===");

// Busca pedidos pendentes (enviados, mas ainda não recebidos)
        List<Pedido> pedidosPendentesParaReceber = pedidoDao.findAll().stream()
                .filter(p -> "PENDENTE".equalsIgnoreCase(p.getStatus()))
                .toList();

        if (pedidosPendentesParaReceber.isEmpty()) {
            System.out.println("Nenhum pedido aguardando recebimento.");
        } else {
            Pedido pedidoParaReceber = pedidosPendentesParaReceber.getFirst();
            System.out.println("➡ Recebendo pedido: " + pedidoParaReceber);

            // Estoque ANTES
            System.out.println("\n--- ESTOQUE ANTES DO RECEBIMENTO ---");
            List<Estoque> estoqueAntes = estoqueDao.findByLaboratorio(pedidoParaReceber.getIdLaboratorio());
            for (Estoque e : estoqueAntes) {
                System.out.println(e);
            }

            // Simula recebimento de todos os itens
            Map<Long, PedidoItens> recebimentos = new HashMap<>();
            for (PedidoItens item : pedidoDao.buscarItensDoPedido(pedidoParaReceber.getIdPedido())) {
                PedidoItens recebido = new PedidoItens(
                        item.getIdPedido(),
                        item.getIdMaterial(),
                        item.getQtdeSolicitada(), // solicitado
                        item.getQtdeSolicitada(), // recebido
                        10.0f, // preço fictício
                        "LOTE-" + System.currentTimeMillis(),
                        LocalDate.now().plusMonths(12)
                );
                recebimentos.put(item.getIdMaterial(), recebido);
            }

            // Confirma o recebimento
            pedidoService.receberPedido(pedidoParaReceber.getIdPedido(), admin, recebimentos);

            System.out.println("\n✅ Pedido confirmado e estoque atualizado!");

            // Estoque DEPOIS
            System.out.println("\n--- ESTOQUE DEPOIS DO RECEBIMENTO ---");
            List<Estoque> estoqueDepois = estoqueDao.findByLaboratorio(pedidoParaReceber.getIdLaboratorio());
            for (Estoque e : estoqueDepois) {
                System.out.println(e);
            }
        }

        System.out.println("\n=== TESTE: VALIDANDO ALERTA DE VALIDADE ===");

        // Busca o mesmo pedido que já recebemos anteriormente
        List<Pedido> pedidosRecebidos = pedidoDao.findAll().stream()
                .filter(p -> "RECEBIDO".equalsIgnoreCase(p.getStatus()))
                .toList();

        if (pedidosRecebidos.isEmpty()) {
            System.out.println("Nenhum pedido recebido disponível para testar validade.");
        } else {
            Pedido pedidoRecebido = pedidosRecebidos.getFirst();
            System.out.println("➡ Simulando validade próxima no pedido: " + pedidoRecebido.getIdPedido());

            // Pegar itens do pedido
            List<PedidoItens> itens = pedidoDao.buscarItensDoPedido(pedidoRecebido.getIdPedido());

            if (!itens.isEmpty()) {
                PedidoItens itemComValidadeCurta = itens.getFirst();

                // Forçar a validade curta (5 dias a partir de hoje)
                itemComValidadeCurta.setValidador(LocalDate.now().plusDays(5));
                pedidoItensDao.update(itemComValidadeCurta);

                System.out.println("Item atualizado com validade curta: " + itemComValidadeCurta);
            }

            // Disparar o alerta (14 dias como recomendado pelo MS)
            alertaService.monitorarValidade(pedidoRecebido.getIdLaboratorio(), 14);
        }


        System.out.println("\n=== TESTE: CADASTRO DE PRESET ===");
        Funcionario adminSolicitante = admin; // o admin que você já buscou no banco

        // Criando preset
        Preset novoPreset = new Preset();
        novoPreset.setNome("Preset Teste");
        novoPreset.setCodigo("PTESTE");
        novoPreset.setDescricao("Preset temporário para teste");

        // Define alguns itens (IDs de materiais precisam existir no banco)
        Map<Long, Float> itensPreset = new HashMap<>();
        itensPreset.put(1L, 2.0f); // exemplo: material 1, qtde 2
        itensPreset.put(2L, 1.0f); // exemplo: material 2, qtde 1

        cadastroPresetService.criar(adminSolicitante, novoPreset, itensPreset);
        System.out.println("Preset criado com sucesso!");

        // Confirma listagem
        List<Preset> presets = cadastroPresetService.listarTodos();
        System.out.println("Presets atuais:");
        for (Preset p : presets) {
            System.out.println(p);
        }

       // Apaga o preset recém-criado
        Long idCriado = presets.stream()
                .filter(p -> "PTESTE".equalsIgnoreCase(p.getCodigo()))
                .map(Preset::getIdPreset)
                .findFirst()
                .orElse(null);

        if (idCriado != null) {
            cadastroPresetService.deletar(adminSolicitante, idCriado);
            System.out.println("Preset deletado com sucesso!");
        }

        System.out.println("\n=== TESTE: CADASTRO DE FUNCIONARIO ===");

        CadastroUsuarioService cadastroUsuarioService = new CadastroUsuarioService(funcionarioDao);

       // Criando funcionário
        Funcionario novoFunc = new Funcionario(
                null,
                "Funcionario Teste",
                "99999999999",
                "funcionario.teste@stoq.com",
                "ENFERMEIRO",
                "S",
                LocalDate.now(),
                32L
        );

        cadastroUsuarioService.cadastrar(adminSolicitante, novoFunc);
        System.out.println("Funcionário criado com sucesso!");

        // Listando todos para conferir
        List<Funcionario> funcs = cadastroUsuarioService.listarTodos();
        System.out.println("Funcionários atuais:");
        for (Funcionario f : funcs) {
            System.out.println(f);
        }

        // Deletando funcionário criado
        Long idFuncCriado = funcs.stream()
                .filter(f -> "funcionario.teste@stoq.com".equalsIgnoreCase(f.getEmail()))
                .map(Funcionario::getIdFuncionario)
                .findFirst()
                .orElse(null);

        if (idFuncCriado != null) {
            cadastroUsuarioService.deletar(adminSolicitante, idFuncCriado);
            System.out.println("Funcionário deletado com sucesso!");
        }


        System.out.println("\n=== TESTE: FECHAMENTO DE ESTOQUE ===");
        FechamentoEstoqueService fechamentoService = new FechamentoEstoqueService(estoqueDao, historicoDao);
        fechamentoService.fecharDia(LocalDate.now());

        System.out.println("✅ Fechamento de estoque realizado!");


        System.out.println("\n=== ESTOQUE ATUAL ===");
        for (Estoque e : estoqueDao.findByLaboratorio(31L)) {
            System.out.println(e);
        }

        System.out.println("\n=== MOVIMENTAÇÕES ===");
        for (MovimentacaoEstoque m : movimentacaoDao.findByLaboratorio(31L)) {
            System.out.println(m);
        }

        System.out.println("\n=== HISTÓRICO ===");
        for (HistoricoEstoque h : historicoDao.findByLaboratorio(31L)) {
            System.out.println(h);
        }

        System.out.println("\n=== TESTE: ANALYTICS SERVICE ===");
        AnalyticsService analytics = new AnalyticsService();

        System.out.println("\nMovimentações ao longo do tempo:");
        System.out.println(analytics.getMovimentacoesAoLongoDoTempo());

        System.out.println("\nFrequência de retiradas por enfermeiro:");
        System.out.println(analytics.getFrequenciaPorEnfermeiro());

        System.out.println("\nMateriais mais usados na semana:");
        System.out.println(analytics.getMateriaisMaisUsadosNaSemana());

        System.out.println("\nMovimento por dia da semana:");
        System.out.println(analytics.getMovimentoPorDiaDaSemana());

        System.out.println("\nMateriais vencidos por mês:");
        System.out.println(analytics.getMateriaisVencidosPorMes());

        System.out.println("\nTempo médio de entrega de pedidos:");
        System.out.println(analytics.getTempoMedioEntregaPedidos());

        System.out.println("\nComparação estoque mínimo x uso real:");
        System.out.println(analytics.getComparacaoEstoqueMinimoUso());



    }
}
