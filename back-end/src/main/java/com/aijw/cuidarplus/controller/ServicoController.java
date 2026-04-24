package com.aijw.cuidarplus.controller;

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

    @PostMapping("/aceitar/{servicoId}")
    @PreAuthorize("hasRole('PRESTADOR')")
    public ResponseEntity<ServicoDTO> aceitarProposta(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @PathVariable Long servicoId,
            @RequestBody @Valid ServicoPropostaDTO request
    ) {
        return ResponseEntity.ok(servicoService.aceitarProposta(principal, servicoId, request));
    }
}
