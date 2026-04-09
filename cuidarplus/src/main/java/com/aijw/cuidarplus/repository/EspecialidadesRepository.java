package com.aijw.cuidarplus.repository;

import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadesRepository extends JpaRepository<Especialidade, Long> {
    boolean existsByPrestadorAndEspecialidadeIn(Prestador prestador, List<Especialidade.EspecialidadeEnum> especialidade);
}
