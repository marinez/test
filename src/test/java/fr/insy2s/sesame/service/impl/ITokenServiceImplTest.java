package fr.insy2s.sesame.service.impl;


import fr.insy2s.sesame.domain.Authority;
import fr.insy2s.sesame.domain.Token;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.domain.enumeration.TokenType;
import fr.insy2s.sesame.repository.ITokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ITokenServiceImplTest {

    private final String jwtToken = "jwtToken";
    @Mock
    private ITokenRepository tokenRepository;
    @InjectMocks
    private ITokenServiceImpl tokenService;
    private User user;
    private Token token;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("johndoe@example.com")

                .email("johndoe@example.com")
                .password("password")
                .authority(Authority.builder().name("ROLE_MANAGER").build())
                .build();
        token = Token.builder()
                .user(user)
                .userToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
    }

    @Test
    void saveUserToken() {
        tokenService.saveUserToken(user, jwtToken);
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository, times(1)).save(tokenCaptor.capture());
        Token capturedToken = tokenCaptor.getValue();
        assertEquals(user, capturedToken.getUser());
        assertEquals(jwtToken, capturedToken.getUserToken());
    }

    @Test
    void revokeAllUserTokens() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(token);
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(tokens);
        tokenService.revokeAllUserTokens(user);
        token.setExpired(true);
        token.setRevoked(true);
        verify(tokenRepository, times(1)).saveAll(tokens);
    }

    @Test
    void revokeAllUserTokens_emptyTokens() {
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(new ArrayList<>());
        tokenService.revokeAllUserTokens(user);
        verify(tokenRepository, never()).saveAll(anyList());
    }
}