package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface Repository {

    List<Entity> findAll();

    Optional<Entity> find(int id);

    Entity save(Entity entity);

    Entity delete(Entity entity);
}