package com.aijw.cuidarplus.dto.servico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MaterialServicoCreateDTO {
    @NotBlank(message = "O produto é obrigatório")
    private String produto;
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser um número positivo")
    private Integer quantidade;
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser um número positivo")
    private BigDecimal valor;
}
