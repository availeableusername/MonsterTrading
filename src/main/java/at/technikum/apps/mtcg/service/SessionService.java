package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.SessionRepository;
import at.technikum.apps.mtcg.repository.UserRepository;

public class SessionService {

    private final SessionRepository sessionRepository = new SessionRepository();

    public boolean CheckUserLogin(User user){
        sessionRepository.CheckUserLogin(user);
        return true;
    }
}
