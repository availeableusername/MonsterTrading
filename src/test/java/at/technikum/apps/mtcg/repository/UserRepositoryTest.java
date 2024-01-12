package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    private final  UserRepository userRepository = new UserRepository();

    private final  UserRepository mock = mock();
    @Test
    public void findAllUsersSizeOf3(){
        List<User> result = userRepository.findAll();
        assertEquals(3, result.size());

    }
    @Test
    public void checkForExistingUserPass(){
        //true, user not in db
        User user = new User("test", "schmassword");
        assertTrue(userRepository.CheckforExistingUser(user));

    }
    @Test
    public void checkForExistingUserFail(){
        //false, user in db
        User user = new User("kienboec", "schmassword");
        assertFalse(userRepository.CheckforExistingUser(user));
    }
    @Test
    public void saveUserInDB(){
        User user = new User();
        user.setId("TestId");
        user.setUsername("test");
        user.setPassword("pwd");

        User user2 = new User();
        Mockito.when(mock.save(user)).thenReturn(user);
        user2 = mock.save(user);
        assertEquals(user, user2);

    }
    //19



}
