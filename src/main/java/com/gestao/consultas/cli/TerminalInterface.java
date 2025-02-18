package com.gestao.consultas.cli;

import com.gestao.consultas.service.AutenticaService;
import com.gestao.consultas.service.ConsultaService;
import com.gestao.consultas.service.VisualizarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class TerminalInterface {

    @Autowired
    private PacienteCLI pacienteCLI;

    @Autowired
    private MedicoCLI medicoCLI;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ConsultaCLI consultaCLI;

    @Autowired
    private VisualizarService visualizarService;


    @Autowired
    private AutenticaService autenticaService;

    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {

        if (autenticaService.autenticarUsuario()) {
            exibirMenuPrincipal();
        } else {
            System.out.println("Autenticação falhou. Tente novamente.");
            autenticaService.autenticarUsuario();
      }
    }

    private void exibirMenuPrincipal() {
        int opcao = -1;
        try{

            while (opcao != 6) {
                System.out.println("=== MENU PRINCIPAL ===");
                System.out.println("1 - Gerenciar Pacientes");
                System.out.println("2 - Gerenciar Médicos");
                System.out.println("3 - Gerenciar Consultas");
                System.out.println("4 - Visualizar Consultas");
                System.out.println("5 - Trocar de Usuário");
                System.out.println("6 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1 -> menuPacientes();
                    case 2 -> menuMedicos();
                    case 3 -> menuConsultas();
                    case 4 -> visualizarConsultas();
                    case 5 -> iniciar();
                    case 6 -> {
                        System.out.println("Saindo do sistema...");
                        System.exit(0);}
                    default -> System.out.println("Opção inválida! Tente novamente.");

                }
            }
        }catch(InputMismatchException e){
            System.out.println("Entrada inválida! Tente novamente.");
            scanner.nextLine();
            exibirMenuPrincipal();
        }
    }


    private void menuPacientes() {

        if (!temPermissao("ADMIN")) {
            System.out.println("Acesso negado. Você não tem permissão para gerenciar pacientes.");
            return;
        }

        int opcao = -1;
        while (opcao != 6) {
            System.out.println("=== GERENCIAR PACIENTES ===");
            System.out.println("1 - Cadastrar Paciente");
            System.out.println("2 - Listar Pacientes");
            System.out.println("3 - Atualizar Paciente");
            System.out.println("4 - Deletar Paciente");
            System.out.println("5 - Voltar ao Menu Principal");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> pacienteCLI.cadastrarPaciente();
                case 2 -> pacienteCLI.listarPacientes();
                case 3 -> pacienteCLI.atualizarPaciente();
                case 4 -> pacienteCLI.deletarPaciente();
                case 5 -> { return;}
                case 6 -> {
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");

            }
        }
    }


    private void menuMedicos() {

        if (!temPermissao("ADMIN")) {
            System.out.println("Acesso negado. Você não tem permissão para gerenciar médicos.");
            return;
        }

        int opcao;
        while (true) {
            System.out.println("=== GERENCIAR MÉDICOS ===");
            System.out.println("1 - Cadastrar Médico");
            System.out.println("2 - Listar Médicos");
            System.out.println("3 - Atualizar Médico");
            System.out.println("4 - Deletar Médico");
            System.out.println("5 - Voltar ao Menu Principal");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> medicoCLI.cadastrarMedico();
                case 2 -> medicoCLI.listarMedicos();
                case 3 -> medicoCLI.atualizarMedico();
                case 4 -> medicoCLI.deletarMedico();
                case 5 -> {return;}
                case 6 ->{
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                }
                default ->  System.out.println("Opção inválida! Tente novamente.");

            }
        }
    }


    private void menuConsultas() {

        if (!temPermissao("ADMIN") ) {
            System.out.println("Acesso negado. Você não tem permissão para gerenciar consultas.");
            return;
        }

        int opcao;
        while (true) {
            System.out.println("=== GERENCIAR CONSULTAS ===");
            System.out.println("1 - Cadastrar Consulta");
            System.out.println("2 - Listar Consultas");
            System.out.println("3 - Atualizar Consulta");
            System.out.println("4 - Deletar Consulta");
            System.out.println("5 - Voltar ao Menu Principal");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> consultaCLI.cadastrarConsulta();
                case 2 -> consultaCLI.listarConsultas();
                case 3 -> consultaCLI.atualizarConsulta();
                case 4 -> consultaCLI.deletarConsulta();
                case 5 -> {return;}
                case 6 ->{
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");

            }
        }
    }


    private void visualizarConsultas() {
        if (temPermissao("ADMIN")) {
            visualizarConsultasAdmin();
        } else if (temPermissao("PACIENTE")) {
            visualizarConsultasPaciente();
        } else if (temPermissao("MEDICO")) {
            visualizarConsultasMedico();
        } else {
            System.out.println("Acesso negado. Role não reconhecida.");
        }
    }


    private void visualizarConsultasAdmin(){
        visualizarService.visualizarConsultaAdminService();
    }

    private void visualizarConsultasPaciente(){
        visualizarService.visualizarConsultaPacienteService();

    }

    private void visualizarConsultasMedico(){
        visualizarService.visualizarConsultasMedicoService();
    }

    private boolean temPermissao(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}