package com.aijw.cuidarplus.mapper;

import com.aijw.cuidarplus.dto.servico.MaterialServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.MaterialServicoDTO;
import com.aijw.cuidarplus.model.MaterialServico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialServicoMapper {
    MaterialServico map(MaterialServicoCreateDTO dto);

    MaterialServicoDTO map(MaterialServico materialServico);
}
