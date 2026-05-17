package com.psw.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordSummaryDto {
    private Long id;
    private String nombrePsw;
    private String password;
    private String urlWebSite;
}
