package com.psw.management.service.Impl;

import com.psw.management.dto.UserProfileResponse;
import com.psw.management.entity.User;
import com.psw.management.repository.UserRepository;
import com.psw.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public User create(User entity) {
        return userRepository.save(entity);
    }


    @Override
    public User update(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getByKeycloakId(Jwt jwt) {
        String idKeycloack = jwt.getSubject();
        return userRepository.findByIdKeycloack(idKeycloack);
    }

    @Override
    public User createOrUpdateWithToken(Jwt jwt) {
        String idKeycloak = jwt.getSubject();
        String gmail = jwt.getClaimAsString("email");
        String displayName = jwt.getClaimAsString("preferred_username");

        Optional<User> existingOpt = getByKeycloakId(jwt);

        User user = existingOpt.orElseGet(User::new);
        boolean isNew = existingOpt.isEmpty();

        if (isNew) {
            user.setIdKeycloack(idKeycloak);
        }

        boolean modified = false;

        if (!Objects.equals(user.getEmail(), gmail)) {
            user.setEmail(gmail);
            modified = true;
        }

        if (!Objects.equals(user.getDisplayName(), displayName)) {
            user.setDisplayName(displayName);
            modified = true;
        }

        if (isNew || modified) {
            user = userRepository.save(user);
        }

        return user;
    }


    @Override
    public Long getIdUserByKeycloakId(Jwt jwt) {
        return getByKeycloakId(jwt)
                .map(User::getId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ese ID de Keycloak."));
    }

    @Override
    public UserProfileResponse getUserProfile(Principal principal) {
        String idKeycloak = principal.getName();

        User user = userRepository.findByIdKeycloack(idKeycloak)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getEmail(),
                user.getDisplayName(),
                user.getProfilePic()
        );
    }

    @Override
    public User updateProfilePictureByKeycloakId(String keycloakId, String profilePicUrl) {
        return userRepository.findByIdKeycloack(keycloakId)
                .map(user -> {
                    user.setProfilePic(profilePicUrl);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Keycloak ID: " + keycloakId));
    }

    @Override
    public User removeProfilePictureByKeycloakId(String keycloakId) {
        User user = userRepository.findByIdKeycloack(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setProfilePic(null);
        return userRepository.save(user);
    }


}
