package com.gestao.consultas.service;

import com.gestao.consultas.model.Consulta;
import com.gestao.consultas.repository.ConsultaRepository;
import com.gestao.consultas.repository.MedicoRepository;
import com.gestao.consultas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisualizarService {

    @Autowired ConsultaService consultaService;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    public List<Consulta> listarConsultasPorCpf(String cpfPaciente) {
        return consultaRepository.findByCpfPaciente((cpfPaciente));
    }


    public void visualizarConsultaPacienteService(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== MODO PACIENTE ===");
        System.out.print("Digite o CPF do paciente: ");
        String cpfPaciente = scanner.nextLine();


        List<Consulta> consultas = listarConsultasPorCpf(cpfPaciente);

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada para o CPF informado.");
            return;
        }


        System.out.println("=== CONSULTAS DO PACIENTE ===");
        for (Consulta consulta : consultas) {
            System.out.println("ID: " + consulta.getId());
            System.out.println("Médico: " + consulta.getCrmMedico());
            System.out.println("Data: " + consulta.getDataConsulta());
            System.out.println("Horário: " + consulta.getHorarioConsulta());
            System.out.println("Status de Confirmação: " + (consulta.isConfirmada() ? "Confirmada" : "Não confirmada"));
            System.out.println("-----------------------------");
        }


        System.out.print("Deseja confirmar alguma consulta? (S/N): ");
        String confirmar = scanner.nextLine();

        if (confirmar!=null && !confirmar.equalsIgnoreCase("S") && !confirmar.equalsIgnoreCase("N")) {
            System.out.println("Opção inválida! Tente novamente.");
            return;
        }


        if (confirmar.equalsIgnoreCase("S")) {
            System.out.print("Digite o ID da consulta que deseja confirmar: ");
            Long idConsulta;

            try {
                idConsulta = scanner.nextLong();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número válido.");
                scanner.nextLine();
                return;
            }

            try {
                Consulta consultaConfirmada = confirmarConsulta(idConsulta);
                System.out.println("Consulta confirmada com sucesso! ID: " + consultaConfirmada.getId());
            } catch (RuntimeException e) {
                System.out.println("Erro ao confirmar consulta: " + e.getMessage());
            }

        }else if (confirmar.equalsIgnoreCase("N")){
            System.out.print("Digite o ID da consulta que deseja cancelar: ");
            Long idConsulta = scanner.nextLong();
            scanner.nextLine();


            try{deletarConsulta(idConsulta);
                deletarConsulta(idConsulta);
                System.out.println("Consulta cancelada com sucesso! ID: " + idConsulta);
            }catch (RuntimeException e){
                System.out.println("Erro ao cancelar consulta: " + e.getMessage());
            }
        }
    }


    public void visualizarConsultaAdminService() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY);
        LocalDate fimSemana = hoje.with(DayOfWeek.SUNDAY);

        List<Consulta> consultas = consultaRepository.findByDataConsultaBetween(inicioSemana, fimSemana);

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada nesta semana.");
            return;
        }

        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, Map<String, List<Consulta>>> consultasAgrupadas = consultas.stream()
                .collect(Collectors.groupingBy(Consulta::getCrmMedico,
                        Collectors.groupingBy(Consulta::getCpfPaciente)));

        System.out.println("Consultas agendadas nesta semana:");

        consultasAgrupadas.forEach((crmMedico, pacientesMap) -> {
            System.out.println("\nMédico (CRM: " + crmMedico + "):");
            pacientesMap.forEach((cpfPaciente, consultasPaciente) -> {
                System.out.println("  Paciente (CPF: " + cpfPaciente + "):");


                List<Consulta> consultasOrdenadas = consultasPaciente.stream()
                        .sorted(Comparator.comparing(Consulta::getDataConsulta)
                                .thenComparing(Consulta::getHorarioConsulta))
                        .collect(Collectors.toList());

                consultasOrdenadas.forEach(consulta -> {
                    String dataFormatada = consulta.getDataConsulta().format(dataFormatter);
                    String horaFormatada = consulta.getHorarioConsulta().format(horaFormatter);
                    System.out.printf("    Data: %s | Horário: %s | Confirmada: %s%n",
                            dataFormatada, horaFormatada, consulta.isConfirmada() ? "Sim" : "Não");
                });
            });
        });
    }


    public void visualizarConsultasMedicoService() {
        System.out.println("=== MODO MEDICO ===");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o CRM do médico: ");
        String crmMedico = scanner.nextLine();
        LocalDate hoje = LocalDate.now();

        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY);

        LocalDate fimSemana = hoje.with(DayOfWeek.SUNDAY);

        List<Consulta> consultas = consultaRepository.findByCrmMedicoAndDataConsultaBetween(crmMedico, inicioSemana, fimSemana);


        if (medicoRepository.findByCrm(crmMedico) == null) {
            System.out.println("Médico com CRM " + crmMedico + " não encontrado.");
        } else if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada para o médico com CRM " + crmMedico + " nesta semana.");

        } else {
            System.out.println("Consultas do médico com CRM " + crmMedico + " nesta semana:");
            for (Consulta consulta : consultas) {
                System.out.println("Data: " + consulta.getDataConsulta() + ", Horário: " + consulta.getHorarioConsulta() +
                        ", Paciente: " + consulta.getCpfPaciente() + ", Confirmada: " + (consulta.isConfirmada() ? "Sim" : "Não"));
            }
        }
    }


    public Consulta confirmarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));

        consulta.setConfirmada(true);
        return consultaRepository.save(consulta);
    }


    public void deletarConsulta(Long id) {
        consultaRepository.deleteById(id);
    }
}
