package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.AuthResponseDTO;
import com.aijw.cuidarplus.dto.auth.PrestadorRegisterRequestDTO;
import com.aijw.cuidarplus.model.Especialidade;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.TipoUsuario;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import com.aijw.cuidarplus.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestadorAuthService {
    private final PrestadorRepository prestadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDTO registrar(PrestadorRegisterRequestDTO request) {
        validarUnicidade(request.getTelefone(), request.getEmail());

        Prestador prestador = new Prestador();
        prestador.setNome(request.getNome());
        prestador.setTelefone(request.getTelefone());
        prestador.setEmail(request.getEmail());
        prestador.setSenha(passwordEncoder.encode(request.getSenha()));
        prestador.setTipoUsuario(TipoUsuario.PRESTADOR);
        prestador.setEspecialidades(criarEspecialidades(request, prestador));

        Prestador salvo = prestadorRepository.saveAndFlush(prestador);
        return gerarRespostaAutenticada(salvo);
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO autenticar(String email, String senha) {
        Prestador prestador = prestadorRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(senha, prestador.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return gerarRespostaAutenticada(prestador);
    }

    private void validarUnicidade(String telefone, String email) {
        if (prestadorRepository.existsByTelefoneIgnoreCase(telefone)) {
            throw new IllegalArgumentException("Já existe um prestador com o mesmo telefone");
        }

        if (prestadorRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Já existe um prestador com o mesmo email");
        }
    }

    private List<Especialidade> criarEspecialidades(PrestadorRegisterRequestDTO request, Prestador prestador) {
        List<Especialidade> especialidades = new ArrayList<>();
        for (Especialidade.EspecialidadeEnum especialidadeEnum : request.getEspecialidades()) {
            Especialidade especialidade = new Especialidade();
            especialidade.setPrestador(prestador);
            especialidade.setEspecialidade(especialidadeEnum);
            especialidades.add(especialidade);
        }
        return especialidades;
    }

    private AuthResponseDTO gerarRespostaAutenticada(Prestador prestador) {
        String token = jwtService.generateToken(prestador.getId(), prestador.getEmail(), prestador.getTipoUsuario());
        return new AuthResponseDTO(
                prestador.getId(),
                prestador.getNome(),
                prestador.getEmail(),
                prestador.getTipoUsuario(),
                token,
                "Bearer",
                jwtService.parseClaims(token).getExpiration().getTime()
        );
    }
}

