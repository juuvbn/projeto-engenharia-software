package com.aijw.cuidarplus.repository;

import com.aijw.cuidarplus.model.PasswordResetToken;
import com.aijw.cuidarplus.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByEmailIgnoreCaseAndTipoUsuario(String email, TipoUsuario tipoUsuario);
}
