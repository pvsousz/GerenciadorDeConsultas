package com.gestao.consultas.repository;

import com.gestao.consultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByCrmMedicoAndDataConsulta(String crmMedico, LocalDate dataConsulta);
    List<Consulta> findByCpfPaciente(String cpfPaciente);
    List<Consulta> findByCrmMedico(String crmMedico);
    List<Consulta> findByCrmMedicoAndDataConsultaBetween(String crmMedico, LocalDate startDate, LocalDate endDate);
    List<Consulta> findByDataConsultaBetween(LocalDate start, LocalDate end);

}