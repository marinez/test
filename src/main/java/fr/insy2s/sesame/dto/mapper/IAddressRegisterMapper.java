package fr.insy2s.sesame.dto.mapper;

import fr.insy2s.sesame.domain.Address;
import fr.insy2s.sesame.dto.request.AddressRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IAddressRegisterMapper {
    Address toEntity(AddressRegisterRequest addressRegisterRequest);

}