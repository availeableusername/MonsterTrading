package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
//mit alt+enter mauszeiger auf klasse, können tests erstellt werden
class UserRepositoryMemoryTest {

    @Test
    void findAllTest(){
        UserRepositoryMemory repo = new UserRepositoryMemory();
        User user = new User("test", "unitTest");
        repo.save(user);

    }

}