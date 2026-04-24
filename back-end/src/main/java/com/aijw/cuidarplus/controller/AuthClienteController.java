package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.auth.AuthResponseDTO;
import com.aijw.cuidarplus.dto.auth.ClienteRegisterRequestDTO;
import com.aijw.cuidarplus.dto.auth.LoginRequestDTO;
import com.aijw.cuidarplus.service.ClienteAuthService;
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
@RequestMapping("/auth/clientes")
@RequiredArgsConstructor
@Tag(name = "Auth Clientes", description = "Endpoints de cadastro e autenticação de clientes")
public class AuthClienteController {
    private final ClienteAuthService clienteAuthService;

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponseDTO> registrar(@RequestBody @Valid ClienteRegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteAuthService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(clienteAuthService.autenticar(request.getEmail(), request.getSenha()));
    }
}

