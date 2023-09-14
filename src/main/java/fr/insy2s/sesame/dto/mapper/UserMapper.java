package fr.insy2s.sesame.dto.mapper;

import fr.insy2s.sesame.dto.request.UserPersonalUpdateRequest;
import fr.insy2s.sesame.domain.User;
import fr.insy2s.sesame.dto.response.UserQueryResponse;
import fr.insy2s.sesame.dto.response.UserResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuthorityMapper.class, OrganizationMapper.class})
public interface UserMapper {
    User toEntity(UserResponse userResponse);

    @Mapping(source = "manager.firstName", target = "managerFirstName")
    @Mapping(source = "manager.lastName", target = "managerLastName")
    @Mapping(source = "manager.uuid", target = "managerUuid")
    UserResponse toDto(User user);

    @Mapping(source = "authority.name", target = "authority")
    @Mapping(source = "organization.businessName", target = "organizationBusinessName")
    @Mapping(source = "manager.uuid", target = "managerUuid")
    UserQueryResponse toQueryDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserResponse userResponse, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserPersonalUpdateRequest userPersonalUpdateRequest, @MappingTarget User user);

    User toEntity(UserPersonalUpdateRequest userPersonalUpdateRequest);

    UserPersonalUpdateRequest toDtoUserUpdate(User user);
}