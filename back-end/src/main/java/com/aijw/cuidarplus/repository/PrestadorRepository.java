package com.aijw.cuidarplus.repository;

import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.Optional;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    Optional<Prestador> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByTelefoneIgnoreCase(String telefone);

    @Query("""
    SELECT p
    FROM Prestador p
    JOIN p.especialidades e
    WHERE e.especialidade IN :especialidades
    """)
    Page<Prestador> buscarPrestadoresPorFiltro(
            @Param("especialidades") Set<Especialidade.EspecialidadeEnum> especialidades,
            Pageable pageable
    );

    boolean existsByIdNotAndTelefoneOrEmailIgnoreCase(
            Long id,
            String telefone,
            String email
    );
}
