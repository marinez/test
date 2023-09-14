package fr.insy2s.sesame.repository;

import fr.insy2s.sesame.domain.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IAuthorityRepositoryTest {

    @Autowired
    private IAuthorityRepository authorityRepository;


    @Test
    void findNameByNameNotAdmin() {
        List<Authority> allAuthorities = authorityRepository.findAll();
        List<String> allAuthoritiesWithoutAdmin = authorityRepository.findNameByNameNotAdmin();
        assertEquals(allAuthorities.size() - 1, allAuthoritiesWithoutAdmin.size());
        assertArrayEquals(allAuthorities.stream().map(Authority::getName).filter(name -> !name.equals("ROLE_ADMIN")).toArray(), allAuthoritiesWithoutAdmin.toArray());
    }

    @Test
    void existsByName() {
        List<Authority> allAuthorities = authorityRepository.findAll();
        for (Authority authority : allAuthorities) {
            assertEquals(true, authorityRepository.existsByName(authority.getName()));
        }
    }

    @Test
    void existsByNameFalse() {
        assertEquals(false, authorityRepository.existsByName("ROLE_COM"));
    }
}