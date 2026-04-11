package com.aijw.cuidarplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(
        name = "especialidades",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_especialides_prestador_id", columnNames = {"prestador_id", "especialidade"}
        )
)
@ToString(of = { "id", "especialidade" })
public class Especialidade {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prestador_id", nullable = false)
    private Prestador prestador;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EspecialidadeEnum especialidade;

    public enum EspecialidadeEnum {
        CUIDADOR_DE_IDOSOS,
        ENFERMEIRO,
        BABA,
        CUIDADOR_DE_PETS
    }
}
