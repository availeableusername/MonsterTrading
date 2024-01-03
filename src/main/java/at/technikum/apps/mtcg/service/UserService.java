package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.repository.UserRepositoryMemory;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.List;
import java.util.Optional;

public class UserService {
    //private final UserRepositoryMemory userRepository = new UserRepositoryMemory();
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

    public Response showUserData(Request request, Response response){
        return this.userRepository.showUserData(request, response);
    }
    public Response updateUserData(Request request, Response response) {
        return userRepository.updateUserData(request, response);
    }

    public User delete(User task) {
        return null;
    }
}
