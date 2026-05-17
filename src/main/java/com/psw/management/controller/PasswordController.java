package com.psw.management.controller;

import com.psw.management.dto.PasswordRequest;
import com.psw.management.dto.SecurityReportDto;
import com.psw.management.entity.Password;
import com.psw.management.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passwords")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class PasswordController {
    private final PasswordService passwordService;

    @GetMapping
    public ResponseEntity<List<Password>> getAll() {
        List<Password> passwords = passwordService.getAll();
        return passwords.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(passwords);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Password>> getPasswordsByUserId(@PathVariable Long userId) {
        List<Password> passwords = passwordService.getPasswordsByUserId(userId);
        return ResponseEntity.ok(passwords);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Password> getById(@PathVariable Long id) {
        return passwordService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Password> create(@Valid @RequestBody PasswordRequest passwordRequest) {
        return ResponseEntity.status(201).body(passwordService.createRequest(passwordRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Password> update(@PathVariable Long id, @Valid @RequestBody PasswordRequest updatedPassword) {
        try {
            Password updated = passwordService.updateRequest(id, updatedPassword);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (passwordService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        passwordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/security-report/{userId}")
    public ResponseEntity<SecurityReportDto> getSecurityReport(@PathVariable Long userId) {
        SecurityReportDto report = passwordService.generateSecurityReport(userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/favorites/count/{userId}")
    public long countFavorites(@PathVariable Long userId) {
        return passwordService.countFavoritesByUserId(userId);
    }


}
