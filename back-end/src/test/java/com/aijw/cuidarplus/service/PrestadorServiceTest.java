package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.PasswordChangeRequestDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorDTO;
import com.aijw.cuidarplus.dto.prestador.PrestadorUpdateDTO;
import com.aijw.cuidarplus.mapper.PrestadorMapper;
import com.aijw.cuidarplus.mapper.ServicoMapper;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.EspecialidadesRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import com.aijw.cuidarplus.repository.ServicoRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestadorServiceTest {

    @Mock
    private PrestadorRepository prestadorRepository;

    @Mock
    private EspecialidadesRepository especialidadesRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private PrestadorMapper prestadorMapper;

    @Mock
    private ServicoMapper servicoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PrestadorService prestadorService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Cria um Prestador com lista de especialidades inicializada (ArrayList mutável).
     * CRÍTICO: Prestador não tem inicializador inline em especialidades, então é
     * obrigatório chamar setEspecialidades() para evitar NullPointerException
     * no método sincronizarEspecialidades.
     */
    private Prestador criarPrestador(Long id) {
        Prestador p = new Prestador();
        p.setId(id);
        p.setNome("João");
        p.setTelefone("11999990000");
        p.setEmail("joao@test.com");
        p.setSenha("hashSenha");
        p.setTipoUsuario(TipoUsuario.PRESTADOR);
        p.setEspecialidades(new ArrayList<>()); // lista mutável — obrigatório
        return p;
    }

    private Especialidade criarEspecialidade(Especialidade.EspecialidadeEnum tipo, Prestador prestador) {
        Especialidade e = new Especialidade();
        e.setEspecialidade(tipo);
        e.setPrestador(prestador);
        return e;
    }

    private AuthenticatedUserPrincipal criarPrincipal(Long id) {
        return new AuthenticatedUserPrincipal(id, "joao@test.com", "hashSenha", TipoUsuario.PRESTADOR);
    }

    private PasswordChangeRequestDTO criarPasswordRequest(String atual, String nova, String confirmacao) {
        PasswordChangeRequestDTO dto = new PasswordChangeRequestDTO();
        dto.setSenhaAtual(atual);
        dto.setNovaSenha(nova);
        dto.setConfirmacaoNovaSenha(confirmacao);
        return dto;
    }

    private PrestadorUpdateDTO criarUpdateDTO(Set<Especialidade.EspecialidadeEnum> especialidades) {
        PrestadorUpdateDTO dto = new PrestadorUpdateDTO();
        dto.setNome("João Atualizado");
        dto.setTelefone("11888880000");
        dto.setEmail("joaonovo@test.com");
        dto.setEspecialidades(especialidades);
        return dto;
    }

    // -------------------------------------------------------------------------
    // buscarPerfilAutenticado
    // -------------------------------------------------------------------------

    @Test
    void buscarPerfilAutenticado_deveRetornarDTODoPrestador() {
        Prestador prestador = criarPrestador(1L);
        PrestadorDTO dto = new PrestadorDTO();

        when(prestadorRepository.findById(1L)).thenReturn(Optional.of(prestador));
        when(prestadorMapper.map(prestador)).thenReturn(dto);

        PrestadorDTO resultado = prestadorService.buscarPerfilAutenticado(criarPrincipal(1L));

        assertSame(dto, resultado);
    }

    @Test
    void buscarPerfilAutenticado_deveLancarExcecaoSePrestadorNaoEncontrado() {
        when(prestadorRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                prestadorService.buscarPerfilAutenticado(criarPrincipal(1L)));

        assertEquals("Prestador não encontrado", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // atualizarPrestador
    // -------------------------------------------------------------------------

    @Test
    void atualizarPrestador_deveAtualizarDadosComSucesso() {
        Prestador prestador = criarPrestador(1L);
        PrestadorUpdateDTO request = criarUpdateDTO(Set.of(Especialidade.EspecialidadeEnum.ENFERMEIRO));
        PrestadorDTO dto = new PrestadorDTO();

        when(prestadorRepository.findById(1L)).thenReturn(Optional.of(prestador));
        when(prestadorRepository.saveAndFlush(any())).thenReturn(prestador);
        when(prestadorMapper.map(any(Prestador.class))).thenReturn(dto);

        PrestadorDTO resultado = prestadorService.atualizarPrestador(criarPrincipal(1L), request);

        assertSame(dto, resultado);
        verify(prestadorMapper).update(request, prestador);
        verify(prestadorRepository).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // sincronizarEspecialidades (testado via atualizarPrestador)
    // -------------------------------------------------------------------------

    @Test
    void sincronizarEspecialidades_deveAdicionarNovasEspecialidades() {
        Prestador prestador = criarPrestador(1L);
        // Prestador inicia com CUIDADOR_DE_IDOSOS
        prestador.getEspecialidades().add(
                criarEspecialidade(Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS, prestador));

        // Atualização solicita CUIDADOR_DE_IDOSOS + ENFERMEIRO (novo)
        PrestadorUpdateDTO request = criarUpdateDTO(
                Set.of(Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS,
                        Especialidade.EspecialidadeEnum.ENFERMEIRO));

        when(prestadorRepository.findById(1L)).thenReturn(Optional.of(prestador));
        when(prestadorRepository.saveAndFlush(any())).thenReturn(prestador);
        when(prestadorMapper.map(any(Prestador.class))).thenReturn(new PrestadorDTO());

        prestadorService.atualizarPrestador(criarPrincipal(1L), request);

        // Captura o prestador salvo para verificar as especialidades
        ArgumentCaptor<Prestador> captor = ArgumentCaptor.forClass(Prestador.class);
        verify(prestadorRepository).saveAndFlush(captor.capture());

        List<Especialidade> especialidadesSalvas = captor.getValue().getEspecialidades();
        assertEquals(2, especialidadesSalvas.size(), "Deve conter 2 especialidades após sincronização");

        boolean temEnfermeiro = especialidadesSalvas.stream()
                .anyMatch(e -> e.getEspecialidade() == Especialidade.EspecialidadeEnum.ENFERMEIRO);
        assertTrue(temEnfermeiro, "ENFERMEIRO deve ter sido adicionado");

        boolean temCuidador = especialidadesSalvas.stream()
                .anyMatch(e -> e.getEspecialidade() == Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS);
        assertTrue(temCuidador, "CUIDADOR_DE_IDOSOS deve permanecer");
    }

    @Test
    void sincronizarEspecialidades_deveRemoverEspecialidadesDeselacionadas() {
        Prestador prestador = criarPrestador(1L);
        // Prestador inicia com ENFERMEIRO
        prestador.getEspecialidades().add(
                criarEspecialidade(Especialidade.EspecialidadeEnum.ENFERMEIRO, prestador));

        // Atualização solicita apenas CUIDADOR_DE_IDOSOS (remove ENFERMEIRO)
        PrestadorUpdateDTO request = criarUpdateDTO(
                Set.of(Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS));

        when(prestadorRepository.findById(1L)).thenReturn(Optional.of(prestador));
        when(prestadorRepository.saveAndFlush(any())).thenReturn(prestador);
        when(prestadorMapper.map(any(Prestador.class))).thenReturn(new PrestadorDTO());

        prestadorService.atualizarPrestador(criarPrincipal(1L), request);

        ArgumentCaptor<Prestador> captor = ArgumentCaptor.forClass(Prestador.class);
        verify(prestadorRepository).saveAndFlush(captor.capture());

        List<Especialidade> especialidadesSalvas = captor.getValue().getEspecialidades();
        assertEquals(1, especialidadesSalvas.size(), "Deve restar apenas 1 especialidade");

        boolean temEnfermeiro = especialidadesSalvas.stream()
                .anyMatch(e -> e.getEspecialidade() == Especialidade.EspecialidadeEnum.ENFERMEIRO);
        assertFalse(temEnfermeiro, "ENFERMEIRO deve ter sido removido");

        boolean temCuidador = especialidadesSalvas.stream()
                .anyMatch(e -> e.getEspecialidade() == Especialidade.EspecialidadeEnum.CUIDADOR_DE_IDOSOS);
        assertTrue(temCuidador, "CUIDADOR_DE_IDOSOS deve ter sido adicionado");
    }

    // -------------------------------------------------------------------------
    // alterarSenha
    // -------------------------------------------------------------------------

    @Test
    void alterarSenha_deveAlterarSenhaComSucesso() {
        Prestador prestador = criarPrestador(1L);
        PasswordChangeRequestDTO request = criarPasswordRequest("senhaVelha", "senhaNova", "senhaNova");

        when(prestadorRepository.findById(1L)).thenReturn(Optional.of(prestador));
        when(passwordEncoder.matches("senhaVelha", "hashSenha")).thenReturn(true);
        when(passwordEncoder.encode("senhaNova")).thenReturn("hashNova");

        prestadorService.alterarSenha(criarPrincipal(1L), request);

        ArgumentCaptor<Prestador> captor = ArgumentCaptor.forClass(Prestador.class);
        verify(prestadorRepository).saveAndFlush(captor.capture());
        assertEquals("hashNova", captor.getValue().getSenha());
    }

    @Test
    void alterarSenha_deveLancarExcecaoQuandoSenhaNovaNaoBateComConfirmacao() {
        PasswordChangeRequestDTO request = criarPasswordRequest("senhaVelha", "abc123", "xyz789");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                prestadorService.alterarSenha(criarPrincipal(1L), request));

        assertEquals("A nova senha e a confirmação não coincidem", ex.getMessage());
        verifyNoInteractions(prestadorRepository);
    }

    @Test
    void alterarSenha_deveLancarExcecaoQuandoNovaSenhaIgualAtual() {
        PasswordChangeRequestDTO request = criarPasswordRequest("mesmaSenha", "mesmaSenha", "mesmaSenha");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                prestadorService.alterarSenha(criarPrincipal(1L), request));

        assertEquals("A nova senha deve ser diferente da senha atual", ex.getMessage());
        verifyNoInteractions(prestadorRepository);
    }

    @Test
    void alterarSenha_deveLancarExcecaoQuandoSenhaAtualIncorreta() {
        Prestador prestador = criarPrestador(1L);
        PasswordChangeRequestDTO request = criarPasswordRequest("senhaErrada", "senhaNova", "senhaNova");

        when(prestadorRepository.findById(1L)).thenReturn(Optional.of(prestador));
        when(passwordEncoder.matches("senhaErrada", "hashSenha")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                prestadorService.alterarSenha(criarPrincipal(1L), request));

        assertEquals("A senha atual está incorreta", ex.getMessage());
        verify(prestadorRepository, never()).saveAndFlush(any());
    }
}
