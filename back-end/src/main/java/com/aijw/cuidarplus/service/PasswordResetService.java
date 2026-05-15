package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.PasswordResetToken;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.repository.PasswordResetTokenRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenRepository tokenRepository;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void solicitarRecuperacao(String email, TipoUsuario tipoUsuario) {
        boolean exists = tipoUsuario == TipoUsuario.CLIENTE
                ? clienteRepository.existsByEmailIgnoreCase(email)
                : prestadorRepository.existsByEmailIgnoreCase(email);

        // Resposta silenciosa para evitar enumeração de e-mails
        if (!exists) {
            return;
        }

        tokenRepository.deleteByEmailIgnoreCaseAndTipoUsuario(email, tipoUsuario);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setEmail(email.toLowerCase());
        resetToken.setTipoUsuario(tipoUsuario);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));

        tokenRepository.saveAndFlush(resetToken);

        // Em produção este token seria enviado por e-mail
        log.info("[RECUPERAÇÃO DE SENHA] Token gerado para {} ({}): {}", email, tipoUsuario, resetToken.getToken());
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha, String confirmacaoNovaSenha) {
        if (!novaSenha.equals(confirmacaoNovaSenha)) {
            throw new IllegalArgumentException("As senhas não conferem");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado"));

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("Token já utilizado");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado");
        }

        String encodedPassword = passwordEncoder.encode(novaSenha);

        if (resetToken.getTipoUsuario() == TipoUsuario.CLIENTE) {
            Cliente cliente = clienteRepository.findByEmailIgnoreCase(resetToken.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            cliente.setSenha(encodedPassword);
            clienteRepository.save(cliente);
        } else {
            Prestador prestador = prestadorRepository.findByEmailIgnoreCase(resetToken.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            prestador.setSenha(encodedPassword);
            prestadorRepository.save(prestador);
        }

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
