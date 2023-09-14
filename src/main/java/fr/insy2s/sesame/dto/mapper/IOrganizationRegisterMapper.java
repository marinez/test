package fr.insy2s.sesame.dto.mapper;

import fr.insy2s.sesame.domain.Organization;
import fr.insy2s.sesame.dto.request.OrganizationRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrganizationRegisterMapper {
    Organization toEntity(OrganizationRegisterRequest organizationRegisterRequest);

}