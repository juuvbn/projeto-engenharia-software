package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.servico.ServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoDTO;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import com.aijw.cuidarplus.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {
    private final ServicoService servicoService;

    @PostMapping("/propor")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ServicoDTO> proporServico(
            @AuthenticationPrincipal AuthenticatedUserPrincipal principal,
            @RequestBody @Valid ServicoCreateDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criarPropostaInicial(principal, request));
    }
}
