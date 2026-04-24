package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.AuthResponseDTO;
import com.aijw.cuidarplus.dto.auth.PrestadorRegisterRequestDTO;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.PrestadorRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestadorAuthServiceTest {

    @Mock
    private PrestadorRepository prestadorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PrestadorAuthService prestadorAuthService;

    @Test
    void registrar_devePersistirSenhaCriptografadaEDefinirTipoUsuario() {
        PrestadorRegisterRequestDTO request = criarRequest();
        Prestador salvo = criarPrestador(1L, request.getNome(), request.getEmail(), "hash", TipoUsuario.PRESTADOR);
        Claims claims = claimsComExpiracao(123456789L);

        when(prestadorRepository.existsByTelefoneIgnoreCase(request.getTelefone())).thenReturn(false);
        when(prestadorRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getSenha())).thenReturn("hash");
        when(prestadorRepository.saveAndFlush(any(Prestador.class))).thenReturn(salvo);
        when(jwtService.generateToken(1L, request.getEmail(), TipoUsuario.PRESTADOR)).thenReturn("token-prestador");
        when(jwtService.parseClaims("token-prestador")).thenReturn(claims);

        AuthResponseDTO response = prestadorAuthService.registrar(request);

        assertEquals(1L, response.getId());
        assertEquals(request.getNome(), response.getNome());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(TipoUsuario.PRESTADOR, response.getTipoUsuario());
        assertEquals("token-prestador", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(123456789L, response.getExpiresIn());
        verify(passwordEncoder).encode(request.getSenha());
        verify(prestadorRepository).saveAndFlush(any(Prestador.class));
    }

    @Test
    void registrar_deveLancarExcecaoQuandoTelefoneJaExiste() {
        PrestadorRegisterRequestDTO request = criarRequest();
        when(prestadorRepository.existsByTelefoneIgnoreCase(request.getTelefone())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prestadorAuthService.registrar(request));

        assertEquals("Já existe um prestador com o mesmo telefone", exception.getMessage());
    }

    @Test
    void autenticar_deveRetornarTokenQuandoCredenciaisForemValidas() {
        Prestador prestador = criarPrestador(2L, "Maria", "maria@example.com", "hash", TipoUsuario.PRESTADOR);
        Claims claims = claimsComExpiracao(987654321L);

        when(prestadorRepository.findByEmailIgnoreCase(prestador.getEmail())).thenReturn(Optional.of(prestador));
        when(passwordEncoder.matches("senha", "hash")).thenReturn(true);
        when(jwtService.generateToken(2L, prestador.getEmail(), TipoUsuario.PRESTADOR)).thenReturn("token-prestador");
        when(jwtService.parseClaims("token-prestador")).thenReturn(claims);

        AuthResponseDTO response = prestadorAuthService.autenticar(prestador.getEmail(), "senha");

        assertEquals(2L, response.getId());
        assertEquals(TipoUsuario.PRESTADOR, response.getTipoUsuario());
        assertEquals("token-prestador", response.getToken());
        assertEquals(987654321L, response.getExpiresIn());
    }

    @Test
    void autenticar_deveLancarBadCredentialsQuandoSenhaNaoBater() {
        Prestador prestador = criarPrestador(2L, "Maria", "maria@example.com", "hash", TipoUsuario.PRESTADOR);
        when(prestadorRepository.findByEmailIgnoreCase(prestador.getEmail())).thenReturn(Optional.of(prestador));
        when(passwordEncoder.matches("senha", "hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> prestadorAuthService.autenticar(prestador.getEmail(), "senha"));
    }

    private PrestadorRegisterRequestDTO criarRequest() {
        PrestadorRegisterRequestDTO dto = new PrestadorRegisterRequestDTO();
        dto.setNome("Maria");
        dto.setTelefone("11999990000");
        dto.setEmail("maria@example.com");
        dto.setSenha("senha");
        dto.setEspecialidades(Set.of(Especialidade.EspecialidadeEnum.ENFERMEIRO));
        return dto;
    }

    private Prestador criarPrestador(Long id, String nome, String email, String senha, TipoUsuario tipoUsuario) {
        Prestador prestador = new Prestador();
        prestador.setId(id);
        prestador.setNome(nome);
        prestador.setTelefone("11999990000");
        prestador.setEmail(email);
        prestador.setSenha(senha);
        prestador.setTipoUsuario(tipoUsuario);
        return prestador;
    }

    private Claims claimsComExpiracao(long expiresAt) {
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(expiresAt));
        return claims;
    }
}




