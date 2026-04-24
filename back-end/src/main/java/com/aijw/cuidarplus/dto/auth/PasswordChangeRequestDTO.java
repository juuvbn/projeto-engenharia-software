package com.aijw.cuidarplus.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDTO {
    @Schema(example = "senhaAntiga123")
    @NotBlank(message = "A senha atual é obrigatória")
    private String senhaAtual;

    @Schema(example = "senhaNova123")
    @NotBlank(message = "A nova senha é obrigatória")
    private String novaSenha;

    @Schema(example = "senhaNova123")
    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    private String confirmacaoNovaSenha;
}
