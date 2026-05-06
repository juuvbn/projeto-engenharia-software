package com.aijw.cuidarplus.dto.servico;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ProporDataDTO {
    @NotNull(message = "A data/horário é obrigatória")
    @Future(message = "A data proposta deve ser futura")
    private Instant dataHorario;
}
