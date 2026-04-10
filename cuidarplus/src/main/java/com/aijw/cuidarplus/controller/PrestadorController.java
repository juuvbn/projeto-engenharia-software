package com.aijw.cuidarplus.controller;

import com.aijw.cuidarplus.dto.prestador.PrestadorCreateDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.service.PrestadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<PrestadorDTO> registrarPrestador(@RequestBody @Valid PrestadorCreateDTO prestadorCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestadorService.registrarPrestador(prestadorCreateDTO));
    }
}
