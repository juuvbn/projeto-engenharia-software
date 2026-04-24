package com.aijw.cuidarplus.dto.prestador;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestadorResumoDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Maria Silva")
    private String nome;
    @Schema(example = "12345678900")
    private String telefone;
    @Schema(example = "mariasilva@gmail.com")
    private String email;
}
