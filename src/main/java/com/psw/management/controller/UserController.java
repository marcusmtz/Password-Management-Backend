package com.psw.management.controller;

import com.psw.management.dto.UpdateProfilePicRequest;
import com.psw.management.dto.UserProfileResponse;
import com.psw.management.entity.User;
import com.psw.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/keycloak")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getByKeycloakId(@AuthenticationPrincipal Jwt jwt) {
        return userService.getByKeycloakId(jwt)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(201).body(userService.create(user));
    }

    @PostMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> createOrUpdateWithToken(
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = userService.createOrUpdateWithToken(jwt);
        return ResponseEntity.ok(user);
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.getById(id)
                .map(existing -> {
                    user.setId(id);
                    return ResponseEntity.ok(userService.update(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/keycloak/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteByKeycloak(@AuthenticationPrincipal Jwt jwt) {
        var existing = userService.getByKeycloakId(jwt);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        userService.delete(existing.get().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getIdByJwt(@AuthenticationPrincipal Jwt jwt) {
        Long userId = userService.getIdUserByKeycloakId(jwt);
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Principal principal) {
        return ResponseEntity.ok(userService.getUserProfile(principal));
    }

    @PutMapping("/me/profile-pic")
    public ResponseEntity<User> updateProfilePicture(
            @RequestBody UpdateProfilePicRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String keycloakId = jwt.getSubject();
        User updatedUser = userService.updateProfilePictureByKeycloakId(keycloakId, request.getProfilePic());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/profile-pic/remove")
    public ResponseEntity<User> removeProfilePicture(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        User updatedUser = userService.removeProfilePictureByKeycloakId(keycloakId);
        return ResponseEntity.ok(updatedUser);
    }

}
