package fr.insy2s.sesame.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Marine Zimmer
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5771867880271782963L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "add_seq")
    @SequenceGenerator(name = "add_seq", sequenceName = "add_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotBlank
    @Column(name = "street")
    private String street;

    //@NotBlank
    @Column(name = "city")
    private String city;

    //@NotBlank
    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    private String country ;

    @Column(name = "complement")
    private String complement;

    @Column(name = "type")
    private String type;


    @PrePersist
    public void prePersist() {
        if(country == null) {
            country = "FRANCE";
        }
        country = country.toUpperCase();
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID();
        }
    }


}
