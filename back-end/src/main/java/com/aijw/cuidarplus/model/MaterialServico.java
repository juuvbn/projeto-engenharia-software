package com.aijw.cuidarplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "materiais_servico")
@ToString(onlyExplicitlyIncluded = true)
public class MaterialServico {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ManyToOne
    @ToString.Include
    @JoinColumn(name = "servico_id", nullable = false, updatable = false)
    private Servico servico;

    @ToString.Include
    private String produto;

    private Integer quantidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;
}
