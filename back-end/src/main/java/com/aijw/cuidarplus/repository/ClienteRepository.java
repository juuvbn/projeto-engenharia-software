package com.aijw.cuidarplus.repository;

import com.aijw.cuidarplus.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByIdNotAndEmailIgnoreCase(Long id, String email);
}