package com.psw.management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {
    private String nombrePsw;
    private String usuario;
    private String password;
    private String urlWebSite;
    private String description;
    private Boolean isFavorite;
    private Long idUser;
    private Long idTypeElement;
    private Long idDirectory;
}
