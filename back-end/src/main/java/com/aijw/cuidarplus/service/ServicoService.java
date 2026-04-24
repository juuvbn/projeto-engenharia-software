package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.servico.MaterialServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoCreateDTO;
import com.aijw.cuidarplus.dto.servico.ServicoDTO;
import com.aijw.cuidarplus.dto.servico.ServicoPropostaDTO;
import com.aijw.cuidarplus.mapper.MaterialServicoMapper;
import com.aijw.cuidarplus.mapper.ServicoMapper;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.model.MaterialServico;
import com.aijw.cuidarplus.model.Prestador;
import com.aijw.cuidarplus.model.Servico;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.repository.PrestadorRepository;
import com.aijw.cuidarplus.repository.ServicoRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;

    private final ServicoMapper servicoMapper;
    private final MaterialServicoMapper materialServicoMapper;

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
        servico.setStatus(Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE);

        return servico;
    }


    public ServicoDTO aceitarProposta(AuthenticatedUserPrincipal principal, Long servicoId, ServicoPropostaDTO request) {
        var servico = buscarServicoPorIdEStatusOuFalhar(servicoId, Servico.StatusServico.ACEITACAO_PRESTADOR_PENDENTE);
        validarPrestadorDoServico(principal, servico);

        List<MaterialServico> materiaisEntityList = criarMateriais(request.getMateriais());

        servico.setMateriais(materiaisEntityList);
        servico.setDataHorario(request.getDataHorario());
        servico.setStatus(Servico.StatusServico.ACEITACAO_CLIENTE_PENDENTE);

        return servicoMapper.map(servicoRepository.saveAndFlush(servico));
    }

    private List<MaterialServico> criarMateriais(List<MaterialServicoCreateDTO> materiais) {
        var list = new ArrayList<MaterialServico>();

        for (MaterialServicoCreateDTO dto : materiais) {
            var material = materialServicoMapper.map(dto);

            list.add(material);
        }

        return list;
    }

    private void validarPrestadorDoServico(AuthenticatedUserPrincipal principal, Servico servico) {
        if (!servico.getContratado().getId().equals(principal.getId())) {
            throw new IllegalArgumentException("Apenas o prestador designado pode aceitar esta proposta");
        }
    }

    private Cliente buscarClientePorIdOuFalhar(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    private Prestador buscarPrestadorPorIdOuFalhar(Long id) {
        return prestadorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Prestador não encontrado"));
    }

    private Servico buscarServicoPorIdEStatusOuFalhar(Long id, Servico.StatusServico status) {
        return servicoRepository.findByIdAndStatusEquals(id, status)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado ou status inválido"));
    }
}
