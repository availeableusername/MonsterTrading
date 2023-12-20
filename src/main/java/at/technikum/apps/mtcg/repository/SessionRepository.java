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
import java.util.Optional;

public class SessionRepository {
    private final Database database = new Database();

    private final String FIND_USER_SQL = "SELECT * FROM users where username=? and password=?";

    public boolean CheckUserLogin(User user){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_USER_SQL)
        )
        {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getPassword());
                //ResultSet rs = pstmt.executeQuery();
            try(ResultSet rs = pstmt.executeQuery()) {
                String dbName = rs.getString("name");
                String dbPwd = rs.getString("password");
                System.out.println("in CheckUserLogin");
                if(user.getName() == dbName && user.getPassword() == dbPwd){
                    return true;
                }
                else{
                    return false;
                }
        }
        } catch (SQLException e) {
            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
        }
        return false;
    }
}
