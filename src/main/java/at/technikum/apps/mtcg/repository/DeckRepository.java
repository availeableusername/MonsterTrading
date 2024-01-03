package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckRepository {
    private final String GET_DECK = "SELECT * FROM cards where taken=? and deck='yes'";

    private final Database database = new Database();
    public Response showDeck(Request request){
        Response response = new Response();
        String username = request.getUsername();
        List<Card> cards = new ArrayList<>();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_DECK)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){ //
                //System.out.println("Deck not configured, please configure your deck first.");
                return response.getResponse("Deck not configured, please configure your deck first.", 400);
            } else {
                do {
                    Card card = new Card(
                            rs.getString("name"),
                            rs.getInt("damage"),
                            rs.getString("type"),
                            rs.getString("category"),
                            rs.getString("id")

                    );
                    cards.add(card);
                }while (rs.next());
            }
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("showDeck");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String cardsJson;

        try {
            cardsJson = objectMapper.writeValueAsString(cards);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }
        return response.getResponse(cardsJson, 200);
    }

    public Response configureDeck(Request request){
        ObjectMapper objectMapper = new ObjectMapper();
        Response response = new Response();
        String jsonString = request.getBody();
        List<String> ids;

        try {
            ids = objectMapper.readValue(jsonString, List.class);
        } catch (IOException e) {
            e.printStackTrace();
            return response.getError();
        }

        assert ids != null;
        if(ids.size() < 4){
            return response.getResponse("You need to enter 4 ids.", 400);
        }
        //checkOwner returns 200 if cards are owned by user, if response code is not 200 error occurred
        response = checkOwner(request, ids);
        if(response.getStatusCode() != 200){
            return response;
        }
        System.out.println(response.getStatusCode() + " " + response.getBody());

        String DELETE_DECK = "Update cards set deck='no' where taken=?";
        String CREATE_DECK = "Update cards set deck='yes' where taken=? and id in (" + String.join(",",  "?, ".repeat(ids.size()).split(", ")) + ")";
        String SELECT_DECK = "SELECT * FROM cards where taken=? and deck='yes'";
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_DECK);
                PreparedStatement pstmt2 = con.prepareStatement(DELETE_DECK);
                PreparedStatement pstmt3 = con.prepareStatement(CREATE_DECK)
                ){
            pstmt2.setString(1, request.getUsername());
            pstmt2.executeUpdate();

            pstmt3.setString(1, request.getUsername());
            for (int i = 0; i < ids.size(); i++) {
                pstmt3.setString(i + 2, ids.get(i));
            }
            pstmt3.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e);
            return response.getError();
        }

        return response.getResponse("Deck configured", 200);
    }

    public Response checkOwner(Request request, List<String> ids){
      String GET_OWNER = "SELECT * FROM cards where taken=? and id in (" + String.join(",",  "?, ".repeat(ids.size()).split(", ")) + ")";
      Response response = new Response();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_OWNER);
        ){
            pstmt.setString(1, request.getUsername());
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setString(i + 2, ids.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                return response.getResponse("Cards not owned by you.", 400);
            }

        }
        catch (SQLException e) {
            System.out.println(e);
            return response.getError();
        }
        return response.getResponse("Ok", 200);
    }
}
