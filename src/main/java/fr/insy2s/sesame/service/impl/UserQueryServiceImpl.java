package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.config.Constants;
import fr.insy2s.sesame.domain.Authority_;
import fr.insy2s.sesame.domain.Organization_;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.domain.User_;
import fr.insy2s.sesame.dto.mapper.UserMapper;
import fr.insy2s.sesame.dto.request.UserCriteria;
import fr.insy2s.sesame.dto.response.UserQueryResponse;
import fr.insy2s.sesame.repository.IUserRepository;
import fr.insy2s.sesame.security.SecurityUtils;
import fr.insy2s.sesame.service.IUserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link User} queries, using Criteria API.
 *
 * @author Peter Mollet
 */
@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UserQueryServiceImpl implements IUserQueryService {

    private static final String ALL_AUTHORITY = "ALL";
    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<UserQueryResponse> getAllByQuery(Pageable pageable, UserCriteria criteria) {
        log.debug("SERVICE to get all Users by query: {}", pageable);

        Specification<User> specification = Specification.where(null);
        final String authorityCurrentUser = SecurityUtils.getCurrentUserAuthority();
        specification = specificationAuthority(specification, criteria.authority(), authorityCurrentUser);
        specification = specificationSearch(specification, criteria.search());
        specification = specificationSubordinate(specification, authorityCurrentUser);

        return userRepository.findAll(specification, pageable).map(userMapper::toQueryDto);
    }

    /**
     * Add a predicate for the authority to the specification if the authority criteria is not null.
     * @param specification The specification to update.
     * @param authorityCriteria The authority criteria.
     * @param authorityCurrentUser The authority of the current user.
     * @return The specification with the authority predicate if needed.
     */
    private Specification<User> specificationAuthority(Specification<User> specification, String authorityCriteria, String authorityCurrentUser) {
        if (authorityCriteria != null) {
            final String levelOfAccess = levelOfAccess(authorityCurrentUser, authorityCriteria);
            if (!levelOfAccess.equals(ALL_AUTHORITY)) {
                specification = specification.and((root, query, builder) -> builder.equal(root.get(User_.authority).get(Authority_.name), levelOfAccess));
            }
        }
        return specification;
    }

    /**
     * Add a predicate for the search to the specification if the search criteria is not null.
     * @param specification The specification to update.
     * @param searchCriteria The search criteria.
     * @return The specification with the search predicate if needed.
     */
    private Specification<User> specificationSearch(Specification<User> specification, String searchCriteria) {
        if (searchCriteria != null) {
            final String search = searchCriteria.toLowerCase();
            specification = specification.and((root, query, builder) -> builder.or(
                builder.like(builder.lower(builder.concat(root.get(User_.firstName), root.get(User_.lastName))), "%" + search.replace(" ", "") + "%"),
                builder.like(builder.lower(builder.concat(root.get(User_.lastName), root.get(User_.firstName))), "%" + search.replace(" ", "") + "%"),
                builder.like(builder.lower(root.get(User_.activityZone)), "%" + search + "%"),
                builder.like(builder.lower(root.get(User_.activityService)), "%" + search + "%"),
                builder.like(builder.lower(root.get(User_.organization).get(Organization_.businessName)), "%" + search + "%"))
            );
        }
        return specification;
    }

    /**
     * Add a predicate for the subordinates to the specification.
     * If admin, no predicate is added.
     * If manager, predicate is added to get only his direct subordinates.
     * If super manager, predicate is added to get his direct subordinates and their subordinates.
     * @param specification The specification to update.
     * @param authorityCurrentUser The authority of the current user.
     * @return The specification with the subordinates predicate if needed.
     */
    private Specification<User> specificationSubordinate(Specification<User> specification, String authorityCurrentUser) {
        if (authorityCurrentUser.equals(Constants.ROLE_ADMIN)) return specification;
        final String username = SecurityUtils.getCurrentUserSub();
        final Integer managerId = userRepository.findByEmail(username).orElse(new User()).getId();
        if (authorityCurrentUser.equals(Constants.ROLE_MANAGER)) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get(User_.manager).get(User_.id), managerId));
        } else if (authorityCurrentUser.equals(Constants.ROLE_SUPER_MANAGER)) {
            specification = specification.and((root, query, builder) -> builder.or(
                builder.equal(root.get(User_.manager).get(User_.id), managerId),
                builder.equal(root.get(User_.manager).get(User_.manager).get(User_.id), managerId))
            );
        }
        return specification;
    }

    /**
     * Return the level of access of the user.
     * If the user is an admin, he has access to all the users.
     * If the user is a super manager, he has access to all the users with the role manager and commercial.
     * If the user is a manager, he has access to all the users with the role commercial.
     * @param authority The authority of the current user.
     * @param levelAsked The authority asked in the criteria.
     * @return The level of access of the user.
     */
    private String levelOfAccess(String authority, String levelAsked) {
        if (authority.equals(Constants.ROLE_MANAGER)
            || (authority.equals(Constants.ROLE_SUPER_MANAGER) && (levelAsked.equals(Constants.ROLE_SUPER_MANAGER) || levelAsked.equals(Constants.ROLE_ADMIN)))) {
            return ALL_AUTHORITY;
        }
        return levelAsked;
    }

}
