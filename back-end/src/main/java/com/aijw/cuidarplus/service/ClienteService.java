package com.aijw.cuidarplus.service;

import com.aijw.cuidarplus.dto.auth.PasswordChangeRequestDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteDTO;
import com.aijw.cuidarplus.dto.cliente.ClienteUpdateDTO;
import com.aijw.cuidarplus.mapper.ClienteMapper;
import com.aijw.cuidarplus.model.Cliente;
import com.aijw.cuidarplus.repository.ClienteRepository;
import com.aijw.cuidarplus.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public ClienteDTO atualizarCliente(AuthenticatedUserPrincipal principal, ClienteUpdateDTO request) {
        Cliente cliente = buscarClientePorIdOuFalhar(principal.getId());

        clienteMapper.update(request, cliente);
        validarUnicidadeDeEmail(cliente);

        Cliente salvo = clienteRepository.saveAndFlush(cliente);
        log.info("Cliente {} atualizado com sucesso", salvo);

        return clienteMapper.map(salvo);
    }

    public void atualizarSenha(final AuthenticatedUserPrincipal principal, final PasswordChangeRequestDTO request) {
        preValidarSenhas(request);

        var cliente = buscarClientePorIdOuFalhar(principal.getId());
        validarSenha(request, cliente);

        cliente.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        clienteRepository.saveAndFlush(cliente);
        log.info("Senha do cliente {} atualizada com sucesso", cliente);
    }

    private void validarSenha(PasswordChangeRequestDTO request, Cliente cliente) {
        var senhaAtual = cliente.getSenha();

        if (!passwordEncoder.matches(request.getSenhaAtual(), senhaAtual)) {
            throw new IllegalArgumentException("A senha atual está incorreta");
        }
    }

    private Cliente buscarClientePorIdOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado"));
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

    private void preValidarSenhas(PasswordChangeRequestDTO request) {
        boolean senhaAtualDiferenteConfirmacao = !request.getSenhaAtual().equals(request.getConfirmacaoSenhaAtual());
        boolean novaSenhaIgualAtual = request.getSenhaAtual().equals(request.getNovaSenha());

        if (senhaAtualDiferenteConfirmacao) {
            throw new IllegalArgumentException("As senhas atuais não coincidem");
        }

        if (novaSenhaIgualAtual) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual");
        }
    }
}
