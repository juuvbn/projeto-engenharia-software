package com.aijw.cuidarplus.dto.prestador;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(name = "PrestadorDTO", description = "DTO de Prestador para transferência de dados")
public class PrestadorDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Maria Silva")
    private String nome;
    @Schema(example = "12345678900")
    private String telefone;
    @Schema(example = "mariasilva@gmail.com")
    private String email;
    private Set<EspecialidadeDTO> especialidades;
}
