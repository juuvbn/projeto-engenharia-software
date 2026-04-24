package com.aijw.cuidarplus.mapper;

import com.aijw.cuidarplus.dto.cliente.ClienteDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteUpdateDTO;
import com.aijw.cuidarplus.model.Cliente;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "servicos", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "tipoUsuario", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(ClienteUpdateDTO request, @MappingTarget Cliente cliente);

    ClienteDTO map(Cliente cliente);
}

