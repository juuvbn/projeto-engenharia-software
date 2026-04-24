package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.auth.PasswordChangeRequestDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorUpdateDTO;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import com.aijw.cuidarplus.service.PrestadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/prestadores")
@RequiredArgsConstructor
@Tag(name = "Prestadores", description = "Endpoints para gerenciamento de prestadores de serviços de saúde")
public class PrestadorController {
    private final PrestadorService prestadorService;

    @GetMapping
    public ResponseEntity<Page<PrestadorDTO>> buscarPrestadores(
            @RequestParam(required = false) Set<Especialidade.EspecialidadeEnum> especialidades,
            Pageable pageable
    ) {
        return ResponseEntity.ok(prestadorService.buscarPrestadores(especialidades, pageable));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PRESTADOR')")
    public ResponseEntity<PrestadorDTO> buscarPerfilAutenticado(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal
    ) {
        return ResponseEntity.ok(prestadorService.buscarPerfilAutenticado(principal));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('PRESTADOR')")
    public ResponseEntity<PrestadorDTO> atualizarPrestador(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestBody PrestadorUpdateDTO prestadorDTO
    ) {
        return ResponseEntity.ok(prestadorService.atualizarPrestador(principal, prestadorDTO));
    }

    @PatchMapping("/me/senha")
    @PreAuthorize("hasRole('PRESTADOR')")
    public ResponseEntity<Void> atualizarSenha(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestBody PasswordChangeRequestDTO request
    ) {
        prestadorService.alterarSenha(principal, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
