package fr.insy2s.sesame.dto.mapper;

import fr.insy2s.sesame.domain.Authority;
import fr.insy2s.sesame.dto.response.AuthorityResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorityMapper {
    Authority toEntity(AuthorityResponse authorityResponse);

    AuthorityResponse toDto(Authority authority);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Authority partialUpdate(AuthorityResponse authorityResponse, @MappingTarget Authority authority);
}