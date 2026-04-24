package com.aijw.cuidarplus.dto.auth;

import com.aijw.cuidarplus.model.Especialidade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PrestadorRegisterRequestDTO {
    @Schema(example = "Maria Silva")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(example = "11999990000")
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Schema(example = "maria@example.com")
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @Schema(example = "senhaForte123")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotEmpty(message = "É necessário selecionar pelo menos uma especialidade")
    private Set<Especialidade.EspecialidadeEnum> especialidades;
}

