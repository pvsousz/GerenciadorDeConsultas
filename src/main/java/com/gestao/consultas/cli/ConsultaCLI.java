package com.gestao.consultas.cli;

import com.gestao.consultas.model.Consulta;
import com.gestao.consultas.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ConsultaCLI {

    @Autowired
    private ConsultaService consultaService;

    private final Scanner scanner = new Scanner(System.in);

    public void cadastrarConsulta() {
        System.out.println("=== CADASTRAR CONSULTA ===");
        consultaService.cadastrarConsultaCli();

    }

    public void listarConsultas() {
        System.out.println("=== LISTA DE CONSULTAS ===");
        consultaService.listarConsultas().forEach(consulta -> {
            System.out.println("ID: " + consulta.getId());
            System.out.println("CRM do Médico: " + consulta.getCrmMedico());
            System.out.println("Data: " + consulta.getDataConsulta());
            System.out.println("Horário: " + consulta.getHorarioConsulta());
            System.out.println("CPF do Paciente: " + consulta.getCpfPaciente());
            System.out.println("Confirmada: " + consulta.isConfirmada());
            System.out.println("-----------------------------");
        });
    }

    public void atualizarConsulta() {
        System.out.println("=== ATUALIZAR CONSULTA ===");
        consultaService.atualizarConsultaCli();

    }

    public void deletarConsulta() {
        Long id;
        Consulta consulta;

        System.out.println("=== DELETAR CONSULTA ===");

        while (true) {
            try {
                System.out.print("ID da Consulta: ");
                id = scanner.nextLong();
                scanner.nextLine();

                consulta = consultaService.buscarConsultaPorId(id);
                if (consulta == null) {
                    System.out.println("Consulta não encontrada.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número válido.");
                scanner.nextLine();
            }
        }

        consultaService.deletarConsulta(id);
        System.out.println("Consulta deletada com sucesso!");
    }
}