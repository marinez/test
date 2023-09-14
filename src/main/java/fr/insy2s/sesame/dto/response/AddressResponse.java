package fr.insy2s.sesame.dto.response;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link fr.insy2s.sesame.domain.Address}
 */
public record AddressResponse(@NotBlank String street,  String city, String zipCode, String country,
                              String complement, String type) implements Serializable {
}