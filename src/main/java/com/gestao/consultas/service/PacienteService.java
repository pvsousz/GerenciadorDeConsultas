package com.gestao.consultas.service;

import com.gestao.consultas.model.Paciente;
import com.gestao.consultas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente cadastrarPaciente(String nome, String cpf, String dataNascimento, String telefone, String email, String planoSaude) {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto(nome);
        paciente.setCpf(cpf);
        paciente.setDataNascimento(dataNascimento);
        paciente.setTelefone(telefone);
        paciente.setEmail(email);
        paciente.setPlanoSaude(planoSaude);
        return pacienteRepository.save(paciente);
    }

    public Paciente atualizarPaciente(String cpf, String nome, String telefone, String email, String planoSaude) {
        Paciente paciente = pacienteRepository.findByCpf(cpf);
        if (paciente != null) {
            paciente.setNomeCompleto(nome);
            paciente.setTelefone(telefone);
            paciente.setEmail(email);
            paciente.setPlanoSaude(planoSaude);
            return pacienteRepository.save(paciente);
        }
        return null;
    }

    public void deletarPaciente(String cpf) {
        Paciente paciente = pacienteRepository.findByCpf(cpf);
        if (paciente != null) {
            pacienteRepository.delete(paciente);
        }
    }

    public Paciente buscarPacientePorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf);
    }
}