package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.AuthResponseDTO;
import com.aijw.cuidarplus.dto.auth.ClienteRegisterRequestDTO;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteAuthService {
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDTO registrar(ClienteRegisterRequestDTO request) {
        if (clienteRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente com o mesmo email");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setTelefone(request.getTelefone());
        cliente.setEmail(request.getEmail());
        cliente.setEndereco(request.getEndereco());
        cliente.setSenha(passwordEncoder.encode(request.getSenha()));
        cliente.setTipoUsuario(TipoUsuario.CLIENTE);

        Cliente salvo = clienteRepository.saveAndFlush(cliente);
        return gerarRespostaAutenticada(salvo);
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO autenticar(String email, String senha) {
        Cliente cliente = clienteRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return gerarRespostaAutenticada(cliente);
    }

    private AuthResponseDTO gerarRespostaAutenticada(Cliente cliente) {
        String token = jwtService.generateToken(cliente.getId(), cliente.getEmail(), cliente.getTipoUsuario());
        return new AuthResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTipoUsuario(),
                token,
                "Bearer",
                jwtService.parseClaims(token).getExpiration().getTime()
        );
    }
}

