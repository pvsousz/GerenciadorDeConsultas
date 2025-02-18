package com.gestao.consultas.repository;

import com.gestao.consultas.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Medico findByCrm(String crm);
}