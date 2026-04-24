package com.aijw.cuidarplus.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Maria Silva")
    private String nome;
    @Schema(example = "12345678900")
    private String telefone;
    @Schema(example = "mariasilva@gmail.com")
    private String email;
    @Schema(example = "Rua dos Bobos, 1")
    private String endereco;
}
