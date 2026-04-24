package com.aijw.cuidarplus.repository;

import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByIdAndStatusEquals(Long id, Servico.StatusServico status);

    @Query("""
    SELECT s
    FROM Servico s
    WHERE
        (:prestador IS NULL OR s.contratado = :prestador)
        AND (
            (:busca IS NULL OR LOWER(s.descricao) LIKE LOWER(CONCAT('%', :busca, '%')))
            OR (:busca IS NULL OR LOWER(s.contratante.nome) LIKE LOWER(CONCAT('%', :busca, '%')))
            OR (:busca IS NULL OR LOWER(s.contratante.email) LIKE LOWER(CONCAT('%', :busca, '%')))
            OR (:busca IS NULL OR LOWER(s.contratante.endereco) LIKE LOWER(CONCAT('%', :busca, '%')))
        )
        AND (:status IS NULL OR s.status IN :status)
    """)
    Page<Servico> buscarServicosPorFiltro(
            @Param("prestador") Prestador prestador,
            @Param("busca") String busca,
            @Param("status") Set<Servico.StatusServico> status,
            Pageable pageable
    );
}
