package fr.insy2s.sesame.service.impl;


import fr.insy2s.sesame.domain.Token;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.domain.enumeration.TokenType;
import fr.insy2s.sesame.repository.ITokenRepository;
import fr.insy2s.sesame.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ITokenServiceImpl class implements the ITokenService interface and manages user tokens.
 *
 * @author Fethi Benseddik
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ITokenServiceImpl implements ITokenService {
    private final ITokenRepository tokenRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .userToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
