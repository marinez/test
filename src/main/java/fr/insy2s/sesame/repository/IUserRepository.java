package fr.insy2s.sesame.repository;


import fr.insy2s.sesame.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = u.failedLoginAttempts + 1 WHERE u.username = ?1")
    void incrementFailedAttempts(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.username = ?1")
    void resetFailedAttempts(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isAccountNonLocked = false WHERE u.username = ?1")
    void lockAccount(String username);

    Optional<User> findByActivationKey(@NonNull String activationKey);

    boolean existsByEmail(String email);

    boolean existsByActivationKey(String activationKey);

    @Query("SELECT u.email FROM User u WHERE u.activationKey = ?1")
    Optional<String> findEmailByActivationKey(@NonNull String activationKey);

    Optional<User> findByUuid(UUID uuid);

    /**
     * Check if the current user is the manager of the target user.
     *
     * @param currentUserId the current user id
     * @param targetUserId  the target user id
     * @return true if the current user is the manager of the target user, false otherwise
     * @author Fethi Benseddik
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :targetUserId AND u.manager.id = :currentUserId")
    boolean isManagerOf(@Param("currentUserId") Integer currentUserId, @Param("targetUserId") Integer targetUserId);

    /**
     * Check if the current user is the super manager of the target user.
     *
     * @param currentUserId the current user id
     * @param targetUserId  the target user id
     * @return true if the current user is the super manager of the target user, false otherwise
     * @author Fethi Benseddik
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :targetUserId AND u.manager.manager.id = :currentUserId")
    boolean isSuperManagerOf(@Param("currentUserId") Integer currentUserId, @Param("targetUserId") Integer targetUserId);



}
