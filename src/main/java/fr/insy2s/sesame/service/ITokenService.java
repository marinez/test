package fr.insy2s.sesame.service;


import fr.insy2s.sesame.domain.User;

/**
 * ITokenService interface defines token management operations.
 *
 * @author Fethi Benseddik
 */
public interface ITokenService {

    /**
     * Saves a user token to the repository.
     *
     * @param user     The user associated with the token
     * @param jwtToken The JWT token to save
     */
    void saveUserToken(User user, String jwtToken);

    /**
     * Revokes all tokens associated with a user.
     *
     * @param user The user for whom tokens need to be revoked
     */
    void revokeAllUserTokens(User user);
}
