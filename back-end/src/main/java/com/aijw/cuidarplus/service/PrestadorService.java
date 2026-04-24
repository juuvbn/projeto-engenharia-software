package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.prestador.PrestadorCreateDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorUpdateDTO;
import com.aijw.cuidarplus.mapper.PrestadorMapper;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.repository.EspecialidadesRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public PrestadorDTO atualizarPrestador(AuthenticatedUserPrincipal principal, PrestadorUpdateDTO prestadorDTO) {
        Prestador prestador = buscarPrestadorPorIdOuFalhar(principal.getId());
        
        prestadorMapper.update(prestadorDTO, prestador);

        // Sincronizar especialidades: remover as deselacionadas e adicionar as novas
        sincronizarEspecialidades(prestador, prestadorDTO.getEspecialidades());

        Prestador saved = prestadorRepository.saveAndFlush(prestador);
        log.info("Prestador {} atualizado com sucesso", saved);

        return prestadorMapper.map(saved);
    }

    private void sincronizarEspecialidades(Prestador prestador, Set<Especialidade.EspecialidadeEnum> novasEspecialidades) {
        // Obter especialidades atuais como um Set de enums
        Set<Especialidade.EspecialidadeEnum> especialidadesAtuais = prestador.getEspecialidades().stream()
                .map(Especialidade::getEspecialidade)
                .collect(java.util.stream.Collectors.toSet());

        // Remover especialidades que não estão mais selecionadas
        prestador.getEspecialidades().removeIf(e -> !novasEspecialidades.contains(e.getEspecialidade()));

        // Adicionar apenas as especialidades novas (que não existem)
        for (Especialidade.EspecialidadeEnum espEnum : novasEspecialidades) {
            if (!especialidadesAtuais.contains(espEnum)) {
                Especialidade especialidade = new Especialidade();
                especialidade.setEspecialidade(espEnum);
                especialidade.setPrestador(prestador);
                prestador.getEspecialidades().add(especialidade);
            }
        }
    }

    private Prestador buscarPrestadorPorIdOuFalhar(Long id) {
        return prestadorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Prestador não encontrado"));
    }

    private Prestador criarPrestador(PrestadorCreateDTO prestadorCreateDTO) {
        Prestador prestador = prestadorMapper.map(prestadorCreateDTO);
        validarUnicidadeDeCampos(prestador);

        List<Especialidade> especialidades = extrairEspecialidades(prestadorCreateDTO.getEspecialidades(), prestador);
        prestador.setEspecialidades(especialidades);

        return prestador;
    }

    private List<Especialidade> extrairEspecialidades(Set<Especialidade.EspecialidadeEnum> especialidadesNovas, Prestador prestador) {
        List<Especialidade> especialidades = new ArrayList<>();

        for (Especialidade.EspecialidadeEnum especialidadeEnum: especialidadesNovas) {
            Especialidade especialidade = new Especialidade();
            especialidade.setEspecialidade(especialidadeEnum);
            especialidade.setPrestador(prestador);
            especialidades.add(especialidade);
        }

        return especialidades;
    }

    private void validarUnicidadeDeCampos(Prestador prestador) {
        if (prestadorRepository.existsByIdNotAndTelefoneOrEmailIgnoreCase(
                prestador.getId(),
                prestador.getTelefone(),
                prestador.getEmail()
        )) {
            throw new IllegalArgumentException("Já existe um prestador com o mesmo telefone e/ou email");
        }
    }
}
