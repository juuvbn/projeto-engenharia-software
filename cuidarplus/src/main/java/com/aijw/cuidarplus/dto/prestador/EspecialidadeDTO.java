package com.aijw.cuidarplus.dto.prestador;

import com.aijw.cuidarplus.model.Especialidade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EspecialidadeDTO {
    private Long id;
    private Especialidade.EspecialidadeEnum especialidade;
}
