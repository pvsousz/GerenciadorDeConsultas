package com.gestao.consultas.cli;

import com.gestao.consultas.model.Medico;
import com.gestao.consultas.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MedicoCLI {

    @Autowired
    private MedicoService medicoService;

    private final Scanner scanner = new Scanner(System.in);

    public void cadastrarMedico() {
        System.out.println("=== CADASTRAR MÉDICO ===");
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();
        System.out.print("CRM: ");
        String crm = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Especialidade: ");
        String especialidade = scanner.nextLine();

        medicoService.cadastrarMedico(nome, crm, cpf, telefone, email, especialidade);
        System.out.println("Médico cadastrado com sucesso!");
    }

    public void listarMedicos() {
        System.out.println("=== LISTA DE MÉDICOS ===");
        medicoService.listarMedicos().forEach(medico -> {
            System.out.println("ID: " + medico.getId());
            System.out.println("Nome: " + medico.getNomeCompleto());
            System.out.println("CRM: " + medico.getCrm());
            System.out.println("CPF: " + medico.getCpf());
            System.out.println("Telefone: " + medico.getTelefone());
            System.out.println("E-mail: " + medico.getEmail());
            System.out.println("Especialidade: " + medico.getEspecialidade());
            System.out.println("-----------------------------");
        });
    }

    public void atualizarMedico() {

        String crm;
        Medico medico;
        System.out.println("=== ATUALIZAR MÉDICO ===");
        do{
            System.out.print("CRM do Médico: ");
            crm = scanner.nextLine();
            medico = medicoService.buscarMedicoPorCrm(crm);
            if (medico == null) {
                System.out.println("Médico não encontrado.");
            }

        }while (medico == null);

        System.out.print("Novo Nome Completo: ");
        String nome = scanner.nextLine();
        System.out.print("Novo Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Novo E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Nova Especialidade: ");
        String especialidade = scanner.nextLine();
        System.out.println("Confirma a atualização? (S/N)");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }
        medicoService.atualizarMedico(crm, nome, telefone, email, especialidade);
        System.out.println("Médico atualizado com sucesso!");
    }

    public void deletarMedico() {
        String crm;
        Medico medico;
        System.out.println("=== DELETAR MÉDICO ===");

        do{
            System.out.print("CRM do Médico: ");
            crm = scanner.nextLine();
            medico = medicoService.buscarMedicoPorCrm(crm);
            if (medico == null) {
                System.out.println("Médico não encontrado.");
            }

        }while (medico == null);

        medicoService.deletarMedico(crm);
        System.out.println("Médico deletado com sucesso!");
    }
}