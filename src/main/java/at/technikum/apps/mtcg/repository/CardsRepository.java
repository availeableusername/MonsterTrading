package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardsRepository {
    private final String GET_CARDS = "SELECT * FROM cards where taken=?";
    private final Database database = new Database();

    public Response showCards(Request request){
        Response response = new Response();
        String username = request.getUsername(request);
        List<Card> cards = new ArrayList<>();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_CARDS);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){ //
                System.out.println("No cards acquired");
                return response.getResponse("No cards acquired", 200);
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
            System.out.println("showCards");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String cardsJson = null;

        try {
            cardsJson = objectMapper.writeValueAsString(cards);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }
        return response.getResponse(cardsJson, 200);
    }
}
