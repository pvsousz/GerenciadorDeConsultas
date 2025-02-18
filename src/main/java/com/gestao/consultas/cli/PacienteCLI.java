package com.gestao.consultas.cli;

import com.gestao.consultas.model.Paciente;
import com.gestao.consultas.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class PacienteCLI {

    @Autowired
    private PacienteService pacienteService;

    private final Scanner scanner = new Scanner(System.in);

    public void cadastrarPaciente() {
        System.out.println("=== CADASTRAR PACIENTE ===");
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Data de Nascimento (dd/MM/yyyy): ");
        String dataNascimento = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Plano de Saúde: ");
        String planoSaude = scanner.nextLine();

        pacienteService.cadastrarPaciente(nome, cpf, dataNascimento, telefone, email, planoSaude);
        System.out.println("Paciente cadastrado com sucesso!");
    }

    public void listarPacientes() {
        System.out.println("=== LISTA DE PACIENTES ===");
        pacienteService.listarPacientes().forEach(paciente -> {
            System.out.println("ID: " + paciente.getId());
            System.out.println("Nome: " + paciente.getNomeCompleto());
            System.out.println("CPF: " + paciente.getCpf());
            System.out.println("Telefone: " + paciente.getTelefone());
            System.out.println("E-mail: " + paciente.getEmail());
            System.out.println("Plano de Saúde: " + paciente.getPlanoSaude());
            System.out.println("-----------------------------");
        });
    }

    public void atualizarPaciente() {
        String cpf;
        Paciente paciente;

        System.out.println("=== ATUALIZAR PACIENTE ===");

        do{
            System.out.print("CPF do Paciente: ");
            cpf = scanner.nextLine();
            paciente = pacienteService.buscarPacientePorCpf(cpf);
            if (paciente == null) {
                System.out.println("Paciente não encontrado.");
            }

        }while (paciente == null);


        System.out.print("Novo Nome Completo: ");
        String nome = scanner.nextLine();
        System.out.print("Novo Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Novo E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Novo Plano de Saúde: ");
        String planoSaude = scanner.nextLine();

        pacienteService.atualizarPaciente(cpf, nome, telefone, email, planoSaude);
        System.out.println("Paciente atualizado com sucesso!");
    }

    public void deletarPaciente() {
        String cpf;
        Paciente paciente;

        System.out.println("=== DELETAR PACIENTE ===");

        do{
            System.out.print("CPF do Paciente: ");
            cpf = scanner.nextLine();
            paciente = pacienteService.buscarPacientePorCpf(cpf);
            if (paciente == null) {
                System.out.println("Paciente não encontrado.");
            }

        }while (paciente == null);

        pacienteService.deletarPaciente(cpf);
        System.out.println("Paciente deletado com sucesso!");
    }
}