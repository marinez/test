package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.domain.User;

import fr.insy2s.sesame.domain.enumeration.TypeContract;
import fr.insy2s.sesame.dto.mapper.UserMapper;
import fr.insy2s.sesame.dto.request.UpdatePwdRequest;
import fr.insy2s.sesame.dto.request.UserPersonalUpdateRequest;
import fr.insy2s.sesame.dto.response.TypeContractResponse;
import fr.insy2s.sesame.dto.response.UserResponse;
import fr.insy2s.sesame.error.exception.BadRequestException;
import fr.insy2s.sesame.error.exception.UnauthorizedException;
import fr.insy2s.sesame.error.exception.UserNotFoundException;
import fr.insy2s.sesame.repository.IUserRepository;

import fr.insy2s.sesame.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * UserServiceImpl class is the implementation of the service to manage users and their contract types.
 *
 * @author Fethi Benseddik
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;



    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeContractResponse> getAllTypeContract() {
        return Arrays.stream(TypeContract.values())
                .map(TypeContractResponse::new)
                .toList();

    }

    /**
     * {@inheritDoc}
     */
    @Override

    public boolean emailAlreadyExists(String email) {
        log.debug("SERVICE to check if email already exists : {}", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse getUserByUuid(String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Current authenticated user not found"));
        User targetUser = userRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!isUserSuperior(currentUser, targetUser)) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        log.info("User with role {} accessed user with UUID {}", currentUser.getAuthority().getName(), uuid);
        return userMapper.toDto(targetUser);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserSuperior(User currentUser, User targetUser) {
        String role = currentUser.getAuthority().getName();
        Integer currentUserId = currentUser.getId();

        // Pour tout rôle, l'utilisateur peut voir son propre compte
        if (currentUserId.equals(targetUser.getId())) {
            return true;
        }

        return switch (role) {
            case "ROLE_ADMIN" ->
                // Un admin peut voir tout le monde
                    true;
            case "ROLE_SUPER_MANAGER" ->
                // Un super manageur peut voir son propre compte, les comptes de ses manageurs et les comptes des vendeurs de ses manageurs
                    userRepository.isSuperManagerOf(currentUserId, targetUser.getId()) ||
                            userRepository.isManagerOf(currentUserId, targetUser.getId());
            case "ROLE_MANAGER" ->
                // Un manageur peut voir son propre compte et les comptes de ses vendeurs
                    userRepository.isManagerOf(currentUserId, targetUser.getId());
            case "ROLE_COMMERCIAL" ->
                // Un vendeur ne peut voir que son propre compte, ce qui est déjà couvert par la première condition
                    false;
            default -> false;
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePasswordOfUser(String currentUsername, UpdatePwdRequest updatePwdRequest) {
        log.debug("SERVICE to update password of user {}", currentUsername);
        final User user = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        final boolean isOldPasswordCorrect = passwordEncoder.matches(updatePwdRequest.oldPassword(), user.getPassword());
        if(!isOldPasswordCorrect) throw new BadRequestException("Ancien mot de passe incorrect");

        final boolean isNewPasswordDifferent = !passwordEncoder.matches(updatePwdRequest.newPassword(), user.getPassword());
        if (!isNewPasswordDifferent) throw new BadRequestException("Nouveau mot de passe identique à l'ancien");

        final String encodedPassword = passwordEncoder.encode(updatePwdRequest.newPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(String uuid, UserPersonalUpdateRequest request) {
        log.debug("SERVICE to update user {}", uuid);
        User user = userRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User newUser = userMapper.partialUpdate(request, user);
        userRepository.save(newUser);}
}
