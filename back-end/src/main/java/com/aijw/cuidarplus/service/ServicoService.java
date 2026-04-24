package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.servico.ServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoDTO;
import com.aijw.cuidarplus.mapper.ServicoMapper;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.Servico;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import com.aijw.cuidarplus.repository.ServicoRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;

    private final ServicoMapper servicoMapper;

    public ServicoDTO criarPropostaInicial(AuthenticatedUserPrincipal principal, ServicoCreateDTO request) {
        var servico = criarServico(principal, request);

        var servicoSalvo = servicoRepository.save(servico);

        return servicoMapper.map(servicoSalvo);
    }

    private Servico criarServico(AuthenticatedUserPrincipal principal, ServicoCreateDTO request) {
        Cliente cliente = buscarClientePorIdOuFalhar(principal.getId());
        Prestador prestador = buscarPrestadorPorIdOuFalhar(request.getPrestadorId());
        var servico = servicoMapper.map(request);

        servico.setContratante(cliente);
        servico.setContratado(prestador);

        return servico;
    }

    private Cliente buscarClientePorIdOuFalhar(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    private Prestador buscarPrestadorPorIdOuFalhar(Long id) {
        return prestadorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Prestador não encontrado"));
    }
}
