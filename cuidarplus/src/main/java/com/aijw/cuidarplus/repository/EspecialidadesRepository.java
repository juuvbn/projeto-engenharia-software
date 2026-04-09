package com.aijw.cuidarplus.repository;

import com.aijw.cuidarplus.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadesRepository extends JpaRepository<Especialidade, Long> {
}
