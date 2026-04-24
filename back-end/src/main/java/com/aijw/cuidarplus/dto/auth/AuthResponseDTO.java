package com.aijw.cuidarplus.dto.auth;

import com.aijw.cuidarplus.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private String token;
    private String tokenType;
    private Long expiresIn;
}

