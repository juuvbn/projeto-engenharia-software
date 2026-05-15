package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.auth.PasswordResetDTO;
import com.aijw.cuidarplus.dto.auth.PasswordResetRequestDTO;
import com.aijw.cuidarplus.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Recuperação de Senha", description = "Endpoints para recuperação e redefinição de senha")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @Operation(summary = "Solicita token de recuperação de senha por e-mail")
    @PostMapping("/recuperar-senha")
    public ResponseEntity<Void> solicitarRecuperacao(@RequestBody @Valid PasswordResetRequestDTO request) {
        passwordResetService.solicitarRecuperacao(request.getEmail(), request.getTipoUsuario());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Redefine a senha usando o token recebido")
    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid PasswordResetDTO request) {
        passwordResetService.redefinirSenha(request.getToken(), request.getNovaSenha(), request.getConfirmacaoNovaSenha());
        return ResponseEntity.ok().build();
    }
}
