package com.aijw.cuidarplus.dto.prestador;

import com.aijw.cuidarplus.model.Especialidade;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import java.util.Set;

@Getter
@Setter
@Schema(name = "PrestadorUpdateDTO", description = "DTO para atualização de prestadores de serviços de saúde")
public class PrestadorUpdateDTO {
    @Schema(example = "Maria Silva", description = "Nome completo do prestador de serviços de saúde")
    @NotBlank(message = "O nome é obrigatório")
    @Length(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(
            example = "11987654321",
            description = "Número de telefone do prestador de serviços de saúde (apenas dígitos, 10 ou 11 caracteres)"
    )
    @NotBlank(message = "O telefone é obrigatório")
    @NumberFormat(pattern = "\\d{10,11}")
    private String telefone;

    @Schema(example = "mariasilva@gmail.com", description = "Endereço de email do prestador de serviços de saúde")
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotEmpty(message = "É necessário selecionar pelo menos uma especialidade")
    private Set<Especialidade.EspecialidadeEnum> especialidades;
}
