package com.psw.management.service;

import com.psw.management.dto.UserProfileResponse;
import com.psw.management.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;
import java.util.Optional;

public interface UserService extends BaseService<User>{
    Optional<User> getByKeycloakId(Jwt jwt);
    User createOrUpdateWithToken(Jwt jwt);
    Long getIdUserByKeycloakId(Jwt jwt);
    UserProfileResponse getUserProfile(Principal principal);
    User updateProfilePictureByKeycloakId(String keycloakId, String profilePicUrl);
    User removeProfilePictureByKeycloakId(String keycloakId);
}
