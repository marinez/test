package fr.insy2s.sesame.error.record;

public record FieldError(
        String entityName,
        String fieldName,
        String message,
        String code
) {
}
