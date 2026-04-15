package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public Cliente registrarCliente(Cliente cliente) {
        validarUnicidadeDeEmail(cliente);

        Cliente salvo = clienteRepository.saveAndFlush(cliente);
        log.info("Cliente {} registrado com sucesso", salvo);

        return salvo;
    }

    private void validarUnicidadeDeEmail(Cliente cliente) {
        if (cliente.getId() == null) {
            if (clienteRepository.existsByEmailIgnoreCase(cliente.getEmail())) {
                throw new IllegalArgumentException("Já existe um cliente com o mesmo email");
            }
        } else if (clienteRepository.existsByIdNotAndEmailIgnoreCase(cliente.getId(), cliente.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente com o mesmo email");
        }
    }
}
