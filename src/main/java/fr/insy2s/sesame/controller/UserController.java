package fr.insy2s.sesame.controller;

import fr.insy2s.sesame.config.Constants;
import fr.insy2s.sesame.dto.request.UserPersonalUpdateRequest;
import fr.insy2s.sesame.dto.request.UpdatePwdRequest;
import fr.insy2s.sesame.dto.request.UserCriteria;
import fr.insy2s.sesame.dto.response.UserQueryResponse;
import fr.insy2s.sesame.dto.response.UserResponse;
import fr.insy2s.sesame.error.exception.AccountException;
import fr.insy2s.sesame.security.SecurityUtils;
import fr.insy2s.sesame.service.IMailService;
import fr.insy2s.sesame.service.IUserQueryService;
import fr.insy2s.sesame.service.IUserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

/**
 * UserController class manages users and their contract types.
 *
 * @RestController Indicates that the class defines RESTful endpoints.
 * @RequestMapping Maps the controller to the base URL "/api/v1/users".
 * @author Fethi Benseddik
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IUserQueryService userQueryService;
    private final IMailService mailService;


    /**
     * Fetch all available contract types.
     *
     * <p>Only users with the 'ADMIN' role are authorized to access this resource.</p>
     *
     * @return A list of all contract types, or an error if an exception is thrown.
     */
    @GetMapping("/type-contract")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?>getAllTypeContract(){
      try{
          return new ResponseEntity<>(userService.getAllTypeContract(), HttpStatus.OK);
    }catch (Exception e){
          log.error("Error while getting all type contract", e);
          return new ResponseEntity<>("Erreur serveur", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    /**
     * Fetch a user by UUID.
     *
     * <p>Only users with the 'ADMIN', 'SUPER_MANAGER' or 'MANAGER' role are authorized to access this resource.</p>
     *
     * @param uuid The UUID of the user to fetch
     * @return The user with the specified UUID, or an error if an exception is thrown.
     * @author Fethi Benseddik
     */
    @GetMapping("/{uuid}")
    @RolesAllowed({"ADMIN", "SUPER_MANAGER", "MANAGER"})
    public ResponseEntity<UserResponse> getUserByUuid(@PathVariable String uuid) {
        log.debug("REST request to get user by uuid {}", uuid);
        return ResponseEntity.ok(userService.getUserByUuid(uuid));
    }

    /**
     * Update a user.
     * @param uuid the uuid of the user to update
     * @param request the new user data
     * @return {@code 200} if the user has been updated,
     * {@code 400} if the user is not valid, or the user is not found,
     */
    @PatchMapping("/{uuid}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> updateUser(@PathVariable String uuid, @RequestBody UserPersonalUpdateRequest request) {
        log.debug("REST request to update user {} {}", uuid, request);
        if (Objects.nonNull(request.birthday()) && request.birthday().isAfter(LocalDate.now().minusYears(Constants.YEAR_MINIMUM))) {
            throw new AccountException("l'utilisateur doit avoir au moins 18 ans");
        }
        userService.updateUser(uuid, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Update the password of the current user.
     * @param updatePwdRequest the new password.
     * @return {@code 200} if the password has been updated,
     * {@code 400} if the password is not valid, or identical to the old one, or old password is not correct,
     */
    @PutMapping("/password")
    public ResponseEntity<Void> updatePasswordCurrentUser(@RequestBody@Valid UpdatePwdRequest updatePwdRequest){

        final String currentUsername = SecurityUtils.getCurrentUserSub();
        log.info("User {} request to update his password", currentUsername);
        userService.updatePasswordOfUser(currentUsername, updatePwdRequest);
        log.info("User {} successfully updated his password", currentUsername);
        mailService.sendNotificationPasswordUpdated(currentUsername);
        return ResponseEntity.ok().build();
    }

    /**
     * Fetch all users, by using query parameters.
     * @param pageable the pagination information.
     * @return a page of users.
     * @author Peter Mollet
     */
    @GetMapping
    @RolesAllowed({Constants.ADMIN, Constants.SUPER_MANAGER, Constants.MANAGER})
    public Page<UserQueryResponse> getAllByQuery(Pageable pageable, UserCriteria criteria) {
        log.debug("REST request to get all users by query: {}, criteria: {}", pageable, criteria);
        return userQueryService.getAllByQuery(pageable, criteria);
    }

}
