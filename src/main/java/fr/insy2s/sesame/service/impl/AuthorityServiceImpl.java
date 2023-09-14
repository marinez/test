package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.repository.IAuthorityRepository;
import fr.insy2s.sesame.service.IAuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AuthorityServiceImpl class implements the IAuthorityService interface
 * and provides authority-related operations.
 *
 * @author Marine Zimmer
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityServiceImpl implements IAuthorityService {
    private final IAuthorityRepository authorityRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAutoritiesWithoutAdmin() {
        log.debug("SERVICE to get all authorities without admin");
        return authorityRepository.findNameByNameNotAdmin();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean authorityExist(String authority) {
        log.debug("SERVICE to check if authority exist");
        return authorityRepository.existsByName(authority);
    }

}
