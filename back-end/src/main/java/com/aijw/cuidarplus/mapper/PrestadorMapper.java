package com.aijw.cuidarplus.mapper;

import com.aijw.cuidarplus.dto.prestador.EspecialidadeDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorCreateDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorUpdateDTO;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrestadorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "especialidades", ignore = true)
    Prestador map(PrestadorCreateDTO prestadorCreateDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "especialidades", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(PrestadorUpdateDTO request, @MappingTarget Prestador prestador);

    PrestadorDTO map(Prestador prestador);

    EspecialidadeDTO map(Especialidade especialidade);
}
