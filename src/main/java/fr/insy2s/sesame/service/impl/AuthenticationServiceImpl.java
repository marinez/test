package fr.insy2s.sesame.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insy2s.sesame.config.Constants;
import fr.insy2s.sesame.config.JwtService;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.dto.request.AuthenticationRequest;
import fr.insy2s.sesame.dto.request.FirstAuthRequest;
import fr.insy2s.sesame.dto.request.RegisterRequest;
import fr.insy2s.sesame.dto.mapper.IUserRegisterMapper;
import fr.insy2s.sesame.dto.response.AuthenticationResponse;
import fr.insy2s.sesame.error.exception.AccountLockedException;
import fr.insy2s.sesame.error.exception.BadCredentialsException;
import fr.insy2s.sesame.repository.IAuthorityRepository;
import fr.insy2s.sesame.repository.IUserRepository;
import fr.insy2s.sesame.service.*;
import fr.insy2s.sesame.utils.service.IRandomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

/**
 * AuthenticationServiceImpl class implements the IAuthenticationService interface
 * and provides authentication-related operations.
 *
 * @author Fethi Benseddik
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserRegisterMapper userRegisterMapper;
    private final IRandomService randomService;
    private final IAuthorityRepository authorityRepository;
    private final IOrganizationService organizationService;

    private final ObjectMapper objectMapper;
    private final IMailService mailService;



    /**
     * {@inheritDoc}
     */
    @Override
    public void register(RegisterRequest request) {
        User user = userRegisterMapper.toEntity(request);
        user.setActivated(false);
        do {
            user.setActivationKey(randomService.generateRandomUUIDString(Constants.LENGTH_ACTIVATION_KEY));
        } while (userRepository.existsByActivationKey(user.getActivationKey()));
        user.setPassword(passwordEncoder.encode(randomService.generateRandomUUIDString(Constants.LENGTH_PASSWORD)));
        user.setUsername(request.getEmail());
        user.getOrganization().setId(organizationService.createOrGetOrganization(request.getOrganization()));
        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
        var savedUser = userRepository.save(user);

        mailService.sendActivationEmail(savedUser.getEmail(), savedUser.getUsername(), savedUser.getActivationKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null && !user.isAccountNonLocked()) {
            throw new AccountLockedException("Vous avez depassé le nombre de tentatives de connexion autorisées. Veuillez contacter l'administrateur.");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            userRepository.resetFailedAttempts(request.getEmail());
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            tokenService.revokeAllUserTokens(user);
            tokenService.saveUserToken(user, jwtToken);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e) {
            if (user != null && user.getFailedLoginAttempts() < Constants.MAX_FAILED_ATTEMPTS) {
                userRepository.incrementFailedAttempts(request.getEmail());
            }
            user = userRepository.findByEmail(request.getEmail()).orElse(null);

            if (user != null && user.getFailedLoginAttempts() >= Constants.MAX_FAILED_ATTEMPTS) {
                userRepository.lockAccount(request.getEmail());
                throw new AccountLockedException("Vous avez depassé le nombre de tentatives de connexion autorisées. Veuillez contacter l'administrateur.");
            } else {
                throw new BadCredentialsException("Identifiants incorrects.");
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                tokenService.revokeAllUserTokens(user);
                tokenService.saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AuthenticationResponse> firstConnection(FirstAuthRequest request) {
        log.debug("SERVICE to first connection of the user with token : {}", request.tokenRegister());

        Optional<User> opUser = this.userRepository.findByActivationKey(request.tokenRegister());
        if (opUser.isEmpty()) return Optional.empty();

        User user = opUser.get();
        final String passwordHash = passwordEncoder.encode(request.password());
        user.setPassword(passwordHash);
        user.setActivated(true);
        user.setActivationKey(null);
        this.userRepository.save(user);

        final String jwtToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, jwtToken);
        return Optional.of(AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<String> getEmailByActivationKey(String activationKey) {
        log.debug("SERVICE to get email by activation key : {}", activationKey);
        return this.userRepository.findEmailByActivationKey(activationKey);
    }


}
