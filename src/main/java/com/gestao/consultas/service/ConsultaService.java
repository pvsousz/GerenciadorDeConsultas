package com.gestao.consultas.service;

import com.gestao.consultas.model.Consulta;
import com.gestao.consultas.model.Medico;
import com.gestao.consultas.model.Paciente;
import com.gestao.consultas.repository.ConsultaRepository;
import com.gestao.consultas.repository.MedicoRepository;
import com.gestao.consultas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@Service
public class ConsultaService {

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

    public List<Consulta> listarConsultasPorCrm(String crmMedico) {
        return consultaRepository.findByCrmMedico(crmMedico);
    }

    public void cadastrarConsultaCli(){
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("CRM do Médico: ");
            String crmMedico = scanner.nextLine();


            LocalDate dataConsulta = null;
            boolean dataValida = false;
            while (!dataValida) {
                try {
                    System.out.print("Data da Consulta (dd/MM/yyyy): ");
                    String dataStr = scanner.nextLine();
                    dataConsulta = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Data inválida. Por favor, insira a data no formato dd/MM/yyyy.");
                }
            }


            LocalTime horarioConsulta = null;
            boolean horarioValido = false;
            while (!horarioValido) {
                try {
                    System.out.print("Horário da Consulta (HH:mm): ");
                    String horarioStr = scanner.nextLine();
                    horarioConsulta = LocalTime.parse(horarioStr, DateTimeFormatter.ofPattern("HH:mm"));
                    horarioValido = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Horário inválido. Por favor, insira o horário no formato HH:mm.");
                }
            }

            System.out.print("CPF do Paciente: ");
            String cpfPaciente = scanner.nextLine();


            Consulta consulta = cadastrarConsulta(crmMedico, dataConsulta, horarioConsulta, cpfPaciente);
            System.out.println("Consulta cadastrada com sucesso! ID: " + consulta.getId());

        } catch (RuntimeException e) {
            System.out.println("Erro ao cadastrar consulta: " + e.getMessage());
        }
    }


    public Consulta cadastrarConsulta(String crmMedico, LocalDate dataConsulta, LocalTime horarioConsulta, String cpfPaciente) {

        Medico medico = medicoRepository.findByCrm(crmMedico);
        Paciente paciente = pacienteRepository.findByCpf(cpfPaciente);

        if (medico == null && paciente == null) {
            throw new RuntimeException("Médico com CRM " + crmMedico + " e Paciente com CPF " + cpfPaciente + " não encontrados.");
        } else if (medico == null) {
            throw new RuntimeException("Médico com CRM " + crmMedico + " não encontrado.");
        } else if (paciente == null) {
            throw new RuntimeException("Paciente com CPF " + cpfPaciente + " não encontrado.");
        }

        if (existeConflitoDeHorario(crmMedico, dataConsulta, horarioConsulta, null)) {
            throw new RuntimeException("Conflito de horário: Já existe uma consulta para este médico na mesma data e horário.");
        }

        String identificadorUnico = gerarIdentificadorUnico(crmMedico, dataConsulta, horarioConsulta, cpfPaciente);


        Consulta consulta = new Consulta();
        consulta.setCrmMedico(crmMedico);
        consulta.setDataConsulta(dataConsulta);
        consulta.setHorarioConsulta(horarioConsulta);
        consulta.setCpfPaciente(cpfPaciente);
        consulta.setConfirmada(false);
        consulta.setIdentificadorUnico(identificadorUnico);

        return consultaRepository.save(consulta);
    }

    public void atualizarConsultaCli(){
        Scanner scanner = new Scanner(System.in);

        try {

            System.out.print("ID da Consulta: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Novo CRM do Médico: ");
            String crmMedico = scanner.nextLine();


            LocalDate dataConsulta = null;
            boolean dataValida = false;
            while (!dataValida) {
                try {
                    System.out.print("Nova Data da Consulta (dd/MM/yyyy): ");
                    String dataStr = scanner.nextLine();
                    dataConsulta = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Data inválida. Por favor, insira a data no formato dd/MM/yyyy.");
                }
            }


            LocalTime horarioConsulta = null;
            boolean horarioValido = false;
            while (!horarioValido) {
                try {
                    System.out.print("Novo Horário da Consulta (HH:mm): ");
                    String horarioStr = scanner.nextLine();
                    horarioConsulta = LocalTime.parse(horarioStr, DateTimeFormatter.ofPattern("HH:mm"));
                    horarioValido = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Horário inválido. Por favor, insira o horário no formato HH:mm.");
                }
            }


            System.out.print("Novo CPF do Paciente: ");
            String cpfPaciente = scanner.nextLine();


            System.out.print("Confirmada (S/N): ");
            String confirmada = scanner.nextLine();


            if (!confirmada.equalsIgnoreCase("S")) {
                System.out.println("Operação cancelada.");
                return;
            }


            Consulta consultaAtualizada = atualizarConsulta(id, crmMedico, dataConsulta, horarioConsulta, cpfPaciente, true);
            System.out.println("Consulta atualizada com sucesso! ID: " + consultaAtualizada.getId());

        } catch (RuntimeException e) {
            System.out.println("Erro ao atualizar consulta: " + e.getMessage());
        }
    }

    public Consulta atualizarConsulta(Long id, String crmMedico, LocalDate dataConsulta, LocalTime horarioConsulta, String cpfPaciente, boolean confirmada) {

        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));


        Medico medico = medicoRepository.findByCrm(crmMedico);
        Paciente paciente = pacienteRepository.findByCpf(cpfPaciente);

        if (medico == null && paciente == null) {
            throw new RuntimeException("Médico com CRM " + crmMedico + " e Paciente com CPF " + cpfPaciente + " não encontrados.");
        } else if (medico == null) {
            throw new RuntimeException("Médico com CRM " + crmMedico + " não encontrado.");
        } else if (paciente == null) {
            throw new RuntimeException("Paciente com CPF " + cpfPaciente + " não encontrado.");
        }


        if (existeConflitoDeHorario(crmMedico, dataConsulta, horarioConsulta, id)) {
            throw new RuntimeException("Conflito de horário: Já existe uma consulta para este médico na mesma data e horário.");
        }


        consulta.setCrmMedico(crmMedico);
        consulta.setDataConsulta(dataConsulta);
        consulta.setHorarioConsulta(horarioConsulta);
        consulta.setCpfPaciente(cpfPaciente);
        consulta.setConfirmada(confirmada);


        String novoIdentificador = gerarIdentificadorUnico(crmMedico, dataConsulta, horarioConsulta, cpfPaciente);
        consulta.setIdentificadorUnico(novoIdentificador);


        return consultaRepository.save(consulta);
    }


    public void deletarConsulta(Long id) {
        consultaRepository.deleteById(id);
    }


    public boolean existeConflitoDeHorario(String crmMedico, LocalDate dataConsulta, LocalTime horarioConsulta, Long idConsultaAtual) {
        List<Consulta> consultas = consultaRepository.findByCrmMedicoAndDataConsulta(crmMedico, dataConsulta);

        for (Consulta consulta : consultas) {

            if (idConsultaAtual == null || !consulta.getId().equals(idConsultaAtual)) {
                LocalTime horarioInicioConsultaExistente = consulta.getHorarioConsulta();
                LocalTime horarioFimConsultaExistente = horarioInicioConsultaExistente.plusMinutes(30);

                LocalTime horarioInicioNovaConsulta = horarioConsulta;
                LocalTime horarioFimNovaConsulta = horarioInicioNovaConsulta.plusMinutes(30);


                if (horarioInicioNovaConsulta.isBefore(horarioFimConsultaExistente) &&
                        horarioFimNovaConsulta.isAfter(horarioInicioConsultaExistente)) {
                    return true;
                }
            }
        }

        return false;
    }


    public Consulta confirmarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));

        consulta.setConfirmada(true);
        return consultaRepository.save(consulta);
    }

    public Consulta buscarConsultaPorId(Long id) {
        return consultaRepository.findById(id).orElse(null);
    }


    private String gerarIdentificadorUnico(String crmMedico, LocalDate dataConsulta, LocalTime horarioConsulta, String cpfPaciente) {
        String dataFormatada = dataConsulta.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String horarioFormatado = horarioConsulta.format(DateTimeFormatter.ofPattern("HH-mm"));
        return crmMedico + "-" + dataFormatada + "-" + horarioFormatado + "-" + cpfPaciente;
    }

}