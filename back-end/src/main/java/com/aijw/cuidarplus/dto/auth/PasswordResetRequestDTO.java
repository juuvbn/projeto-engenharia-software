package com.aijw.cuidarplus.dto.auth;

import com.aijw.cuidarplus.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDTO {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotNull(message = "O tipo de usuário é obrigatório")
    private TipoUsuario tipoUsuario;
}
