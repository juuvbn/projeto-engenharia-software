package com.aijw.cuidarplus.dto.servico;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MaterialServicoDTO {
    private Long id;
    private String produto;
    private Integer quantidade;
    private BigDecimal valor;
}
