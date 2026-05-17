package com.psw.management.service;

import com.psw.management.dto.PasswordRequest;
import com.psw.management.dto.SecurityReportDto;
import com.psw.management.entity.Password;

import java.util.List;

public interface PasswordService extends BaseService<Password> {
    Password createRequest(PasswordRequest passwordRequest);
    Password updateRequest(Long id,PasswordRequest passwordRequest);
    List<Password> getPasswordsByUserId(Long userId);
    SecurityReportDto generateSecurityReport(Long userId);
    long countFavoritesByUserId(Long userId);

}
