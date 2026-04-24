package com.aijw.cuidarplus.dto.servico;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ServicoPropostaDTO {
    private Instant dataHorario;
    private List<MaterialServicoCreateDTO> materiais;
}
