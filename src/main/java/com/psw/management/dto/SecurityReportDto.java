package com.psw.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SecurityReportDto {
    private int totalPasswords;
    private List<PasswordSummaryDto> weakPasswords;
    private List<PasswordSummaryDto> repeatedPasswords;

}
