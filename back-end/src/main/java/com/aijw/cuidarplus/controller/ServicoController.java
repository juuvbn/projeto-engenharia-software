package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.servico.ProporDataDTO;
import com.aijw.cuidarplus.dto.servico.ServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoDTO;
import com.aijw.cuidarplus.dto.servico.ServicoPropostaDTO;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import com.aijw.cuidarplus.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {
    private final ServicoService servicoService;

    @PostMapping("/criar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ServicoDTO> proporServico(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestBody @Valid ServicoCreateDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criarPropostaInicial(principal, request));
    }

    @PostMapping("/{servicoId}/aceitar")
    @PreAuthorize("hasRole('PRESTADOR')")
    public ResponseEntity<ServicoDTO> aceitarProposta(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable Long servicoId,
            @RequestBody @Valid ServicoPropostaDTO request
    ) {
        return ResponseEntity.ok(servicoService.aceitarProposta(principal, servicoId, request));
    }

    @PostMapping("/{servicoId}/negar")
    @PreAuthorize("hasRole('PRESTADOR')")
    public ResponseEntity<ServicoDTO> negarProposta(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable Long servicoId
    ) {
        return ResponseEntity.ok(servicoService.negarProposta(principal, servicoId));
    }

    @PostMapping("/{servicoId}/confirmar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ServicoDTO> confirmarServico(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable Long servicoId
    ) {
        return ResponseEntity.ok(servicoService.confirmarServico(principal, servicoId));
    }

    @PostMapping("/{servicoId}/recusar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ServicoDTO> recusarServico(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable Long servicoId
    ) {
        return ResponseEntity.ok(servicoService.recusarServico(principal, servicoId));
    }

    @PostMapping("/{servicoId}/propor-data")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ServicoDTO> proporData(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable Long servicoId,
            @RequestBody @Valid ProporDataDTO request
    ) {
        return ResponseEntity.ok(servicoService.proporData(principal, servicoId, request));
    }
}
