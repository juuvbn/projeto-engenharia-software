package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.AuthResponseDTO;
import com.aijw.cuidarplus.dto.auth.ClienteRegisterRequestDTO;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.security.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteAuthServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ClienteAuthService clienteAuthService;

    @Test
    void registrar_devePersistirSenhaCriptografadaEDefinirTipoUsuario() {
        ClienteRegisterRequestDTO request = criarRequest();
        Cliente salvo = criarCliente(1L, request.getNome(), request.getEmail(), request.getEndereco(), "hash", TipoUsuario.CLIENTE);
        Claims claims = claimsComExpiracao(123456789L);

        when(clienteRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getSenha())).thenReturn("hash");
        when(clienteRepository.saveAndFlush(any(Cliente.class))).thenReturn(salvo);
        when(jwtService.generateToken(1L, request.getEmail(), TipoUsuario.CLIENTE)).thenReturn("token-cliente");
        when(jwtService.parseClaims("token-cliente")).thenReturn(claims);

        AuthResponseDTO response = clienteAuthService.registrar(request);

        assertEquals(1L, response.getId());
        assertEquals(request.getNome(), response.getNome());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(TipoUsuario.CLIENTE, response.getTipoUsuario());
        assertEquals("token-cliente", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(123456789L, response.getExpiresIn());
        verify(passwordEncoder).encode(request.getSenha());
        verify(clienteRepository).saveAndFlush(any(Cliente.class));
    }

    @Test
    void registrar_deveLancarExcecaoQuandoEmailJaExiste() {
        ClienteRegisterRequestDTO request = criarRequest();
        when(clienteRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> clienteAuthService.registrar(request));

        assertEquals("Já existe um cliente com o mesmo email", exception.getMessage());
    }

    @Test
    void autenticar_deveRetornarTokenQuandoCredenciaisForemValidas() {
        Cliente cliente = criarCliente(2L, "Maria", "maria@example.com", "Rua A", "hash", TipoUsuario.CLIENTE);
        Claims claims = claimsComExpiracao(987654321L);

        when(clienteRepository.findByEmailIgnoreCase(cliente.getEmail())).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("senha", "hash")).thenReturn(true);
        when(jwtService.generateToken(2L, cliente.getEmail(), TipoUsuario.CLIENTE)).thenReturn("token-cliente");
        when(jwtService.parseClaims("token-cliente")).thenReturn(claims);

        AuthResponseDTO response = clienteAuthService.autenticar(cliente.getEmail(), "senha");

        assertEquals(2L, response.getId());
        assertEquals(TipoUsuario.CLIENTE, response.getTipoUsuario());
        assertEquals("token-cliente", response.getToken());
        assertEquals(987654321L, response.getExpiresIn());
    }

    @Test
    void autenticar_deveLancarBadCredentialsQuandoEmailNaoExistir() {
        when(clienteRepository.findByEmailIgnoreCase("maria@example.com")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> clienteAuthService.autenticar("maria@example.com", "senha"));
    }

    private ClienteRegisterRequestDTO criarRequest() {
        ClienteRegisterRequestDTO dto = new ClienteRegisterRequestDTO();
        dto.setNome("Maria");
        dto.setTelefone("11999990000");
        dto.setEmail("maria@example.com");
        dto.setEndereco("Rua A, 123");
        dto.setSenha("senha");
        return dto;
    }

    private Cliente criarCliente(Long id, String nome, String email, String endereco, String senha, TipoUsuario tipoUsuario) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setEndereco(endereco);
        cliente.setSenha(senha);
        cliente.setTipoUsuario(tipoUsuario);
        return cliente;
    }

    private Claims claimsComExpiracao(long expiresAt) {
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(expiresAt));
        return claims;
    }
}




