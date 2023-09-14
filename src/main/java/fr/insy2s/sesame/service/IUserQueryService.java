package fr.insy2s.sesame.service;

import fr.insy2s.sesame.dto.request.UserCriteria;
import fr.insy2s.sesame.dto.response.UserQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserQueryService {

    /**
     * Get all users by query.
     * @param pageable the pagination information
     * @param criteria the query criteria
     * @return the page of users matching the query
     */
    Page<UserQueryResponse> getAllByQuery(Pageable pageable, UserCriteria criteria);

}
