package main.java.com.stoq;

import main.java.com.stoq.domain.model.Funcionario;
import main.java.com.stoq.infra.dao.FuncionarioDao;

import java.time.LocalDate;

public class App {

    public static void main(String[] args) {

        Funcionario func = new Funcionario("Sofia", "0000000", "fofia", "ADMIN", "S", LocalDate.parse("2025-09-15"), 2);
        FuncionarioDao funcdao = new FuncionarioDao();

        funcdao.delete(12L);
        System.out.println("Funcionário deletado com sucesso!");

        Funcionario fBuscado = funcdao.findById(11L);

        if (fBuscado != null) {
            System.out.println("Funcionario encontrado: " + fBuscado.getNome() + " - " + fBuscado.getEmail());
        } else {
            System.out.println("Nenhum funcionário encontrado com ID 1");
        }
    }
}
