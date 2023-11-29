package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryMemory{
    private final List<User> users = new ArrayList();

    public UserRepositoryMemory() {
    }
    public List<User> findAll() {
        return this.users;
    }

    public Optional<User> find(int id) {
        return Optional.empty();
    }

    public User save(User user) {
        this.users.add(user);
        System.out.println(users);
        return user;
    }

    public User delete(User user) {
        return user;
    }
}
