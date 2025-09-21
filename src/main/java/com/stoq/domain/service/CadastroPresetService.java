package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.domain.model.Material;
import main.java.com.stoq.domain.model.Preset;
import main.java.com.stoq.domain.model.PresetMaterial;
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

    public void criar(Funcionario solicitante, Preset preset, Map<Long, Float> itens) {
        exigirAdminOuGestor(solicitante);
        validarNovoPreset(preset);
        exigirCodigoUnico(preset.getCodigo(), null);

        if (preset.getAtivo() == null || preset.getAtivo().isBlank()) preset.setAtivo("S");

        presetDao.insert(preset);

        Long idPreset = resolverIdPorCodigo(preset.getCodigo());
        if (idPreset == null) throw new RuntimeException("Falha ao resolver ID do preset recém-criado.");

        if (itens != null && !itens.isEmpty()) {
            definirItens(solicitante, idPreset, itens);
        }
    }

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

    public void deletar(Funcionario solicitante, Long idPreset) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);
        removerTodosItens(idPreset);
        presetDao.delete(idPreset);
    }

    // ========== Preset: Itens ==========

    public void definirItens(Funcionario solicitante, Long idPreset, Map<Long, Float> itens) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);

        if (itens == null || itens.isEmpty())
            throw new RuntimeException("A lista de itens não pode ser vazia.");

        for (Map.Entry<Long, Float> e : itens.entrySet()) {
            validarMaterialExiste(e.getKey());
            validarQtdePorExame(e.getValue());
        }

        removerTodosItens(idPreset);
        for (Map.Entry<Long, Float> e : itens.entrySet()) {
            PresetMaterial pm = new PresetMaterial(idPreset, e.getKey(), e.getValue());
            presetMaterialDao.insert(pm);
        }
    }

    public void adicionarOuAtualizarItem(Funcionario solicitante, Long idPreset, Long idMaterial, float qtdePorExame) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);
        validarMaterialExiste(idMaterial);
        validarQtdePorExame(qtdePorExame);

        PresetMaterial existente = presetMaterialDao.findById(idPreset, idMaterial);
        if (existente == null) {
            presetMaterialDao.insert(new PresetMaterial(idPreset, idMaterial, qtdePorExame));
        } else {
            existente.setQtdePorExame(qtdePorExame);
            presetMaterialDao.update(existente);
        }
    }

    public void removerItem(Funcionario solicitante, Long idPreset, Long idMaterial) {
        exigirAdminOuGestor(solicitante);
        obterPresetObrigatorio(idPreset);
        validarMaterialExiste(idMaterial);
        presetMaterialDao.delete(idPreset, idMaterial);
    }

    public Map<Long, Float> listarItens(Long idPreset) {
        obterPresetObrigatorio(idPreset);
        List<Long> materiais = presetMaterialDao.findMateriaisByPresetId(idPreset);
        Map<Long, Float> mapa = new LinkedHashMap<>();
        for (Long idMat : materiais) {
            PresetMaterial pm = presetMaterialDao.findById(idPreset, idMat);
            if (pm != null) {
                mapa.put(idMat, pm.getQtdePorExame());
            }
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
