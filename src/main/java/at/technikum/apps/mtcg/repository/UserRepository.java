package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository{
    private final List<User> users = new ArrayList();

    public UserRepository() {
    }
    public List<User> findAll() {
        return this.users;
    }

    public Optional<User> find(int id) {
        return Optional.empty();
    }

    public User save(User user) {
        user.setId(this.users.size() + 1);
        this.users.add(user);
        return user;
    }

    public User delete(User user) {
        return user;
    }
}
