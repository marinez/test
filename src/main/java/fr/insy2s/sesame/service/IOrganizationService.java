package fr.insy2s.sesame.service;

import fr.insy2s.sesame.dto.request.OrganizationRegisterRequest;

/**
 * IOrganizationService interface is the service to manage organizations.
 */
public interface IOrganizationService {

    /**
     * Create or get an organization if exists (siren and business name).
     *
     * @param organization: the organization to create or get.
     * @return the id of the organization.
     */
    Integer createOrGetOrganization(OrganizationRegisterRequest organization);
}
