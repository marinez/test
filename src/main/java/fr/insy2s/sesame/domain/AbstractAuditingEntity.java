package fr.insy2s.sesame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class AbstractAuditingEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = 1389270909488702734L;

    @NotNull
    @Size(max = 50)
    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @NotAudited
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @NotAudited
    private Instant createdDate = Instant.now();

    @Size(max = 50)
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    @NotAudited
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @NotAudited
    private Instant lastModifiedDate = Instant.now();

    @Column(name = "uuid", updatable = false, nullable = false, unique = true)
    private UUID uuid = UUID.randomUUID();
}
