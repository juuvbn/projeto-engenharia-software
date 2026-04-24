package com.aijw.cuidarplus.mapper;

import com.aijw.cuidarplus.dto.servico.ServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoDTO;
import com.aijw.cuidarplus.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contratante", ignore = true)
    @Mapping(target = "contratado", ignore = true)
    @Mapping(target = "materiais", ignore = true)
    Servico map(ServicoCreateDTO dto);

    ServicoDTO map(Servico servico);
}
