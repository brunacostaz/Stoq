package main.java.com.stoq.domain.service;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.infra.dao.FuncionarioDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Regras de cadastro e manutenção de Funcionários.
 * Papéis:
 *  - ADMIN pode criar/editar/ativar/desativar/deletar
 *  - GESTOR pode criar/editar/ativar/desativar (não deletar)
 *  - ENFERMEIRO sem permissões administrativas
 */
public class CadastroUsuarioService {

    private final FuncionarioDao funcionarioDao;

    public CadastroUsuarioService(FuncionarioDao funcionarioDao) {
        this.funcionarioDao = funcionarioDao;
    }

    // ========= AÇÕES PRINCIPAIS =========

    /**
     * Cadastra um novo funcionário.
     * - Valida cargo do solicitante (ADMIN ou GESTOR)
     * - Valida campos obrigatórios e formato de e-mail
     * - Garante unicidade do e-mail
     * - Seta dt_cadastro (agora) e ativo = "S" se vier nulo
     */
    public void cadastrar(Funcionario solicitante, Funcionario novo) {
        exigirAdminOuGestor(solicitante);
        validarNovoFuncionario(novo);
        exigirEmailUnico(novo.getEmail(), null);

        if (novo.getDtCadastro() == null) novo.setDtCadastro(LocalDate.now());
        if (novo.getAtivo() == null || novo.getAtivo().isBlank()) novo.setAtivo("S");

        funcionarioDao.insert(novo);
    }

    /**
     * Atualiza dados de um funcionário.
     * - Admin/Gestor podem atualizar
     * - Se trocar e-mail, mantém unicidade
     */
    public void atualizar(Funcionario solicitante, Long idFuncionario, Funcionario dados) {
        exigirAdminOuGestor(solicitante);

        Funcionario atual = obterObrigatorio(idFuncionario);

        // Checagem de e-mail único (se alterado)
        if (dados.getEmail() != null && !dados.getEmail().isBlank()
                && !Objects.equals(normalizar(atual.getEmail()), normalizar(dados.getEmail()))) {
            exigirEmailUnico(dados.getEmail(), idFuncionario);
            validarEmail(dados.getEmail());
            atual.setEmail(dados.getEmail());
        }

        // Campos simples (se vierem preenchidos)
        if (naoVazio(dados.getNome())) atual.setNome(dados.getNome());
        if (naoVazio(dados.getCpf()))  atual.setCpf(dados.getCpf());
        if (naoVazio(dados.getCargo())) {
            validarCargo(dados.getCargo());
            atual.setCargo(dados.getCargo());
        }
        if (dados.getIdLaboratorio() != null) atual.setIdLaboratorio(dados.getIdLaboratorio());
        if (naoVazio(dados.getAtivo())) atual.setAtivo(dados.getAtivo());
        if (dados.getDtCadastro() != null) atual.setDtCadastro(dados.getDtCadastro());

        funcionarioDao.update(atual);
    }

    public void ativar(Funcionario solicitante, Long idFuncionario) {
        exigirAdminOuGestor(solicitante);
        Funcionario f = obterObrigatorio(idFuncionario);
        f.setAtivo("S");
        funcionarioDao.update(f);
    }

    public void desativar(Funcionario solicitante, Long idFuncionario) {
        exigirAdminOuGestor(solicitante);
        Funcionario f = obterObrigatorio(idFuncionario);
        f.setAtivo("N");
        funcionarioDao.update(f);
    }

    /**
     * Exclusão definitiva (DELETE). Restrita a ADMIN.
     */
    public void deletar(Funcionario solicitante, Long idFuncionario) {
        exigirAdmin(solicitante);
        // opcional: impedir auto-delete do próprio ADMIN logado
        funcionarioDao.delete(idFuncionario);
    }

    // ========= CONSULTAS ÚTEIS =========

    public Funcionario buscarPorId(Long id) {
        return funcionarioDao.findById(id);
    }

    public Optional<Funcionario> buscarPorEmail(String email) {
        if (email == null) return Optional.empty();
        String alvo = normalizar(email);
        return funcionarioDao.findAll().stream()
                .filter(f -> f.getEmail() != null && normalizar(f.getEmail()).equals(alvo))
                .findFirst();
    }

    public List<Funcionario> listarTodos() {
        return funcionarioDao.findAll();
    }

    public List<Funcionario> listarPorLaboratorio(Long idLaboratorio) {
        return funcionarioDao.findAll().stream()
                .filter(f -> Objects.equals(f.getIdLaboratorio(), idLaboratorio))
                .collect(Collectors.toList());
    }

    public List<Funcionario> listarAtivosPorCargo(String cargo) {
        validarCargo(cargo);
        return funcionarioDao.findAll().stream()
                .filter(f -> "S".equalsIgnoreCase(f.getAtivo()))
                .filter(f -> cargo.equalsIgnoreCase(f.getCargo()))
                .collect(Collectors.toList());
    }

    // ========= VALIDAÇÕES & HELPERS =========

    private void exigirAdmin(Funcionario f) {
        if (f == null || !("ADMIN".equalsIgnoreCase(f.getCargo())))
            throw new RuntimeException("Ação restrita a ADMIN.");
    }

    private void exigirAdminOuGestor(Funcionario f) {
        if (f == null || !( "ADMIN".equalsIgnoreCase(f.getCargo()) || "GESTOR".equalsIgnoreCase(f.getCargo()) ))
            throw new RuntimeException("Ação restrita a ADMIN ou GESTOR.");
    }

    private void validarNovoFuncionario(Funcionario f) {
        if (f == null) throw new RuntimeException("Dados do funcionário são obrigatórios.");
        exigir(nf(f.getNome()), "nome");
        exigir(nf(f.getCpf()),  "cpf");
        exigir(nf(f.getEmail()),"email");
        exigir(nf(f.getCargo()),"cargo");
        if (f.getIdLaboratorio() == null) throw new RuntimeException("laboratorio_id é obrigatório.");
        validarEmail(f.getEmail());
        validarCargo(f.getCargo());
        validarAtivo(f.getAtivo()); // aceita null (default setado no cadastrar)
    }

    private void exigirEmailUnico(String email, Long ignorarIdFuncionario) {
        String alvo = normalizar(email);
        boolean existe = funcionarioDao.findAll().stream().anyMatch(f -> {
            if (f.getEmail() == null) return false;
            boolean mesmoEmail = normalizar(f.getEmail()).equals(alvo);
            boolean mesmoId = ignorarIdFuncionario != null && Objects.equals(f.getIdFuncionario(), ignorarIdFuncionario);
            return mesmoEmail && !mesmoId;
        });
        if (existe) throw new RuntimeException("E-mail já cadastrado: " + email);
    }

    private void validarEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new RuntimeException("E-mail inválido: " + email);
    }

    private void validarCargo(String cargo) {
        if (!( "ENFERMEIRO".equalsIgnoreCase(cargo) ||
                "ADMIN".equalsIgnoreCase(cargo) ||
                "GESTOR".equalsIgnoreCase(cargo))) {
            throw new RuntimeException("Cargo inválido. Use: ENFERMEIRO, ADMIN ou GESTOR.");
        }
    }

    private void validarAtivo(String ativo) {
        if (ativo == null) return; // será default "S" no cadastro
        if (!( "S".equalsIgnoreCase(ativo) || "N".equalsIgnoreCase(ativo) )) {
            throw new RuntimeException("Campo 'ativo' deve ser 'S' ou 'N'.");
        }
    }

    private Funcionario obterObrigatorio(Long id) {
        Funcionario f = funcionarioDao.findById(id);
        if (f == null) throw new RuntimeException("Funcionário não encontrado: " + id);
        return f;
    }

    private static String normalizar(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    private static boolean naoVazio(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static void exigir(boolean condicao, String campo) {
        if (!condicao) throw new RuntimeException("Campo obrigatório: " + campo);
    }

    private static boolean nf(String s) { // not blank
        return s != null && !s.trim().isEmpty();
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
}
