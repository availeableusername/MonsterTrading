package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionRepository {
    private final String GET_CARDS = "SELECT * FROM cards where taken='free' order by chronology limit 5";

    private final String SET_OWNER = "UPDATE cards set taken=? where id=?";

    private final String GET_GOLD = "SELECT gold FROM users where name=?";

    private final String UPDATE_GOLD = "UPDATE users set gold=? where name=?";


    private final Database database = new Database();

    public Response acquireCards(Request request){
        Response response = new Response();
        Pattern pattern = Pattern.compile("Bearer\\s(\\w+)-mtcgToken");
        Matcher matcher = pattern.matcher(request.getToken());
        String username;
        String[] ids = new String[5];
        int i = 0;

        if (matcher.find()) {
            username = matcher.group(1);
            //System.out.println(username);
        } else {
            System.out.println("Matcher Problem");
            return response.getError();
        }

        if(request.getToken().isEmpty()){
            String msg = "Missing token";
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(msg);
            return response;
        }
        if(!checkGold(request, username)){
            String msg = "No gold left";
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(msg);
            return response;
        }
        //System.out.println("Erfolgreich");
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_CARDS);
                PreparedStatement pstmt2 = con.prepareStatement(SET_OWNER);
                ResultSet rs = pstmt.executeQuery();
        ) {
            pstmt2.setString(1, username);
            System.out.println(username);
            if(!rs.next()){ //
                System.out.println("No packages left");
                restoreGold(username);
                return response.getResponse("No packages left", 200);
            } else {
               do {
                    String id = rs.getString("id");
                    System.out.println("Id: " + id);
                    ids[i] = id;
                    i++;
                }while (rs.next());
                i--;
            }
            //System.out.println(ids[0]);
            //System.out.println(ids[4]);
            while(i >= 0){
                pstmt2.setString(1, username);
                pstmt2.setString(2, ids[i]);
                pstmt2.executeUpdate();
                i--;
            }

        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("acquireCards");
        }
        String msg = "Successfully acquired package";
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(msg);
        return response;

    }

    public boolean checkGold(Request request, String username) {
        try(
            Connection con = database.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_GOLD);
            PreparedStatement pstmt2 = con.prepareStatement(UPDATE_GOLD);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int gold = rs.getInt("gold");
                if(gold <= 0){
                    return false;
                } else{
                    gold--;
                    pstmt2.setInt(1, gold);
                    pstmt2.setString(2, username);
                    pstmt2.executeUpdate();
                    return true;
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("checkGold");
        }


        return false;
    }

    public void restoreGold(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_GOLD);
                PreparedStatement pstmt2 = con.prepareStatement(UPDATE_GOLD);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int gold = rs.getInt("gold");
                gold++;
                pstmt2.setInt(1, gold);
                pstmt2.setString(2, username);
                pstmt2.executeUpdate();

            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("restoreGold");
        }
    }
}
