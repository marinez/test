package fr.insy2s.sesame.repository;

import fr.insy2s.sesame.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAuthorityRepository extends JpaRepository<Authority, String> {
    @Query("select a.name from Authority a where a.name <> 'ROLE_ADMIN'")
    List<String> findNameByNameNotAdmin();

    boolean existsByName(String authority);
}
