package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatsRepository {

    private final String GET_USER_STATS = "SELECT * FROM stats where username=?";
    //table stats
    private final String GET_ALL_STATS = "SELECT * FROM stats order by win";

    private final Database database = new Database();

    public Response showUserStats(Request request, Response response){
        HashMap<String, String> hashMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String statsJson = null;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_USER_STATS)
        ) {
            pstmt.setString(1, request.getUsername());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                hashMap.put("Username", rs.getString("username"));
                hashMap.put("Win", String.valueOf(rs.getInt("win")));
                hashMap.put("Defeat", String.valueOf(rs.getInt("defeat")));
                hashMap.put("Draw", String.valueOf(rs.getInt("draw")));
                hashMap.put("Games", String.valueOf(rs.getInt("games")));
            }
        }
        catch(SQLException e){
            System.out.println(e);
            return response.getError();
        }

        try {
            statsJson = objectMapper.writeValueAsString(hashMap);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }
        return response.getResponse(statsJson, 200);
    }

    public Response showAllStats(Request request, Response response){
        List<HashMap<String, String>> hashList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String statsJson = null;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_ALL_STATS)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Username", rs.getString("username"));
                hashMap.put("Win", String.valueOf(rs.getInt("win")));
                hashMap.put("Defeat", String.valueOf(rs.getInt("defeat")));
                hashMap.put("Draw", String.valueOf(rs.getInt("draw")));
                hashMap.put("Games", String.valueOf(rs.getInt("games")));
                hashList.add(hashMap);
            }

        }
        catch(SQLException e){
            System.out.println(e);
            return response.getError();
        }

        try {
            statsJson = objectMapper.writeValueAsString(hashList);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }
        return response.getResponse(statsJson, 200);
    }
}
