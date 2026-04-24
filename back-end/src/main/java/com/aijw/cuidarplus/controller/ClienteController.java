package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.auth.PasswordChangeRequestDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteUpdateDTO;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import com.aijw.cuidarplus.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteDTO> buscarPerfilAutenticado(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal
    ) {
        return ResponseEntity.ok(clienteService.buscarPerfilAutenticado(principal));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteDTO> atualizarCliente(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestBody @Valid ClienteUpdateDTO request
    ) {
        return ResponseEntity.ok(clienteService.atualizarCliente(principal, request));
    }

    @PatchMapping("/me/senha")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> atualizarSenha(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestBody @Valid PasswordChangeRequestDTO request
    ) {
        clienteService.atualizarSenha(principal, request);

        return ResponseEntity.noContent().build();
    }
}