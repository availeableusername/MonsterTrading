package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Entity;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository{
    private final String FIND_ALL_SQL = "SELECT * FROM users";
    private final String SAVE_SQL = "INSERT INTO users(id, name) VALUES(?, ?)";

    private final Database database = new Database();

    //@Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_SQL);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("id"),
                        rs.getString("name")
                        //rs.getString("description"),
                        //rs.getBoolean("done")
                );
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            return users;
        }
    }

    //@Override
    public Optional<Entity> find(int id) {
        return Optional.empty();
    }

    //@Override
    public User save(User user) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            //pstmt.setString(3, user.getDescription());
            //pstmt.setBoolean(4, user.isDone());
            System.out.println("in db save");
            pstmt.execute();
        } catch (SQLException e) {
            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
        }

        return user;
    }

    //@Override
    public User delete(User user) {
        return null;
    }
}
