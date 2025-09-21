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


        // Criando os serviços
        EstoqueService estoqueService = new EstoqueService(
                estoqueDao,
                movimentacaoDao,
                historicoDao,
                pedidoDao,
                null // AlertaService será instanciado depois
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

        // injetar alerta no estoque
        EstoqueService estoqueServiceFinal = new EstoqueService(
                estoqueDao,
                movimentacaoDao,
                historicoDao,
                pedidoDao,
                alertaService
        );

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
                estoqueServiceFinal
        );

        System.out.println("\n=== TESTE: CONSULTANDO OS LABORATORIOS CADASTRADOS NO BANCO ===");
        LaboratorioDao labDao = new LaboratorioDao();
        List<Laboratorio> labs = labDao.findAll();
        System.out.println(labs);


    }
}
