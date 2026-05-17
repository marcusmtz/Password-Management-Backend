package com.psw.management.controller;

import com.psw.management.entity.TypeElement;
import com.psw.management.service.TypeElementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TypeElementController {
    private final TypeElementService typeElementService;

    @GetMapping
    public ResponseEntity<List<TypeElement>> getAll() {
        List<TypeElement> types = typeElementService.getAll();
        return types.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeElement> getById(@PathVariable Long id) {
        return typeElementService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TypeElement> create(@Valid @RequestBody TypeElement typeElement) {
        return ResponseEntity.status(201).body(typeElementService.create(typeElement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeElement> update(@PathVariable Long id, @Valid @RequestBody TypeElement updatedTypeElement) {
        return typeElementService.getById(id)
                .map(existingType -> {
                    existingType.setName(updatedTypeElement.getName());
                    return ResponseEntity.ok(typeElementService.update(existingType));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (typeElementService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        typeElementService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
