package com.aijw.cuidarplus.dto.prestador;

import com.aijw.cuidarplus.model.Especialidade;
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
public class PrestadorCreateDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Length(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O telefone é obrigatório")
    @NumberFormat(pattern = "\\d{10,11}")
    private String telefone;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotEmpty(message = "É necessário selecionar pelo menos uma especialidade")
    private Set<Especialidade.EspecialidadeEnum> especialidades;
}
