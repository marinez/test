package fr.insy2s.sesame.dto.response;

public record UserQueryResponse(
        String uuid,
        String lastName,
        String firstName,
        String authority,
        String activityZone,
        String activityService,
        String organizationBusinessName,
        String managerUuid
) {
}
