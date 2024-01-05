package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Entity;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.data.Database;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRepository{
    private final String FIND_ALL_SQL = "SELECT * FROM users";
    private final String SAVE_SQL = "INSERT INTO users(id, name, password, gold) VALUES(?, ?, ?, ?)";

    private final String CHECK_FOR_EXISTING_USERS = "SELECT * FROM users where name=?";

    private final String ADD_USERDATA = "INSERT INTO userdata(username) VALUES(?)";

    private final String SHOW_USERDATA = "SELECT * FROM userdata where username=?";

    private final String UPDATE_USERDATA = "Update userdata set name=?, bio=?, image=? where username=?";
    private final String CREATE_USER_STATS = "Insert into stats(username, win, defeat, draw, games, elo) values(?,0,0,0,0,100)";

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
        if(!CheckforExistingUser(user)){
            //System.out.println("in Checkforex");
            return user;
        }
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL);
                PreparedStatement pstmt2 = con.prepareStatement(CREATE_USER_STATS)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, 4);
            pstmt.execute();

            if(!user.getUsername().equals("admin")) {
                pstmt2.setString(1, user.getUsername());
                pstmt2.execute();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return user;
        }
        addUserdata(user);

        return user;
    }

    //@Override
    public User delete(User user) {
        return null;
    }

    public boolean CheckforExistingUser(User user){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CHECK_FOR_EXISTING_USERS);
        )
        {
            pstmt.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbName = rs.getString("name");
                System.out.println(dbName);
                System.out.println(user.getUsername());
                if(Objects.equals(user.getUsername(), dbName)){
                    System.out.println("User already in DB");
                    user.setExists();
                    return false;
                }
                else{
                    //System.out.println("true");
                    return true;
                }
            }
            else{
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
        }

        System.out.println("end");
        return false;
    }

    public void addUserdata(User user){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ADD_USERDATA);
                )
        {
            pstmt.setString(1, user.getUsername());
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e);
        }

    }

    public Response showUserData(Request request, Response response){
        List<String> userdata = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String userdataJson = null;

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SHOW_USERDATA);

        ) {
            pstmt.setString(1, request.getUsername());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                userdata.add(rs.getString("username"));
                userdata.add(rs.getString("name"));
                userdata.add(rs.getString("bio"));
                userdata.add(rs.getString("image"));
            }

        } catch (SQLException e) {
            return response.getError();
        }

        for (int i = 0; i < userdata.size(); i++) {
            if (userdata.get(i) == null) {
                userdata.set(i, "Not set");
            }
        }

        try {
            userdataJson = objectMapper.writeValueAsString(userdata);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }
        return response.getResponse(userdataJson, 200);
    }

    public Response updateUserData(Request request, Response response){
        List<String> values = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> dataMap = new HashMap<>();

        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody());
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                dataMap.put(field.getKey(), field.getValue().asText());
            }
            /*for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }*/

        } catch (IOException e){
            System.out.println(e);
            return response.getError();
        }
        try (
            Connection con = database.getConnection();
            PreparedStatement pstmt = con.prepareStatement(UPDATE_USERDATA);
        ){
            pstmt.setString(1, dataMap.get("Name"));
            pstmt.setString(2, dataMap.get("Bio"));
            pstmt.setString(3, dataMap.get("Image"));
            pstmt.setString(4, request.getUsername());
            pstmt.executeUpdate();


        }
        catch (SQLException e){
            System.out.println(e);
            return response.getError();
        }

        return response.getResponse("User data was updated successfully.", 200);
    }

}
