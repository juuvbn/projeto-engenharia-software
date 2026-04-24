package com.aijw.cuidarplus.dto.servico;

import com.aijw.cuidarplus.dto.cliente.ClienteDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorResumoDTO;
import com.aijw.cuidarplus.model.Servico;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ServicoDTO {
    private Long id;

    private ClienteDTO contratante;

    private PrestadorResumoDTO contratado;

    private String descricao;

    private Instant dataHorario;

    private BigDecimal valor;

    private Servico.StatusServico status;
}
