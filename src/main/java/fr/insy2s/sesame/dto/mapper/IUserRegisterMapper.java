package fr.insy2s.sesame.dto.mapper;

import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.dto.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserRegisterMapper {

    @Mapping(target = "authority.name", source = "authority")
    User toEntity(RegisterRequest registerRequest);

}