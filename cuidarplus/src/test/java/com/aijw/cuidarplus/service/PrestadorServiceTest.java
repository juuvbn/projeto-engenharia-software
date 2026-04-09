package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.prestador.PrestadorCreateDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.mapper.PrestadorMapper;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.repository.EspecialidadesRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestadorServiceTest {

    @Mock
    private PrestadorRepository prestadorRepository;

    @Mock
    private EspecialidadesRepository especialidadesRepository;

    @Mock
    private PrestadorMapper prestadorMapper;

    @InjectMocks
    private PrestadorService prestadorService;

    @Test
    void buscarPrestadores_deveMapearPaginaRetornadaPeloRepositorio() {
        Pageable pageable = PageRequest.of(0, 10);
        Set<Especialidade.EspecialidadeEnum> especialidades = Set.of(Especialidade.EspecialidadeEnum.ENFERMEIRO);

        Prestador prestador = criarPrestador(1L, "Maria", "11999990000", "maria@example.com");
        PrestadorDTO dto = criarPrestadorDTO(1L, "Maria", "11999990000", "maria@example.com");

        when(prestadorRepository.buscarPrestadoresPorFiltro(especialidades, pageable))
                .thenReturn(new PageImpl<>(List.of(prestador), pageable, 1));
        when(prestadorMapper.map(prestador)).thenReturn(dto);

        Page<PrestadorDTO> resultado = prestadorService.buscarPrestadores(especialidades, pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        assertEquals(dto, resultado.getContent().getFirst());
        verify(prestadorRepository).buscarPrestadoresPorFiltro(especialidades, pageable);
        verify(prestadorMapper).map(prestador);
    }

    @Test
    void buscarPrestadores_deveRetornarPaginaVaziaQuandoNaoHouverResultados() {
        Pageable pageable = PageRequest.of(0, 10);
        Set<Especialidade.EspecialidadeEnum> especialidades = Set.of(Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS);

        when(prestadorRepository.buscarPrestadoresPorFiltro(especialidades, pageable))
                .thenReturn(Page.empty(pageable));

        Page<PrestadorDTO> resultado = prestadorService.buscarPrestadores(especialidades, pageable);

        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(prestadorRepository).buscarPrestadoresPorFiltro(especialidades, pageable);
        verifyNoInteractions(prestadorMapper);
    }

    @Test
    void registrarPrestador_deveSalvarPrestadorComEspecialidadesEBackReference() {
        PrestadorCreateDTO createDTO = criarCreateDTO(Set.of(
                Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS,
                Especialidade.EspecialidadeEnum.ENFERMEIRO
        ));
        Prestador prestadorMapeado = criarPrestador(null, createDTO.getNome(), createDTO.getTelefone(), createDTO.getEmail());
        PrestadorDTO prestadorDTO = criarPrestadorDTO(10L, createDTO.getNome(), createDTO.getTelefone(), createDTO.getEmail());

        when(prestadorMapper.map(createDTO)).thenReturn(prestadorMapeado);
        when(prestadorRepository.saveAndFlush(any(Prestador.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(prestadorMapper.map(any(Prestador.class))).thenReturn(prestadorDTO);

        PrestadorDTO resultado = prestadorService.registrarPrestador(createDTO);

        ArgumentCaptor<Prestador> captor = ArgumentCaptor.forClass(Prestador.class);
        verify(prestadorRepository).saveAndFlush(captor.capture());

        Prestador salvo = captor.getValue();
        assertEquals(createDTO.getNome(), salvo.getNome());
        assertEquals(createDTO.getTelefone(), salvo.getTelefone());
        assertEquals(createDTO.getEmail(), salvo.getEmail());
        assertEquals(2, salvo.getEspecialidades().size());
        assertTrue(salvo.getEspecialidades().stream().allMatch(especialidade -> especialidade.getPrestador() == salvo));
        assertTrue(salvo.getEspecialidades().stream()
                .map(Especialidade::getEspecialidade)
                .collect(Collectors.toSet())
                .containsAll(createDTO.getEspecialidades()));
        assertEquals(prestadorDTO, resultado);
    }

    @Test
    void registrarPrestador_deveSalvarPrestadorComUmaEspecialidade() {
        PrestadorCreateDTO createDTO = criarCreateDTO(Set.of(Especialidade.EspecialidadeEnum.BABA));
        Prestador prestadorMapeado = criarPrestador(null, createDTO.getNome(), createDTO.getTelefone(), createDTO.getEmail());
        PrestadorDTO prestadorDTO = criarPrestadorDTO(11L, createDTO.getNome(), createDTO.getTelefone(), createDTO.getEmail());

        when(prestadorMapper.map(createDTO)).thenReturn(prestadorMapeado);
        when(prestadorRepository.saveAndFlush(any(Prestador.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(prestadorMapper.map(any(Prestador.class))).thenReturn(prestadorDTO);

        PrestadorDTO resultado = prestadorService.registrarPrestador(createDTO);

        ArgumentCaptor<Prestador> captor = ArgumentCaptor.forClass(Prestador.class);
        verify(prestadorRepository).saveAndFlush(captor.capture());

        Prestador salvo = captor.getValue();
        assertEquals(1, salvo.getEspecialidades().size());
        assertEquals(Especialidade.EspecialidadeEnum.BABA, salvo.getEspecialidades().getFirst().getEspecialidade());
        assertTrue(salvo.getEspecialidades().getFirst().getPrestador() == salvo);
        assertEquals(prestadorDTO, resultado);
    }

    @Test
    void registrarPrestador_deveLancarExcecaoQuandoJaExistirTelefoneOuEmail() {
        PrestadorCreateDTO createDTO = criarCreateDTO(Set.of(Especialidade.EspecialidadeEnum.CUIDADOR_DE_PETS));
        Prestador prestadorMapeado = criarPrestador(1L, createDTO.getNome(), createDTO.getTelefone(), createDTO.getEmail());

        when(prestadorMapper.map(createDTO)).thenReturn(prestadorMapeado);
        when(prestadorRepository.existsByIdNotAndTelefoneOrEmailIgnoreCase(
                eq(1L),
                eq(createDTO.getTelefone()),
                eq(createDTO.getEmail())
        )).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> prestadorService.registrarPrestador(createDTO)
        );

        assertEquals("Já existe um prestador com o mesmo telefone e/ou email", exception.getMessage());
        verify(prestadorRepository, never()).saveAndFlush(any());
    }

    private PrestadorCreateDTO criarCreateDTO(Set<Especialidade.EspecialidadeEnum> especialidades) {
        PrestadorCreateDTO dto = new PrestadorCreateDTO();
        dto.setNome("Maria da Silva");
        dto.setTelefone("11999990000");
        dto.setEmail("maria@example.com");
        dto.setEspecialidades(especialidades);
        return dto;
    }

    private Prestador criarPrestador(Long id, String nome, String telefone, String email) {
        Prestador prestador = new Prestador();
        prestador.setId(id);
        prestador.setNome(nome);
        prestador.setTelefone(telefone);
        prestador.setEmail(email);
        return prestador;
    }

    private PrestadorDTO criarPrestadorDTO(Long id, String nome, String telefone, String email) {
        PrestadorDTO dto = new PrestadorDTO();
        dto.setId(id);
        dto.setNome(nome);
        dto.setTelefone(telefone);
        dto.setEmail(email);
        return dto;
    }
}