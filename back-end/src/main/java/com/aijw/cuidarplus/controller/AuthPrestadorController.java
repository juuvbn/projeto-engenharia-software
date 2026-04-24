package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.auth.AuthResponseDTO;
import com.aijw.cuidarplus.dto.auth.LoginRequestDTO;
import com.aijw.cuidarplus.dto.auth.PrestadorRegisterRequestDTO;
import com.aijw.cuidarplus.service.PrestadorAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/prestadores")
@RequiredArgsConstructor
@Tag(name = "Auth Prestadores", description = "Endpoints de cadastro e autenticação de prestadores")
public class AuthPrestadorController {
    private final PrestadorAuthService prestadorAuthService;

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponseDTO> registrar(@RequestBody @Valid PrestadorRegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestadorAuthService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(prestadorAuthService.autenticar(request.getEmail(), request.getSenha()));
    }
}

