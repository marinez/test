package fr.insy2s.sesame.service.impl;

import fr.insy2s.sesame.domain.Address;
import fr.insy2s.sesame.domain.Organization;
import fr.insy2s.sesame.dto.request.AddressRegisterRequest;
import fr.insy2s.sesame.dto.request.OrganizationRegisterRequest;
import fr.insy2s.sesame.dto.mapper.IAddressRegisterMapper;
import fr.insy2s.sesame.dto.mapper.IOrganizationRegisterMapper;
import fr.insy2s.sesame.repository.IAddressRepository;
import fr.insy2s.sesame.repository.IOrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrganizationServiceImplTest {

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Mock
    private IOrganizationRegisterMapper organizationMapper;

    @Mock
    private IAddressRegisterMapper addressMapper;
    @Mock
    private IOrganizationRepository organizationRepository;

    @Mock
    private IAddressRepository addressRepository;

    private OrganizationRegisterRequest organization;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        AddressRegisterRequest address = AddressRegisterRequest.builder()
                .street("street")
                .city("city")
                .zipCode("12345")
                .build();
        organization = OrganizationRegisterRequest.builder()
                .businessName("businessName")
                .rib("012345678901234567891234568")
                .siren("123456789")
                .activity("activity")
                .capital(1000L)
                .directorName("directorName")
                .address(address)
                .businessCreationDate(LocalDate.now().minusYears(1))
                .build();
    }
    @Test
    void createOrGetOrganizationCreate() {
        Address address = Address.builder()
                .id(1)
                .build();
        Organization organization1 = Organization.builder()
                .id(1)
                .address(address)
                .build();

        when(organizationRepository.findBySirenAndBusinessName(organization.getSiren(), organization.getBusinessName())).thenReturn(Optional.empty());
        when(organizationRepository.save(any())).thenReturn(organization1);
        when(addressRepository.save(any())).thenReturn(address);
        when(organizationMapper.toEntity(any())).thenReturn(organization1);
        when(addressMapper.toEntity(any())).thenReturn(address);

        organizationService.createOrGetOrganization(organization);
        assertEquals(1, organizationService.createOrGetOrganization(organization));
    }

    @Test
    void createOrGetOrganizationGet() {
        Organization organization1 = Organization.builder()
                .id(1)
                .build();
        when(organizationRepository.findBySirenAndBusinessName(organization.getSiren(), organization.getBusinessName())).thenReturn(Optional.of(organization1));
        organizationService.createOrGetOrganization(organization);
        assertEquals(1, organizationService.createOrGetOrganization(organization));
    }
}