package com.aijw.cuidarplus.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRegisterRequestDTO {
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

    @Schema(example = "Rua A, 123")
    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    @Schema(example = "senhaForte123")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}

