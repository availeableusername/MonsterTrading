package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Entity;
import at.technikum.apps.mtcg.entity.User;

import java.util.List;
import java.util.Optional;

public interface Repository {

    List<User> findAll();

    Optional<Entity> find(int id);

    Entity save(Entity entity);

    Entity delete(Entity entity);
}