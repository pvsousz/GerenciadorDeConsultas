package com.gestao.consultas.service;

import com.gestao.consultas.model.Medico;
import com.gestao.consultas.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Medico cadastrarMedico(String nome, String crm, String cpf, String telefone, String email, String especialidade) {
        Medico medico = new Medico();
        medico.setNomeCompleto(nome);
        medico.setCrm(crm);
        medico.setCpf(cpf);
        medico.setTelefone(telefone);
        medico.setEmail(email);
        medico.setEspecialidade(especialidade);
        return medicoRepository.save(medico);
    }

    public Medico atualizarMedico(String crm, String nome, String telefone, String email, String especialidade) {
        Medico medico = medicoRepository.findByCrm(crm);
        if (medico != null) {
            medico.setNomeCompleto(nome);
            medico.setTelefone(telefone);
            medico.setEmail(email);
            medico.setEspecialidade(especialidade);
            return medicoRepository.save(medico);
        }
        return null;
    }

    public void deletarMedico(String crm) {
        Medico medico = medicoRepository.findByCrm(crm);
        if (medico != null) {
            medicoRepository.delete(medico);
        }
    }

    public Medico buscarMedicoPorCrm(String crm) {
        return medicoRepository.findByCrm(crm);
    }
}