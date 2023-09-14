package fr.insy2s.sesame.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IUserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    void existsByEmail() {
        assertTrue(userRepository.existsByEmail("admin@local.host"));
    }

    @Test
    void existsByEmailFalse() {
        assertFalse(userRepository.existsByEmail("wrong email"));
    }

    @Test
    void testIsManagerOf() {
        // Tester la méthode isManagerOf()
        boolean isManager = userRepository.isManagerOf(4, 10);
        assertTrue(isManager, "L'utilisateur avec l'ID 4 devrait être le manager de l'utilisateur avec l'ID 10");

        isManager = userRepository.isManagerOf(5, 10);
        assertFalse(isManager, "L'utilisateur avec l'ID 5 ne devrait pas être le manager de l'utilisateur avec l'ID 10");
    }

    @Test
    void testIsSuperManagerOf() {
        // Tester la méthode isSuperManagerOf()
        boolean isSuperManager = userRepository.isSuperManagerOf(2, 27);
        assertTrue(isSuperManager, "L'utilisateur avec l'ID 2 devrait être le super manager de l'utilisateur avec l'ID 27");

        isSuperManager = userRepository.isSuperManagerOf(2, 7);
        assertFalse(isSuperManager, "L'utilisateur avec l'ID 2 ne devrait pas être le super manager de l'utilisateur avec l'ID 7");
    }
}