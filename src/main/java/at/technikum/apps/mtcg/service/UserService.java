package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public UserService() {
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public Optional<User> find(int id) {
        return Optional.empty();
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User update(int updateId, User updatedTask) {
        return null;
    }

    public User delete(User task) {
        return null;
    }
}
