package com.gestao.consultas.repository;

import com.gestao.consultas.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByCpf(String cpf);
}