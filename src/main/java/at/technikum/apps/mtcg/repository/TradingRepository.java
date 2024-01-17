package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.server.http.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TradingRepository {
    private final String CREATE_TRADE = "Insert into trades(id, cardoffered, username, category, damage) values(?,?,?,?,?)";
    private final String SHOW_TRADES = "Select * from trades where cardtraded is Null";
    private final String DELETE_TRADES = "Delete from trades where id=? and username=?";
    private final String CHECK_OWNER = "Select * from cards where id=? and taken=?";
    private final String CHECK_OWNER_TRADE_DEAL = "Select * from trades where id=? and username=? and cardtraded is Null";
    private final String CHECK_VALIDITY = "Select * from trades where id=? and cardtraded is Null";
    private final String GET_CARD = "Select * from cards where id=?";
    private final String COMPLETE_TRADE = "Update trades  set cardtraded=? where id=?";
    private final String CHANGE_OWNER = "Update cards set taken=? where id=?";
    private final String GET_USERNAME = "Select * from trades where id=?";

   private final Database database = new Database();

   private Response response = new Response();

    public Response createTrade(HashMap<String, Object> hashMap){
        String damage = hashMap.get("MinimumDamage").toString();
        try {
            Connection con = database.getConnection();
            PreparedStatement pstmt = con.prepareStatement(CREATE_TRADE);
            pstmt.setString(1, hashMap.get("Id").toString());
            pstmt.setString(2, hashMap.get("CardToTrade").toString());
            pstmt.setString(3, hashMap.get("Username").toString());
            pstmt.setString(4, hashMap.get("Type").toString());
            pstmt.setInt(5, Integer.parseInt(damage));
                pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return response.getError();
        }
        return response.getResponse("Trade deal created.", 200);
    }

    public Response showTrades(){
        String trades = null;
        try (
            Connection con = database.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SHOW_TRADES);
            ResultSet rs = pstmt.executeQuery();
        ){
            if(!rs.next()){
                return response.getResponse("No trade deals yet.", 200);
            }
            trades = "";
            do {
                HashMap<String, String> trade = new LinkedHashMap<>();
                trade.put("id", rs.getString("id"));
                trade.put("CardToTrade", rs.getString("cardoffered"));
                trade.put("Username",  rs.getString("username"));
                trade.put("Type", rs.getString("category"));
                trade.put("damage", rs.getString("damage"));
                trades += trade.toString();
                trades += "\n";
            } while(rs.next());
        } catch (SQLException e){
            e.printStackTrace();
            return response.getError();
        }
        return response.getResponse(trades, 200);

    }
    public Response deleteTrade(String id, String username){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_TRADES);
        ){
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return response.getError();
        }
        return response.getResponse("Trade deleted.", 200);
    }

    public Response completeTrade(String tradeId, String cardId, String username){
        String username2 = null;
        String cardOffered = null;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_USERNAME);
                PreparedStatement pstmt2 = con.prepareStatement(COMPLETE_TRADE);
                PreparedStatement pstmt3 = con.prepareStatement(CHANGE_OWNER);

        ){
            pstmt.setString(1, tradeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                username2 = rs.getString("username");
                cardOffered = rs.getString("cardoffered");
            }
            pstmt2.setString(1, cardId);
            pstmt2.setString(2, tradeId);
            pstmt2.executeUpdate();

            pstmt3.setString(2, cardId);
            pstmt3.setString(1, username2);
            pstmt3.executeUpdate();

            pstmt3.setString(2, cardOffered);
            pstmt3.setString(1, username);
            pstmt3.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            //return response.getError();
        }

        return response.getResponse("Trade completed.", 200);
    }
    public boolean checkValidityOfCard(String tradeId, String cardId){
        //checks if cards has enough damage and is from the correct type(category) for trade
        String categoryTrade = null;
        int damageTrade = 0;
        String categoryCard = null;
        int damageCard = 0;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CHECK_VALIDITY);
                PreparedStatement pstmt2 = con.prepareStatement(GET_CARD);
        ){
            pstmt2.setString(1, cardId);
            ResultSet rs = pstmt2.executeQuery();
            while (rs.next()){
                categoryCard = rs.getString("category");
                damageCard = rs.getInt("damage");
            }
            pstmt.setString(1, tradeId);
            rs = pstmt.executeQuery();
            while (rs.next()){
                categoryTrade = rs.getString("category");
                damageTrade = rs.getInt("damage");
            }
            return categoryTrade.equalsIgnoreCase(categoryCard) && damageCard >= damageTrade;
        } catch (SQLException e){
            e.printStackTrace();
            //return response.getError();
        }
        return false;
    }

    public boolean checkOwner(String id, String username){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CHECK_OWNER);
        ){
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            //return response.getError();
        }
        return false;
    }
    public boolean checkOwnerTradeDeal(String id, String username){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CHECK_OWNER_TRADE_DEAL);
        ){
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
            //return response.getError();
        }
        return false;

    }

}
