package com.psw.management.controller;

import com.psw.management.entity.Directory;
import com.psw.management.service.DirectoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class DirectoryController {
    private final DirectoryService directoryService;

    @GetMapping
    public ResponseEntity<List<Directory>> getAll() {
        List<Directory> directories = directoryService.getAll();
        return directories.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(directories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Directory> getById(@PathVariable Long id) {
        return directoryService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Directory> create(@Valid @RequestBody Directory directory) {
        return ResponseEntity.status(201).body(directoryService.create(directory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Directory> update(@PathVariable Long id, @Valid @RequestBody Directory updatedDirectory) {
        return directoryService.getById(id)
                .map(existingDirectory -> {
                    existingDirectory.setName(updatedDirectory.getName());
                    return ResponseEntity.ok(directoryService.update(existingDirectory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (directoryService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        directoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Directory>> getByUserId(@PathVariable Long userId) {
        List<Directory> directories = directoryService.getByUserId(userId);
        return ResponseEntity.ok(directories);
    }
}
