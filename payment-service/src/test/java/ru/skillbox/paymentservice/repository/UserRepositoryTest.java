package ru.skillbox.paymentservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepositoryJpa;

    @Test
    public void addUser() {
        long userId = 9L;
        User user = new User();
        user.setId(userId);
        entityManager.persist(user);
        entityManager.flush();

        User gotUser = userRepositoryJpa.findById(userId).get();
        assertThat(gotUser.getBalance()).isEqualTo(0);
    }
}
