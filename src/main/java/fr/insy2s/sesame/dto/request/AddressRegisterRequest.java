package fr.insy2s.sesame.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link fr.insy2s.sesame.domain.Address}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRegisterRequest implements Serializable {
    @NotBlank
    private String street;
    //@NotBlank
    private String city;
    //@NotBlank
   // @Pattern(regexp = RegexUtils.REGEX_ZIP_CODE, message = "doit contenir 5 chiffres")
    private String zipCode;
    private String country;
    private String complement;
    private String type;
}