package fr.insy2s.sesame.service;

import fr.insy2s.sesame.dto.request.UserPersonalUpdateRequest;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.dto.request.UpdatePwdRequest;
import fr.insy2s.sesame.dto.response.TypeContractResponse;
import fr.insy2s.sesame.dto.response.UserResponse;
import fr.insy2s.sesame.error.exception.UnauthorizedException;
import fr.insy2s.sesame.error.exception.UserNotFoundException;

import java.util.List;

public interface IUserService {

    /**
     * Fetch all available contract types.
     *
     * @return A list of {@link TypeContractResponse} representing all contract types.
     * @author Fethi Benseddik
     */
    List<TypeContractResponse> getAllTypeContract();

    /**
     * Check if the email already exists in the database.
     *
     * @param email: the email to check.
     * @return true if the email already exists, false otherwise.
     */
    boolean emailAlreadyExists(String email);

    /** Retrieves the user details associated with the given UUID.
     * <p>
     * The method applies business rules to determine whether the currently
     * authenticated user has the permission to access the details of the target user.
     * </p>
     *
     * @param uuid The UUID of the target user.
     * @return UserResponse object containing details of the target user.
     * @throws UserNotFoundException if the user associated with the UUID is not found.
     * @throws UnauthorizedException if the currently authenticated user does not have the
     *                               permission to access the details of the target user.
     * @author Fethi Benseddik
     */
    UserResponse getUserByUuid(String uuid);

    /**
     * Check if the user is superior to the target user.
     *
     * @param currentUser: the current user.
     * @param targetUser: the target user.
     * @return true if the user is superior to the target user, false otherwise.
     * @author Fethi Benseddik
     */
    boolean isUserSuperior(User currentUser, User targetUser);

    /**
     * Update the password of a username.
     * @param currentUsername the username of the user who wants to update his password.
     * @param updatePwdRequest the new password.
     */
    void updatePasswordOfUser(String currentUsername, UpdatePwdRequest updatePwdRequest);

    /**
     * Update the user.
     *
     * @param uuid: the uuid of the user to update.
     * @param request: the request containing the new user data.
     * @author Fethi Benseddik
     */
    void updateUser(String uuid, UserPersonalUpdateRequest request);
}
