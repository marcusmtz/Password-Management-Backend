package com.psw.management.service.Impl;

import com.psw.management.dto.PasswordRequest;
import com.psw.management.dto.PasswordSummaryDto;
import com.psw.management.dto.SecurityReportDto;
import com.psw.management.entity.Password;
import com.psw.management.repository.DirectoryRepository;
import com.psw.management.repository.PasswordRepository;
import com.psw.management.repository.TypeElementRepository;
import com.psw.management.repository.UserRepository;
import com.psw.management.service.PasswordService;
import com.psw.management.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PasswordServiceImpl implements PasswordService {
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TypeElementRepository typeElementRepository;
    @Autowired
    private DirectoryRepository directoryRepository;


    @Override
    public Password create(Password entity) {
        entity.setPassword(EncryptionUtils.encrypt(entity.getPassword()));
        return passwordRepository.save(entity);
    }

    @Override
    public Password update(Password entity) {
        entity.setPassword(EncryptionUtils.encrypt(entity.getPassword()));
        return passwordRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        passwordRepository.deleteById(id);
    }

    @Override
    public Optional<Password> getById(Long id) {
        return passwordRepository.findById(id)
                .map(password -> {
                    try {
                        password.setPassword(EncryptionUtils.decrypt(password.getPassword()));
                    } catch (Exception e) {
                        System.out.println("Fallo al desencriptar: " + password.getPassword());
                        e.printStackTrace();
                    }
                    return password;
                });
    }

    @Override
    public List<Password> getAll() {
        return passwordRepository.findAll();
    }

    @Override
    public Password createRequest(PasswordRequest passwordRequest) {
        Password password = new Password();

        password.setNombrePsw(passwordRequest.getNombrePsw());
        password.setUsuario(passwordRequest.getUsuario());
        password.setPassword(EncryptionUtils.encrypt(passwordRequest.getPassword()));
        password.setUrlWebSite(passwordRequest.getUrlWebSite());
        password.setDescription(passwordRequest.getDescription());
        password.setFavorite(Boolean.TRUE.equals(passwordRequest.getIsFavorite()));

        // Asociaciones
        userRepository.findById(passwordRequest.getIdUser()).ifPresent(password::setUser);
        typeElementRepository.findById(passwordRequest.getIdTypeElement()).ifPresent(password::setTypeElement);
        directoryRepository.findById(passwordRequest.getIdDirectory()).ifPresent(password::setDirectory);

        return passwordRepository.save(password);
    }

    @Override
    public Password updateRequest(Long id, PasswordRequest passwordRequest) {
        Password existing = passwordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Password not found with id: " + id));

        if (passwordRequest.getNombrePsw() != null) {
            existing.setNombrePsw(passwordRequest.getNombrePsw());
        }
        if (passwordRequest.getUsuario() != null) {
            existing.setUsuario(passwordRequest.getUsuario());
        }
        if (passwordRequest.getPassword() != null) {
            existing.setPassword(EncryptionUtils.encrypt(passwordRequest.getPassword()));
        }
        if (passwordRequest.getUrlWebSite() != null) {
            existing.setUrlWebSite(passwordRequest.getUrlWebSite());
        }
        if (passwordRequest.getDescription() != null) {
            existing.setDescription(passwordRequest.getDescription());
        }
        if (passwordRequest.getIsFavorite() != null) {
            existing.setFavorite(passwordRequest.getIsFavorite());
        }

        // Asociaciones
        if (passwordRequest.getIdUser() != null) {
            userRepository.findById(passwordRequest.getIdUser()).ifPresent(existing::setUser);
        }
        if (passwordRequest.getIdTypeElement() != null) {
            typeElementRepository.findById(passwordRequest.getIdTypeElement()).ifPresent(existing::setTypeElement);
        }
        if (passwordRequest.getIdDirectory() != null) {
            directoryRepository.findById(passwordRequest.getIdDirectory()).ifPresent(existing::setDirectory);
        }

        return passwordRepository.save(existing);
    }

    @Override
    public List<Password> getPasswordsByUserId(Long userId) {
        List<Password> passwords = passwordRepository.findByUserId(userId);
        for (Password psw : passwords) {
            try {
                psw.setPassword(EncryptionUtils.decrypt(psw.getPassword()));
            } catch (Exception e) {
                System.out.println("Fallo al desencriptar: " + psw.getPassword());
                e.printStackTrace();
            }
        }
        return passwords;
    }

    @Override
    public SecurityReportDto generateSecurityReport(Long userId) {
        List<Password> passwords = passwordRepository.findByUserId(userId);

        List<PasswordSummaryDto> weakPasswords = new ArrayList<>();
        List<PasswordSummaryDto> repeatedPasswords = new ArrayList<>();

        Map<String, List<Password>> groupedByPassword = new HashMap<>();

        for (Password psw : passwords) {
            try {
                String decrypted = EncryptionUtils.decrypt(psw.getPassword());
                psw.setPassword(decrypted);

                groupedByPassword
                        .computeIfAbsent(decrypted, k -> new ArrayList<>())
                        .add(psw);

                if (isWeakPassword(decrypted)) {
                    weakPasswords.add(new PasswordSummaryDto(psw.getId(), psw.getNombrePsw(), decrypted, psw.getUrlWebSite()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, List<Password>> entry : groupedByPassword.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (Password psw : entry.getValue()) {
                    repeatedPasswords.add(new PasswordSummaryDto(psw.getId(), psw.getNombrePsw(), psw.getPassword(), psw.getUrlWebSite()));
                }
            }
        }

        return new SecurityReportDto(passwords.size(), weakPasswords, repeatedPasswords);
    }

    @Override
    public long countFavoritesByUserId(Long userId) {
        return passwordRepository.countFavoriteByUserId(userId);
    }

    private boolean isWeakPassword(String password) {
        if (password.length() < 8) return true;
        if (!password.matches(".*[A-Z].*")) return true;
        if (!password.matches(".*[a-z].*")) return true;
        if (!password.matches(".*[0-9].*")) return true;
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) return true;
        return false;
    }

}
