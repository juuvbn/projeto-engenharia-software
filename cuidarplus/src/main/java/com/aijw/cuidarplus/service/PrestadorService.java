package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.prestador.PrestadorCreateDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.mapper.PrestadorMapper;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.repository.EspecialidadesRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrestadorService {
    private final PrestadorRepository prestadorRepository;
    private final EspecialidadesRepository especialidadesRepository;

    private final PrestadorMapper prestadorMapper;

    @Transactional(readOnly=true)
    public Page<PrestadorDTO> buscarPrestadores(Set<Especialidade.EspecialidadeEnum> especialidades, Pageable pageable) {
        var prestadores = prestadorRepository.buscarPrestadoresPorFiltro(especialidades, pageable);

        return prestadores.map(prestadorMapper::map);
    }

    @Transactional
    public PrestadorDTO registrarPrestador(PrestadorCreateDTO prestadorCreateDTO) {
        Prestador prestador = criarPrestador(prestadorCreateDTO);

        Prestador saved = prestadorRepository.saveAndFlush(prestador);
        log.info("Prestador {} registrado com sucesso", saved);

        return prestadorMapper.map(saved);
    }

    private Prestador criarPrestador(PrestadorCreateDTO prestadorCreateDTO) {
        Prestador prestador = prestadorMapper.map(prestadorCreateDTO);

        List<Especialidade> especialidades = extrairEspecialidades(prestadorCreateDTO, prestador);
        prestador.setEspecialidades(especialidades);

        return prestador;
    }

    private static List<Especialidade> extrairEspecialidades(PrestadorCreateDTO prestadorCreateDTO, Prestador prestador) {
        List<Especialidade> especialidades = new ArrayList<>();

        for (Especialidade.EspecialidadeEnum especialidadeEnum: prestadorCreateDTO.getEspecialidades()) {
            Especialidade especialidade = new Especialidade();
            especialidade.setEspecialidade(especialidadeEnum);
            especialidade.setPrestador(prestador);
            especialidades.add(especialidade);
        }

        return especialidades;
    }
}
