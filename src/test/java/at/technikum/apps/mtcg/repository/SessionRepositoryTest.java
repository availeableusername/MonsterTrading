package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionRepositoryTest {
    private final SessionRepository sessionRepository = new SessionRepository();

    @Test
    void checkUserLoginPass() {
        User user = new User();
        user.setUsername("kienboec");
        user.setPassword("daniel");
        assertTrue(sessionRepository.CheckUserLogin(user));

    }
    @Test
    void checkUserLoginFail() {
        User user = new User();
        user.setUsername("kienboec");
        user.setPassword("schmaniel");
        assertFalse(sessionRepository.CheckUserLogin(user));

    }
}