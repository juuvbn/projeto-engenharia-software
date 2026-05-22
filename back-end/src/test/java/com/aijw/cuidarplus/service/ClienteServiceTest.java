package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.PasswordChangeRequestDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteUpdateDTO;
import com.aijw.cuidarplus.mapper.ClienteMapper;
import com.aijw.cuidarplus.mapper.ServicoMapper;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.repository.ServicoRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ServicoMapper servicoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Cliente criarCliente(Long id, String email) {
        Cliente c = new Cliente();
        c.setId(id);
        c.setNome("Maria");
        c.setTelefone("11999990000");
        c.setEmail(email);
        c.setEndereco("Rua A, 123");
        c.setSenha("hashSenha");
        c.setTipoUsuario(TipoUsuario.CLIENTE);
        return c;
    }

    private AuthenticatedUserPrincipal criarPrincipal(Long id) {
        return new AuthenticatedUserPrincipal(id, "maria@test.com", "hashSenha", TipoUsuario.CLIENTE);
    }

    private PasswordChangeRequestDTO criarPasswordRequest(String atual, String nova, String confirmacao) {
        PasswordChangeRequestDTO dto = new PasswordChangeRequestDTO();
        dto.setSenhaAtual(atual);
        dto.setNovaSenha(nova);
        dto.setConfirmacaoNovaSenha(confirmacao);
        return dto;
    }

    private ClienteUpdateDTO criarUpdateDTO(String email) {
        ClienteUpdateDTO dto = new ClienteUpdateDTO();
        dto.setNome("Maria Atualizada");
        dto.setTelefone("11888880000");
        dto.setEmail(email);
        dto.setEndereco("Rua B, 456");
        return dto;
    }

    // -------------------------------------------------------------------------
    // registrarCliente
    // -------------------------------------------------------------------------

    @Test
    void registrarCliente_deveSalvarQuandoEmailUnico() {
        Cliente cliente = criarCliente(null, "maria@test.com");
        when(clienteRepository.existsByEmailIgnoreCase("maria@test.com")).thenReturn(false);
        when(clienteRepository.saveAndFlush(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.registrarCliente(cliente);

        assertNotNull(resultado);
        verify(clienteRepository).existsByEmailIgnoreCase("maria@test.com");
        verify(clienteRepository).saveAndFlush(cliente);
    }

    @Test
    void registrarCliente_deveLancarExcecaoQuandoEmailDuplicado() {
        Cliente cliente = criarCliente(null, "maria@test.com");
        when(clienteRepository.existsByEmailIgnoreCase("maria@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.registrarCliente(cliente));

        assertEquals("Já existe um cliente com o mesmo email", ex.getMessage());
        verify(clienteRepository, never()).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // atualizarCliente
    // -------------------------------------------------------------------------

    @Test
    void atualizarCliente_deveAtualizarDadosComSucesso() {
        Cliente cliente = criarCliente(1L, "maria@test.com");
        ClienteUpdateDTO request = criarUpdateDTO("maria@test.com");
        ClienteDTO dto = new ClienteDTO();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdNotAndEmailIgnoreCase(1L, "maria@test.com")).thenReturn(false);
        when(clienteRepository.saveAndFlush(any())).thenReturn(cliente);
        when(clienteMapper.map(any(Cliente.class))).thenReturn(dto);

        ClienteDTO resultado = clienteService.atualizarCliente(criarPrincipal(1L), request);

        assertSame(dto, resultado);
        verify(clienteMapper).update(request, cliente);
        verify(clienteRepository).saveAndFlush(any());
    }

    @Test
    void atualizarCliente_deveLancarExcecaoSeEmailJaExistirParaOutroUsuario() {
        Cliente cliente = criarCliente(1L, "maria@test.com");
        ClienteUpdateDTO request = criarUpdateDTO("maria@test.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdNotAndEmailIgnoreCase(1L, "maria@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.atualizarCliente(criarPrincipal(1L), request));

        assertEquals("Já existe um cliente com o mesmo email", ex.getMessage());
        verify(clienteRepository, never()).saveAndFlush(any());
    }

    // -------------------------------------------------------------------------
    // buscarPerfilAutenticado
    // -------------------------------------------------------------------------

    @Test
    void buscarPerfilAutenticado_deveRetornarDTODoCliente() {
        Cliente cliente = criarCliente(1L, "maria@test.com");
        ClienteDTO dto = new ClienteDTO();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.map(cliente)).thenReturn(dto);

        ClienteDTO resultado = clienteService.buscarPerfilAutenticado(criarPrincipal(1L));

        assertSame(dto, resultado);
    }

    @Test
    void buscarPerfilAutenticado_deveLancarExcecaoSeClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.buscarPerfilAutenticado(criarPrincipal(1L)));

        // Atenção: typo intencional no código-fonte — sem acento em "nao"
        assertEquals("Cliente nao encontrado", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // atualizarSenha
    // -------------------------------------------------------------------------

    @Test
    void atualizarSenha_deveAlterarSenhaComSucesso() {
        Cliente cliente = criarCliente(1L, "maria@test.com");
        PasswordChangeRequestDTO request = criarPasswordRequest("senhaVelha", "senhaNova", "senhaNova");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("senhaVelha", "hashSenha")).thenReturn(true);
        when(passwordEncoder.encode("senhaNova")).thenReturn("hashNova");

        clienteService.atualizarSenha(criarPrincipal(1L), request);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).saveAndFlush(captor.capture());
        assertEquals("hashNova", captor.getValue().getSenha());
    }

    @Test
    void atualizarSenha_deveLancarExcecaoQuandoSenhaNovaNaoBateComConfirmacao() {
        // nova senha e confirmação diferentes — falha antes de consultar o banco
        PasswordChangeRequestDTO request = criarPasswordRequest("senhaVelha", "abc123", "xyz789");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.atualizarSenha(criarPrincipal(1L), request));

        assertEquals("A nova senha e a confirmação não coincidem", ex.getMessage());
        verifyNoInteractions(clienteRepository);
    }

    @Test
    void atualizarSenha_deveLancarExcecaoQuandoNovaSenhaIgualAtual() {
        // nova senha igual à atual — falha antes de consultar o banco
        PasswordChangeRequestDTO request = criarPasswordRequest("mesmaSenha", "mesmaSenha", "mesmaSenha");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.atualizarSenha(criarPrincipal(1L), request));

        assertEquals("A nova senha deve ser diferente da senha atual", ex.getMessage());
        verifyNoInteractions(clienteRepository);
    }

    @Test
    void atualizarSenha_deveLancarExcecaoQuandoSenhaAtualIncorreta() {
        Cliente cliente = criarCliente(1L, "maria@test.com");
        PasswordChangeRequestDTO request = criarPasswordRequest("senhaErrada", "senhaNova", "senhaNova");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("senhaErrada", "hashSenha")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                clienteService.atualizarSenha(criarPrincipal(1L), request));

        assertEquals("A senha atual está incorreta", ex.getMessage());
        verify(clienteRepository, never()).saveAndFlush(any());
    }
}
