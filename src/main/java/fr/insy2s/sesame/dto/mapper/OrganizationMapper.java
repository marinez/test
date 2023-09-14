package fr.insy2s.sesame.dto.mapper;

import fr.insy2s.sesame.domain.Organization;
import fr.insy2s.sesame.dto.response.OrganizationResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {
    Organization toEntity(OrganizationResponse organizationResponse);

    OrganizationResponse toDto(Organization organization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Organization partialUpdate(OrganizationResponse organizationResponse, @MappingTarget Organization organization);
}