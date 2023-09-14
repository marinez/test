package fr.insy2s.sesame.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.insy2s.sesame.domain.enumeration.TypeContract;
import fr.insy2s.sesame.utils.annotation.Password;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Fethi Benseddik
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Audited
public class User extends AbstractAuditingEntity implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = -5846629789020830989L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "username", nullable = false, unique = true, length = 50, updatable = false)
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable = false, length = 50, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8, max = 60)
    @Column(name = "password", nullable = false)
    @JsonIgnore
    @Password
    private String password;

    @NotBlank
    @Size(min = 10)
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Size(max = 20)
    @Column(name = "activation_key", unique = true, length = 20)
    @JsonIgnore
    private String activationKey;

    @Column(name = "is_account_non_locked", nullable = false)
    private boolean isAccountNonLocked;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @OneToMany(mappedBy = "user")
    @NotAudited
    private List<Token> tokens;

    @ManyToOne
    @JoinColumn(name = "authority_name")
    @NotAudited
    private Authority authority;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_contract")
    private TypeContract typeContract;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "reset_password_key")
    private String resetPasswordKey;


    @Column(name = "post", length = 50)
    private String post;

    @Past
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Europe/Paris")
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "activity_zone")
    private String activityZone;

    @Column(name = "activity_service")
    private String activityService;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private User manager;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(authority.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        email = email.toLowerCase();
        username = username.toLowerCase();
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID();
        }
    }

    @PreUpdate
    public void preUpdate() {
        email = email.toLowerCase();
    }
}
