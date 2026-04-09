package com.aijw.cuidarplus.dto.prestador;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PrestadorDTO {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private Set<EspecialidadeDTO> especialidades;
}
