package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.domain.model.QRCode;
import main.java.com.stoq.domain.model.Consulta;
import main.java.com.stoq.domain.model.Material;
import main.java.com.stoq.domain.model.PedidoItens;
import main.java.com.stoq.infra.dao.QRCodeDao;
import main.java.com.stoq.infra.dao.ConsultaDao;
import main.java.com.stoq.infra.dao.PresetMaterialDao;
import main.java.com.stoq.infra.dao.MaterialDao;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço responsável por gerar, validar e exibir QR Code
 */
public class QRCodeService {

    private final QRCodeDao qrCodeDao;
    private final ConsultaDao consultaDao;
    private final PresetMaterialDao presetMaterialDao;
    private final MaterialDao materialDao;
    private final EstoqueService estoqueService;

    public QRCodeService(QRCodeDao qrCodeDao,
                         ConsultaDao consultaDao,
                         PresetMaterialDao presetMaterialDao,
                         MaterialDao materialDao,
                         EstoqueService estoqueService) {
        this.qrCodeDao = qrCodeDao;
        this.consultaDao = consultaDao;
        this.presetMaterialDao = presetMaterialDao;
        this.materialDao = materialDao;
        this.estoqueService = estoqueService;
    }

    /**
     * Enfermeiro gera QRCode para a consulta selecionada
     */
    public QRCode gerarQRCode(Long consultaId, Funcionario enfermeiro) {
        if (!"ENFERMEIRO".equalsIgnoreCase(enfermeiro.getCargo())) {
            throw new RuntimeException("Acesso negado: apenas ENFERMEIROS podem gerar QRCode.");
        }

        Consulta consulta = consultaDao.findById(consultaId);
        if (consulta == null) {
            throw new RuntimeException("Consulta não encontrada.");
        }

        QRCode qr = new QRCode(
                0,
                consulta.getIdConsulta(),
                enfermeiro.getIdFuncionario(),
                0, // admin ainda não validou
                consulta.getIdLab(),
                "QR-" + System.currentTimeMillis(),
                "PENDENTE",
                LocalDate.now(),
                null
        );

        qrCodeDao.insert(qr);
        return qr;
    }

    /**
     * Admin valida QRCode → retira materiais do estoque
     */
    public void validarQRCode(Long qrCodeId, Funcionario admin) {
        if (!"ADMIN".equalsIgnoreCase(admin.getCargo())) {
            throw new RuntimeException("Acesso negado: apenas ADMIN pode validar QRCode.");
        }

        QRCode qr = qrCodeDao.findById(qrCodeId);
        if (qr == null) {
            throw new RuntimeException("QRCode não encontrado.");
        }

        qr.setStatus("ACEITO");
        qr.setIdAdminValidador(admin.getIdFuncionario());
        qr.setDtValidacao(LocalDate.now());

        qrCodeDao.update(qr);

        // Consulta associada
        Consulta consulta = consultaDao.findById(qr.getIdConsulta());
        if (consulta == null) {
            throw new RuntimeException("Consulta associada ao QRCode não encontrada.");
        }

        // Busca materiais e quantidades do preset
        List<PedidoItens> materiaisRetirados = presetMaterialDao.findMateriaisComQtde(consulta.getIdPreset());

        // Chama retirada do estoque
        estoqueService.retiradaEstoque(
                qr.getIdLaboratorio(),
                qr.getIdEnfermeiro(),
                qrCodeId,
                materiaisRetirados
        );
    }

    /**
     * Lista materiais do preset associado ao QRCode (para admin visualizar)
     */
    public List<Material> listarMateriaisDoQRCode(Long qrCodeId) {
        QRCode qr = qrCodeDao.findById(qrCodeId);
        if (qr == null) {
            throw new RuntimeException("QRCode não encontrado.");
        }

        Consulta consulta = consultaDao.findById(qr.getIdConsulta());
        if (consulta == null) {
            throw new RuntimeException("Consulta associada ao QRCode não encontrada.");
        }

        List<Long> materiaisIds = presetMaterialDao.findMateriaisByPresetId(consulta.getIdPreset());
        return materialDao.findByIds(materiaisIds);
    }
}
