package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.PasswordResetToken;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.repository.PasswordResetTokenRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PrestadorRepository prestadorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetService passwordResetService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Cliente criarCliente(String email) {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setNome("Maria");
        c.setTelefone("11999990000");
        c.setEmail(email);
        c.setEndereco("Rua A, 1");
        c.setSenha("hashAntiga");
        c.setTipoUsuario(TipoUsuario.CLIENTE);
        return c;
    }

    private Prestador criarPrestador(String email) {
        Prestador p = new Prestador();
        p.setId(2L);
        p.setNome("João");
        p.setTelefone("11888880000");
        p.setEmail(email);
        p.setSenha("hashAntiga");
        p.setTipoUsuario(TipoUsuario.PRESTADOR);
        return p;
    }

    private PasswordResetToken criarTokenValido(String tokenStr, String email, TipoUsuario tipo) {
        PasswordResetToken t = new PasswordResetToken();
        t.setToken(tokenStr);
        t.setEmail(email);
        t.setTipoUsuario(tipo);
        t.setExpiresAt(LocalDateTime.now().plusHours(1));
        t.setUsed(false);
        return t;
    }

    // -------------------------------------------------------------------------
    // solicitarRecuperacao
    // -------------------------------------------------------------------------

    @Test
    void solicitarRecuperacao_deveCriarTokenParaClienteExistente() {
        String email = "maria@test.com";
        when(clienteRepository.existsByEmailIgnoreCase(email)).thenReturn(true);

        passwordResetService.solicitarRecuperacao(email, TipoUsuario.CLIENTE);

        // Deve deletar tokens anteriores antes de criar um novo
        verify(tokenRepository).deleteByEmailIgnoreCaseAndTipoUsuario(eq(email), eq(TipoUsuario.CLIENTE));

        // Deve salvar um novo token com os dados corretos
        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).saveAndFlush(captor.capture());

        PasswordResetToken tokenSalvo = captor.getValue();
        assertNotNull(tokenSalvo.getToken(), "O token UUID não deve ser nulo");
        assertEquals(email.toLowerCase(), tokenSalvo.getEmail(), "O email deve ser armazenado em minúsculas");
        assertEquals(TipoUsuario.CLIENTE, tokenSalvo.getTipoUsuario());
        assertTrue(tokenSalvo.getExpiresAt().isAfter(LocalDateTime.now()),
                "O token deve expirar no futuro");
    }

    @Test
    void solicitarRecuperacao_deveCriarTokenParaPrestadorExistente() {
        String email = "joao@test.com";
        when(prestadorRepository.existsByEmailIgnoreCase(email)).thenReturn(true);

        passwordResetService.solicitarRecuperacao(email, TipoUsuario.PRESTADOR);

        verify(tokenRepository).deleteByEmailIgnoreCaseAndTipoUsuario(eq(email), eq(TipoUsuario.PRESTADOR));

        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).saveAndFlush(captor.capture());

        PasswordResetToken tokenSalvo = captor.getValue();
        assertNotNull(tokenSalvo.getToken());
        assertEquals(email.toLowerCase(), tokenSalvo.getEmail());
        assertEquals(TipoUsuario.PRESTADOR, tokenSalvo.getTipoUsuario());
        assertTrue(tokenSalvo.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void solicitarRecuperacao_deveRetornarSilenciosamenteParaEmailNaoExistente_Cliente() {
        when(clienteRepository.existsByEmailIgnoreCase("naoexiste@test.com")).thenReturn(false);

        // Não deve lançar exceção (falha silenciosa para prevenir enumeração de emails)
        assertDoesNotThrow(() ->
                passwordResetService.solicitarRecuperacao("naoexiste@test.com", TipoUsuario.CLIENTE));

        // Não deve criar nem deletar tokens
        verify(tokenRepository, never()).saveAndFlush(any());
        verify(tokenRepository, never()).deleteByEmailIgnoreCaseAndTipoUsuario(any(), any());
    }

    @Test
    void solicitarRecuperacao_deveRetornarSilenciosamenteParaEmailNaoExistente_Prestador() {
        when(prestadorRepository.existsByEmailIgnoreCase("naoexiste@test.com")).thenReturn(false);

        assertDoesNotThrow(() ->
                passwordResetService.solicitarRecuperacao("naoexiste@test.com", TipoUsuario.PRESTADOR));

        verify(tokenRepository, never()).saveAndFlush(any());
        verify(tokenRepository, never()).deleteByEmailIgnoreCaseAndTipoUsuario(any(), any());
    }

    // -------------------------------------------------------------------------
    // redefinirSenha
    // -------------------------------------------------------------------------

    @Test
    void redefinirSenha_deveAtualizarSenhaDeClienteComTokenValido() {
        String email = "maria@test.com";
        PasswordResetToken token = criarTokenValido("uuid-token", email, TipoUsuario.CLIENTE);
        Cliente cliente = criarCliente(email);

        when(tokenRepository.findByToken("uuid-token")).thenReturn(Optional.of(token));
        when(clienteRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.encode("senhaNova")).thenReturn("hashNova");

        passwordResetService.redefinirSenha("uuid-token", "senhaNova", "senhaNova");

        // Deve atualizar a senha do cliente
        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(clienteCaptor.capture());
        assertEquals("hashNova", clienteCaptor.getValue().getSenha());

        // Deve marcar o token como utilizado
        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        assertTrue(tokenCaptor.getValue().isUsed());
    }

    @Test
    void redefinirSenha_deveAtualizarSenhaDeProviderComTokenValido() {
        String email = "joao@test.com";
        PasswordResetToken token = criarTokenValido("uuid-token-prestador", email, TipoUsuario.PRESTADOR);
        Prestador prestador = criarPrestador(email);

        when(tokenRepository.findByToken("uuid-token-prestador")).thenReturn(Optional.of(token));
        when(prestadorRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(prestador));
        when(passwordEncoder.encode("senhaNova")).thenReturn("hashNova");

        passwordResetService.redefinirSenha("uuid-token-prestador", "senhaNova", "senhaNova");

        ArgumentCaptor<Prestador> prestadorCaptor = ArgumentCaptor.forClass(Prestador.class);
        verify(prestadorRepository).save(prestadorCaptor.capture());
        assertEquals("hashNova", prestadorCaptor.getValue().getSenha());

        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        assertTrue(tokenCaptor.getValue().isUsed());
    }

    @Test
    void redefinirSenha_deveLancarExcecaoQuandoSenhasNaoConferem() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.redefinirSenha("qualquer-token", "senha1", "senha2"));

        assertEquals("As senhas não conferem", ex.getMessage());
        // Nenhum acesso ao banco deve ocorrer
        verifyNoInteractions(tokenRepository);
    }

    @Test
    void redefinirSenha_deveLancarExcecaoQuandoTokenInvalido() {
        when(tokenRepository.findByToken("token-invalido")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.redefinirSenha("token-invalido", "senhaNova", "senhaNova"));

        assertEquals("Token inválido ou expirado", ex.getMessage());
    }

    @Test
    void redefinirSenha_deveLancarExcecaoQuandoTokenJaUtilizado() {
        PasswordResetToken token = criarTokenValido("uuid-usado", "maria@test.com", TipoUsuario.CLIENTE);
        token.setUsed(true);

        when(tokenRepository.findByToken("uuid-usado")).thenReturn(Optional.of(token));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.redefinirSenha("uuid-usado", "senhaNova", "senhaNova"));

        assertEquals("Token já utilizado", ex.getMessage());
    }

    @Test
    void redefinirSenha_deveLancarExcecaoQuandoTokenExpirado() {
        PasswordResetToken token = criarTokenValido("uuid-expirado", "maria@test.com", TipoUsuario.CLIENTE);
        // Força expiração no passado
        token.setExpiresAt(LocalDateTime.now().minusHours(2));

        when(tokenRepository.findByToken("uuid-expirado")).thenReturn(Optional.of(token));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.redefinirSenha("uuid-expirado", "senhaNova", "senhaNova"));

        assertEquals("Token expirado", ex.getMessage());
    }
}
