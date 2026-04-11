package com.aijw.cuidarplus.dto.prestador;

import com.aijw.cuidarplus.model.Especialidade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "EspecialidadeDTO", description = "DTO de Especialidade para transferência de dados")
public class EspecialidadeDTO {
    @Schema(example = "1")
    private Long id;
    private Especialidade.EspecialidadeEnum especialidade;
}
