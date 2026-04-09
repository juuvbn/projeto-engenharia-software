package com.aijw.cuidarplus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PrestadorDTO {
    private String nome;
    private String telefone;
    private String email;
    private Set<String> especialidades;
}
