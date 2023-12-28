package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Session;
import at.technikum.apps.mtcg.entity.Entity;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SessionRepository {
    private final Database database = new Database();

    private final String FIND_USER_SQL = "SELECT * FROM users where name=? and password=?";

    public boolean CheckUserLogin(User user){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_USER_SQL)
        )
        {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbName = rs.getString("name");
                String dbPwd = rs.getString("password");
                if(Objects.equals(user.getUsername(), dbName) && Objects.equals(user.getPassword(), dbPwd)){
                    //System.out.println("User in DB");
                    return true;
                }
                else{
                    //System.out.println("User not in DB");
                    return false;
                }
        }
        } catch (SQLException e) {
            System.out.println(e);
            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
        }
        return false;
    }
}
