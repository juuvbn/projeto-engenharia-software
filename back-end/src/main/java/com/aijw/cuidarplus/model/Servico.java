package com.aijw.cuidarplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "servicos")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prestador_id", nullable = false)
    private Prestador contratado;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente contratante;

    @Column
    private Instant dataHorario;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusServico status;

    private BigDecimal valor;

    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialServico> materiais;

    public enum StatusServico {
        ACEITACAO_PRESTADOR_PENDENTE,
        ACEITACAO_CLIENTE_PENDENTE,
        ACEITO,
        NEGADO,
        FINALIZADO
    }
}
