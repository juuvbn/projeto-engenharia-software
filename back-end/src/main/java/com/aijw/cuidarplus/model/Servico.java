package com.aijw.cuidarplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
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

    @Column(length = 1024, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusServico status;

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Instant creationTimestamp;

    @Column(nullable = false)
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updateTimestamp;

    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialServico> materiais = Collections.emptyList();

    public enum StatusServico {
        ACEITACAO_PRESTADOR_PENDENTE,
        ACEITACAO_CLIENTE_PENDENTE,
        ACEITO,
        NEGADO,
        FINALIZADO
    }

    @PrePersist
    @PreUpdate
    public void calcularValor() {
        valor = materiais.stream().reduce(BigDecimal.ZERO, (total, material) -> total.add(material.getValor().multiply(BigDecimal.valueOf(material.getQuantidade()))), BigDecimal::add);
    }
}
