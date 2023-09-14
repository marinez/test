package fr.insy2s.sesame.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "organization")
@Audited
public class Organization extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3837016212776569675L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orga_seq")
    @SequenceGenerator(name = "orga_seq", sequenceName = "orga_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotBlank
    @Column(name = "businessName", nullable = false, unique = true)
    private String businessName;

    @NotBlank
    @Size(min = 9, max = 9)
    @Column(name = "siren", length = 9, nullable = false)
    private String siren;

    @NotBlank
    @Column(name = "activity", nullable = false)
    private String activity;

    @NotNull
    @Column(name = "business_creation_date", nullable = false)
    private LocalDate businessCreationDate;

    @NotNull
    @Column(name = "capital", nullable = false)
    private Long capital;

    @NotBlank
    @Column(name = "director_name", nullable = false)
    private String directorName;

    @NotBlank
    @Size(min = 20, max = 34)
    @Column(name = "rib", length = 34)
    private String rib;

    @Column(name = "is_principal", nullable = false, columnDefinition = "boolean default false")
    private boolean isPrincipal;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @NotAudited
    private Address address;


    @PrePersist
    public void prePersist() {
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID();
        }
    }


}
