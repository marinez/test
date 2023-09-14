package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.domain.Address;
import fr.insy2s.sesame.domain.Organization;
import fr.insy2s.sesame.dto.request.OrganizationRegisterRequest;
import fr.insy2s.sesame.dto.mapper.IAddressRegisterMapper;
import fr.insy2s.sesame.dto.mapper.IOrganizationRegisterMapper;
import fr.insy2s.sesame.repository.IAddressRepository;
import fr.insy2s.sesame.repository.IOrganizationRepository;
import fr.insy2s.sesame.service.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * OrganizationServiceImpl class is the implementation of the service to manage organizations.
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements IOrganizationService {

    private final IOrganizationRepository organizationRepository;
    private final IAddressRepository addressRepository;

    private final IOrganizationRegisterMapper organizationMapper;
    private final IAddressRegisterMapper addressMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createOrGetOrganization(OrganizationRegisterRequest organizationRegisterRequest) {
        Optional<Organization> organizationOptional = organizationRepository.findBySirenAndBusinessName(organizationRegisterRequest.getSiren(), organizationRegisterRequest.getBusinessName());
        if (organizationOptional.isPresent())
            return organizationOptional.get().getId();

        Organization organization = organizationMapper.toEntity(organizationRegisterRequest);
        Address address = addressMapper.toEntity(organizationRegisterRequest.getAddress());
        address = addressRepository.save(address);
        organization.setAddress(address);
        organization = organizationRepository.save(organization);
        return organization.getId();
    }
}
