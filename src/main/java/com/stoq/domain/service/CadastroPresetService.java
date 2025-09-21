package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.domain.model.Material;
import main.java.com.stoq.domain.model.Preset;
import main.java.com.stoq.infra.dao.MaterialDao;
import main.java.com.stoq.infra.dao.PresetDao;
import main.java.com.stoq.infra.dao.PresetMaterialDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Cadastro e manutenção de Presets e seus materiais (preset_materiais).
 * Permissões: ADMIN ou GESTOR.
 */
public class CadastroPresetService {

    private final PresetDao presetDao;
    private final PresetMaterialDao presetMaterialDao;
    private final MaterialDao materialDao;

    public CadastroPresetService(PresetDao presetDao,
                                 PresetMaterialDao presetMaterialDao,
                                 MaterialDao materialDao) {
        this.presetDao = presetDao;
        this.presetMaterialDao = presetMaterialDao;
        this.materialDao = materialDao;
    }

    // ========== Preset: CRUD & status ==========

    /**
     * Cria um preset e, opcionalmente, os itens (materialId -> qtde_por_exame).
     */
    public void criar(Funcionario solicitante, Preset preset, Map<Long, Float> itens) {
        exigirAdminOuGestor(solicitante);
        validarNovoPreset(preset);
        exigirCodigoUnico(preset.getCodigo(), null);

        // ativo default
        if (preset.getAtivo() == null || preset.getAtivo().isBlank()) preset.setAtivo("S");

        presetDao.insert(preset);

        // resolver id do preset recém-criado (se o insert não retornar PK)
        Long idPreset = resolverIdPorCodigo(preset.getCodigo());
        if (idPreset == null) throw new RuntimeException("Falha ao resolver ID do preset recém-criado.");

        if (itens != null && !itens.isEmpty()) {
            definirItens(solicitante, idPreset, itens);
        }
    }

    /**
     * Atualiza campos do preset (nome, codigo, descricao, ativo).
     */
    public void atualizar(Funcionario solicitante, Long idPreset, Preset dados) {
        exigirAdminOuGestor(solicitante);
        Preset atual = obterPresetObrigatorio(idPreset);

        if (naoVazio(dados.getNome())) atual.setNome(dados.getNome());
        if (naoVazio(dados.getDescricao())) atual.setDescricao(dados.getDescricao());
        if (naoVazio(dados.getAtivo())) {
            validarAtivo(dados.getAtivo());
            atual.setAtivo(dados.getAtivo());
        }
        if (naoVazio(dados.getCodigo()) && !igual(atual.getCodigo(), dados.getCodigo())) {
            exigirCodigoUnico(dados.getCodigo(), idPreset);
            atual.setCodigo(dados.getCodigo());
        }

        presetDao.update(atual);
    }

    public void ativar(Funcionario solicitante, Long idPreset) {
        exigirAdminOuGestor(solicitante);
        Preset p = obterPresetObrigatorio(idPreset);
        p.setAtivo("S");
        presetDao.update(p);
    }

    public void desativar(Funcionario solicitante, Long idPreset) {
        exigirAdminOuGestor(solicitante);
        Preset p = obterPresetObrigatorio(idPreset);
        p.setAtivo("N");
        presetDao.update(p);
    }

    /**
     * Exclusão do preset e de suas associações.
     */
    public void deletar(Funcionario solicitante, Long idPreset) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset); // valida existência
        // apaga associações
        removerTodosItens(idPreset);
        // apaga preset
        presetDao.delete(idPreset);
    }

    // ========== Preset: Itens (preset_materiais) ==========

    /**
     * Substitui COMPLETAMENTE os itens do preset.
     * @param itens Map<materialId, qtde_por_exame>
     */
    public void definirItens(Funcionario solicitante, Long idPreset, Map<Long, Float> itens) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);

        if (itens == null || itens.isEmpty())
            throw new RuntimeException("A lista de itens não pode ser vazia.");

        // valida materiais e quantidades
        for (Map.Entry<Long, Float> e : itens.entrySet()) {
            Long idMat = e.getKey();
            Float qtd = e.getValue();
            validarMaterialExiste(idMat);
            validarQtdePorExame(qtd);
        }

        // remove tudo e reinsere
        removerTodosItens(idPreset);
        for (Map.Entry<Long, Float> e : itens.entrySet()) {
            presetMaterialDao.insert(idPreset, e.getKey(), e.getValue());
        }
    }

    /**
     * Adiciona ou atualiza um único item (material) do preset.
     */
    public void adicionarOuAtualizarItem(Funcionario solicitante, Long idPreset, Long idMaterial, float qtdePorExame) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);
        validarMaterialExiste(idMaterial);
        validarQtdePorExame(qtdePorExame);

        // se já existe, atualiza; se não, insere
        Float atual = presetMaterialDao.findQtdeByPresetAndMaterial(idPreset, idMaterial);
        if (atual == null) {
            presetMaterialDao.insert(idPreset, idMaterial, qtdePorExame);
        } else {
            presetMaterialDao.updateQtde(idPreset, idMaterial, qtdePorExame);
        }
    }

    /**
     * Remove um material do preset.
     */
    public void removerItem(Funcionario solicitante, Long idPreset, Long idMaterial) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);
        validarMaterialExiste(idMaterial);
        presetMaterialDao.delete(idPreset, idMaterial);
    }

    /**
     * Lista itens do preset em forma de Map<materialId, qtde_por_exame>.
     */
    public Map<Long, Float> listarItens(Long idPreset) {
        obterPresetObrigatorio(idPreset);
        // se tiver método pronto no DAO que retorna pares (materialId, qtde), use-o.
        // como fallback, carrego IDs e converto para Map
        List<Long> materiais = presetMaterialDao.findMateriaisByPresetId(idPreset);
        Map<Long, Float> mapa = new LinkedHashMap<>();
        for (Long idMat : materiais) {
            Float qtd = presetMaterialDao.findQtdeByPresetAndMaterial(idPreset, idMat);
            if (qtd != null) mapa.put(idMat, qtd);
        }
        return mapa;
    }

    // ========== Consultas auxiliares ==========

    public Preset buscarPorId(Long idPreset) {
        return presetDao.findById(idPreset);
    }

    public Optional<Preset> buscarPorCodigo(String codigo) {
        if (codigo == null) return Optional.empty();
        String alvo = codigo.trim().toLowerCase();
        return presetDao.findAll().stream()
                .filter(p -> p.getCodigo() != null && p.getCodigo().trim().toLowerCase().equals(alvo))
                .findFirst();
    }

    public List<Preset> listarTodos() {
        return presetDao.findAll();
    }

    public List<Preset> listarAtivos() {
        return presetDao.findAll().stream()
                .filter(p -> "S".equalsIgnoreCase(p.getAtivo()))
                .collect(Collectors.toList());
    }

    // ========== Helpers & validações ==========

    private void exigirAdminOuGestor(Funcionario f) {
        if (f == null || !( "ADMIN".equalsIgnoreCase(f.getCargo()) || "GESTOR".equalsIgnoreCase(f.getCargo()) ))
            throw new RuntimeException("Ação restrita a ADMIN ou GESTOR.");
    }

    private void validarNovoPreset(Preset p) {
        if (p == null) throw new RuntimeException("Dados do preset são obrigatórios.");
        exigir(naoVazio(p.getNome()), "nome");
        exigir(naoVazio(p.getCodigo()), "codigo");
        // ativo opcional; validamos se vier
        if (p.getAtivo() != null) validarAtivo(p.getAtivo());
    }

    private void validarAtivo(String ativo) {
        if (!( "S".equalsIgnoreCase(ativo) || "N".equalsIgnoreCase(ativo) )) {
            throw new RuntimeException("Campo 'ativo' deve ser 'S' ou 'N'.");
        }
    }

    private void exigirCodigoUnico(String codigo, Long ignorarId) {
        String alvo = codigo.trim().toLowerCase();
        boolean existe = presetDao.findAll().stream().anyMatch(p -> {
            if (p.getCodigo() == null) return false;
            boolean mesmoCodigo = p.getCodigo().trim().toLowerCase().equals(alvo);
            boolean mesmoId = (ignorarId != null && Objects.equals(p.getIdPreset(), ignorarId));
            return mesmoCodigo && !mesmoId;
        });
        if (existe) throw new RuntimeException("Código de preset já cadastrado: " + codigo);
    }

    private void validarMaterialExiste(Long idMaterial) {
        Material m = materialDao.findById(idMaterial);
        if (m == null) throw new RuntimeException("Material não encontrado: " + idMaterial);
        if ("N".equalsIgnoreCase(m.getAtivo())) {
            throw new RuntimeException("Material inativo: " + idMaterial);
        }
    }

    private void validarQtdePorExame(Float qtde) {
        if (qtde == null || qtde <= 0f)
            throw new RuntimeException("qtde_por_exame deve ser > 0.");
    }

    private Preset obterPresetObrigatorio(Long id) {
        Preset p = presetDao.findById(id);
        if (p == null) throw new RuntimeException("Preset não encontrado: " + id);
        return p;
    }

    private void removerTodosItens(Long idPreset) {
        // se não houver deleteByPreset no DAO, fazemos via lista de materiais
        List<Long> mats = presetMaterialDao.findMateriaisByPresetId(idPreset);
        for (Long idMat : mats) presetMaterialDao.delete(idPreset, idMat);
    }

    private Long resolverIdPorCodigo(String codigo) {
        return presetDao.findAll().stream()
                .filter(p -> p.getCodigo() != null && p.getCodigo().equals(codigo))
                .map(Preset::getIdPreset)
                .findFirst()
                .orElse(null);
    }

    private static boolean naoVazio(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static boolean igual(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private static void exigir(boolean cond, String campo) {
        if (!cond) throw new RuntimeException("Campo obrigatório: " + campo);
    }
}
