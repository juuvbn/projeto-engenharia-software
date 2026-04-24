package com.aijw.cuidarplus.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Schema(name = "ClienteUpdateDTO", description = "DTO para atualizacao de clientes")
public class ClienteUpdateDTO {
    @Schema(example = "Maria Silva", description = "Nome completo do cliente")
    @NotBlank(message = "O nome e obrigatorio")
    @Length(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(example = "11987654321", description = "Telefone do cliente (10 ou 11 digitos)")
    @NotBlank(message = "O telefone e obrigatorio")
    @NumberFormat(pattern = "\\d{10,11}")
    private String telefone;

    @Schema(example = "mariasilva@gmail.com", description = "Email do cliente")
    @NotBlank(message = "O email e obrigatorio")
    @Email(message = "O email deve ser valido")
    private String email;

    @Schema(example = "Rua dos Bobos, 1", description = "Endereco do cliente")
    @NotBlank(message = "O endereco e obrigatorio")
    private String endereco;
}

