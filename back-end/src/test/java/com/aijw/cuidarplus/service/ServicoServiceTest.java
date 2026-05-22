package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.servico.ProporDataDTO;
import com.aijw.cuidarplus.dto.servico.ServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoDTO;
import com.aijw.cuidarplus.dto.servico.ServicoPropostaDTO;
import com.aijw.cuidarplus.mapper.MaterialServicoMapper;
import com.aijw.cuidarplus.mapper.ServicoMapper;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.Servico;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import com.aijw.cuidarplus.repository.ServicoRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PrestadorRepository prestadorRepository;

    @Mock
    private ServicoMapper servicoMapper;

    @Mock
    private MaterialServicoMapper materialServicoMapper;

    @InjectMocks
    private ServicoService servicoService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Cliente criarCliente(Long id) {
        Cliente c = new Cliente();
        c.setId(id);
        c.setNome("Maria");
        c.setTelefone("11999990000");
        c.setEmail("maria@test.com");
        c.setEndereco("Rua A, 1");
        c.setSenha("hash");
        c.setTipoUsuario(TipoUsuario.CLIENTE);
        return c;
    }

    private Prestador criarPrestador(Long id) {
        Prestador p = new Prestador();
        p.setId(id);
        p.setNome("João");
        p.setTelefone("11888880000");
        p.setEmail("joao@test.com");
        p.setSenha("hash");
        p.setTipoUsuario(TipoUsuario.PRESTADOR);
        return p;
    }

    /**
     * Cria um Servico com entidades reais (não mockadas).
     * Servico.materiais já é inicializado como new ArrayList<>() inline na entidade,
     * portanto new Servico() sempre terá lista mutável — não é preciso setar.
     */
    private Servico criarServico(Long id, Cliente contratante, Prestador contratado,
                                  Servico.StatusServico status) {
        Servico s = new Servico();
        s.setId(id);
        s.setContratante(contratante);
        s.setContratado(contratado);
        s.setDescricao("Cuidado diário");
        s.setStatus(status);
        return s;
    }

    private AuthenticatedUserPrincipal criarPrincipalCliente(Long id) {
        return new AuthenticatedUserPrincipal(id, "maria@test.com", "hash", TipoUsuario.CLIENTE);
    }

    private AuthenticatedUserPrincipal criarPrincipalPrestador(Long id) {
        return new AuthenticatedUserPrincipal(id, "joao@test.com", "hash", TipoUsuario.PRESTADOR);
    }

    private ServicoCreateDTO criarCreateDTO(Long prestadorId) {
        ServicoCreateDTO dto = new ServicoCreateDTO();
        dto.setPrestadorId(prestadorId);
        dto.setDescricao("Cuidado diário");
        return dto;
    }

    private ServicoPropostaDTO criarPropostaDTO() {
        ServicoPropostaDTO dto = new ServicoPropostaDTO();
        dto.setDataHorario(Instant.now().plusSeconds(3600));
        dto.setMateriais(Collections.emptyList()); // sem materiais — não precisa mockar materialServicoMapper
        return dto;
    }

    private ProporDataDTO criarProporDataDTO() {
        ProporDataDTO dto = new ProporDataDTO();
        dto.setDataHorario(Instant.now().plusSeconds(7200));
        return dto;
    }

    // -------------------------------------------------------------------------
    // criarPropostaInicial
    // -------------------------------------------------------------------------

    @Test
    void criarPropostaInicial_deveCriarServicoComStatusPendente() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servicoVazio = new Servico(); // tem materiais já como ArrayList vazio
        ServicoDTO mockDTO = new ServicoDTO();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(prestadorRepository.findById(2L)).thenReturn(Optional.of(prestador));
        // Distinguir as duas sobrecargas do mapper pelo tipo do argumento
        when(servicoMapper.map(any(ServicoCreateDTO.class))).thenReturn(servicoVazio);
        when(servicoRepository.save(any(Servico.class))).thenReturn(servicoVazio);
        when(servicoMapper.map(any(Servico.class))).thenReturn(mockDTO);

        ServicoDTO resultado = servicoService.criarPropostaInicial(
                criarPrincipalCliente(1L), criarCreateDTO(2L));

        assertSame(mockDTO, resultado);
        assertEquals(Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE, servicoVazio.getStatus());
        assertEquals(cliente, servicoVazio.getContratante());
        assertEquals(prestador, servicoVazio.getContratado());
        verify(servicoRepository).save(servicoVazio);
    }

    @Test
    void criarPropostaInicial_deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.criarPropostaInicial(criarPrincipalCliente(1L), criarCreateDTO(2L)));

        assertEquals("Cliente não encontrado", ex.getMessage());
        verifyNoInteractions(prestadorRepository, servicoRepository);
    }

    @Test
    void criarPropostaInicial_deveLancarExcecaoQuandoPrestadorNaoEncontrado() {
        Cliente cliente = criarCliente(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(prestadorRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.criarPropostaInicial(criarPrincipalCliente(1L), criarCreateDTO(2L)));

        assertEquals("Prestador não encontrado", ex.getMessage());
        verifyNoInteractions(servicoRepository);
    }

    // -------------------------------------------------------------------------
    // aceitarProposta
    // -------------------------------------------------------------------------

    @Test
    void aceitarProposta_deveTransicionarStatusParaAceitacaoClientePendente() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE))
                .thenReturn(Optional.of(servico));
        when(servicoRepository.saveAndFlush(any())).thenReturn(servico);
        when(servicoMapper.map(any(Servico.class))).thenReturn(new ServicoDTO());

        // Principal é o prestador designado (id=2L)
        servicoService.aceitarProposta(criarPrincipalPrestador(2L), 10L, criarPropostaDTO());

        assertEquals(Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE, servico.getStatus());
        verify(servicoRepository).saveAndFlush(servico);
    }

    @Test
    void aceitarProposta_deveLancarExcecaoQuandoStatusInvalido() {
        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.aceitarProposta(criarPrincipalPrestador(2L), 10L, criarPropostaDTO()));

        assertEquals("Serviço não encontrado ou status inválido", ex.getMessage());
    }

    @Test
    void aceitarProposta_deveLancarExcecaoQuandoPrestadorNaoEODesignado() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L); // designado é id=2
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE))
                .thenReturn(Optional.of(servico));

        // Principal com id=99 (diferente do designado id=2)
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.aceitarProposta(criarPrincipalPrestador(99L), 10L, criarPropostaDTO()));

        assertEquals("Apenas o prestador designado pode realizar esta ação", ex.getMessage());
        verify(servicoRepository, never()).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // negarProposta
    // -------------------------------------------------------------------------

    @Test
    void negarProposta_deveTransicionarStatusParaNegado() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE))
                .thenReturn(Optional.of(servico));
        when(servicoRepository.saveAndFlush(any())).thenReturn(servico);
        when(servicoMapper.map(any(Servico.class))).thenReturn(new ServicoDTO());

        servicoService.negarProposta(criarPrincipalPrestador(2L), 10L);

        assertEquals(Servico.StatusServico.NEGADO, servico.getStatus());
        verify(servicoRepository).saveAndFlush(servico);
    }

    @Test
    void negarProposta_deveLancarExcecaoQuandoPrestadorNaoEODesignado() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE))
                .thenReturn(Optional.of(servico));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.negarProposta(criarPrincipalPrestador(99L), 10L));

        assertEquals("Apenas o prestador designado pode realizar esta ação", ex.getMessage());
        verify(servicoRepository, never()).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // confirmarServico
    // -------------------------------------------------------------------------

    @Test
    void confirmarServico_deveTransicionarStatusParaAceito() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE))
                .thenReturn(Optional.of(servico));
        when(servicoRepository.saveAndFlush(any())).thenReturn(servico);
        when(servicoMapper.map(any(Servico.class))).thenReturn(new ServicoDTO());

        // Principal é o cliente contratante (id=1L)
        servicoService.confirmarServico(criarPrincipalCliente(1L), 10L);

        assertEquals(Servico.StatusServico.ACEITO, servico.getStatus());
        verify(servicoRepository).saveAndFlush(servico);
    }

    @Test
    void confirmarServico_deveLancarExcecaoQuandoClienteNaoEOContratante() {
        Cliente cliente = criarCliente(1L); // contratante é id=1
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE))
                .thenReturn(Optional.of(servico));

        // Principal com id=99 (diferente do contratante id=1)
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.confirmarServico(criarPrincipalCliente(99L), 10L));

        assertEquals("Apenas o cliente contratante pode realizar esta ação", ex.getMessage());
        verify(servicoRepository, never()).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // recusarServico
    // -------------------------------------------------------------------------

    @Test
    void recusarServico_deveTransicionarStatusParaNegado() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE))
                .thenReturn(Optional.of(servico));
        when(servicoRepository.saveAndFlush(any())).thenReturn(servico);
        when(servicoMapper.map(any(Servico.class))).thenReturn(new ServicoDTO());

        servicoService.recusarServico(criarPrincipalCliente(1L), 10L);

        assertEquals(Servico.StatusServico.NEGADO, servico.getStatus());
        verify(servicoRepository).saveAndFlush(servico);
    }

    @Test
    void recusarServico_deveLancarExcecaoQuandoClienteNaoEOContratante() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE))
                .thenReturn(Optional.of(servico));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.recusarServico(criarPrincipalCliente(99L), 10L));

        assertEquals("Apenas o cliente contratante pode realizar esta ação", ex.getMessage());
        verify(servicoRepository, never()).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // proporData
    // -------------------------------------------------------------------------

    @Test
    void proporData_deveTransicionarStatusDeVoltaParaPrestadorPendente() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        ProporDataDTO request = criarProporDataDTO();

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE))
                .thenReturn(Optional.of(servico));
        when(servicoRepository.saveAndFlush(any())).thenReturn(servico);
        when(servicoMapper.map(any(Servico.class))).thenReturn(new ServicoDTO());

        servicoService.proporData(criarPrincipalCliente(1L), 10L, request);

        // Status volta para o prestador avaliar novamente
        assertEquals(Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE, servico.getStatus());
        // Data proposta deve ter sido atualizada
        assertEquals(request.getDataHorario(), servico.getDataHorario());
        verify(servicoRepository).saveAndFlush(servico);
    }

    @Test
    void proporData_deveLancarExcecaoQuandoClienteNaoEOContratante() {
        Cliente cliente = criarCliente(1L);
        Prestador prestador = criarPrestador(2L);
        Servico servico = criarServico(10L, cliente, prestador,
                Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        when(servicoRepository.findByIdAndStatusEquals(10L, Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE))
                .thenReturn(Optional.of(servico));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                servicoService.proporData(criarPrincipalCliente(99L), 10L, criarProporDataDTO()));

        assertEquals("Apenas o cliente contratante pode realizar esta ação", ex.getMessage());
        verify(servicoRepository, never()).saveAndFlush(any());
    }
}
