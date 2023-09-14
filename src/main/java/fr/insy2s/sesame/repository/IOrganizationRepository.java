package fr.insy2s.sesame.repository;

import fr.insy2s.sesame.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findBySirenAndBusinessName(String siren, String businessName);

}
