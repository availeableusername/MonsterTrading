package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BattleRepository {
    private final String GET_BATTLE = "SELECT * FROM battles where id = (Select max(id) from battles) and winner is null";
    private final String WAIT_FOR_BATTLE = "Insert into battles(player1) values(?)"; //vielleicht muss das noch geändert werden
    private final String GET_DECK = "select * from cards where taken=? and deck='yes'";
    private final String PLAYER2 = "UPDATE battles set player2=? where id=?";
    private final String WINNER = "UPDATE battles set winner=? where id=?";
    private final String STATS_WINNER = "UPDATE stats set win=win +1, games=games +1, elo=elo+3 where username=?";
    private final String STATS_LOSER = "UPDATE stats set defeat=defeat +1, games=games + 1, elo=elo-5 where username=?";
    private final String STATS_DRAW = "UPDATE stats set draw=draw+1, games=games+1 where username in (?, ?)";
    private final String CHECK_BATTLE = "SELECT winner FROM battles where id=? and winner is not null";
    private final String GET_ID = "SELECT id FROM battles where id = (Select max(id) from battles) and winner is null and player1=?";
    private final Database database = new Database();

    private int p1 = 0;

    private int p2 = 0;

    public Response battle(Request request, Response response){
        //checks if battle in DB, if not inserts player1 into DB, if battle is found calls func battleDecks and insert player2 into DB
        HashMap<String, String> map = new HashMap<>();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_BATTLE);
                PreparedStatement pstmt2 = con.prepareStatement(WAIT_FOR_BATTLE);
                PreparedStatement pstmt3 = con.prepareStatement(PLAYER2);
                ResultSet rs = pstmt.executeQuery();
                ){
            if(!rs.next()){
                pstmt2.setString(1, request.getUsername());
                pstmt2.executeUpdate();
                return response.getResponse("Waiting", 200);
            }
            do{
               map.put("id", String.valueOf(rs.getInt("id")));
               map.put("player1", rs.getString("player1"));
               map.put("player2", request.getUsername());
            }while(rs.next());
            System.out.println(map);

            pstmt3.setString(1, map.get("player2"));
            pstmt3.setInt(2, Integer.parseInt(map.get("id")));
            pstmt3.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return response.getError();
        }

        response = battleDecks(map, response);

        return response;
    }

    public Response battleDecks(HashMap<String, String> map, Response response){
        //when battle is found, gets decks from both players and starts battle with func battleLogic
        List<HashMap<String, String>> deck1 = new ArrayList<>();
        List<HashMap<String, String>> deck2 = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_DECK);
                PreparedStatement pstmt2 = con.prepareStatement(GET_DECK);

        ){
            pstmt.setString(1, map.get("player1"));
            ResultSet rs = pstmt.executeQuery();
            deck1 = getDeck(rs);

            pstmt2.setString(1, map.get("player2"));
            rs = pstmt2.executeQuery();
            deck2 = getDeck(rs);

        } catch (SQLException e){
            System.out.println(e);
            return response.getError();
        }
        return battleLogic(deck1, deck2, map, response);
    }
    public List<HashMap<String, String>> getDeck(ResultSet rs){
        //gets decks of players
        List<HashMap<String, String>> deckList = new ArrayList<>();
        try {
            while (rs.next()) {
                HashMap<String, String> deck = new HashMap<>();
                deck.put("id", rs.getString("id"));
                deck.put("name", rs.getString("name"));
                deck.put("damage", String.valueOf(rs.getInt("damage")));
                deck.put("type", rs.getString("type"));
                deck.put("category", rs.getString("category"));
                deckList.add(deck);
                //System.out.println(deck);
                //System.out.println(deckList);
            }

        }
        catch (SQLException e){
            throw new RuntimeException(e);
            //System.out.println(e);
            //return deckList;
        }

        return deckList;
    }
    public Response battleLogic(List<HashMap<String, String>> deck1, List<HashMap<String, String>> deck2, HashMap<String, String> map, Response response) {
        String path = "C:\\Users\\Anwender\\OneDrive\\Informatik_Studium\\3. Semester\\SWEN1\\MonsterTrading\\battles\\" + map.get("id") + ".txt";
        int i = 0;

        while(i < 11) {
            StringBuilder history = new StringBuilder();
            Random random = new Random();
            String user1 = map.get("player1");
            String user2 = map.get("player2");
            String winner = null;

            if(deck1.isEmpty()){
                writer("Match ended: " + user1 + " has no cards left.", path);
                System.out.println("deck1 empty");
                break;
            } else if (deck2.isEmpty()) {
                writer("Match ended: " + user2 + " has no cards left.", path);
                System.out.println("deck2 empty");
                break;
            }

            //System.out.println("kienboec deck: " + deck1.size());
            //System.out.println("altenhof deck: " + deck2.size());

            int c1 = random.nextInt(deck1.size());
            int c2 = random.nextInt(deck2.size());
            HashMap<String, String> card1 = deck1.get(c1);
            HashMap<String, String> card2 = deck2.get(c2);

            boolean monsterFight = card1.get("category").equals("Monster") && card2.get("category").equals("Monster");
            float damage1 = Float.parseFloat(card1.get("damage"));
            float damage2 = Float.parseFloat(card2.get("damage"));
            String type1 = card1.get("type");
            String type2 = card2.get("type");
            String name1 = card1.get("name");
            String name2 = card2.get("name");

            history.append(map.get("player1")).append(": ").append(name1).append(" (").append(card1.get("damage")).append(") vs ");
            history.append(map.get("player2")).append(": ").append(name2).append(" (").append(card2.get("damage")).append(") => ");

            if (!monsterFight) {
                if (type1.equals("Fire") && type2.equals("Water")) {
                    damage1 = damage1 / 2;
                    damage2 = damage2 * 2;

                } else if(type1.equals("Fire") && type2.equals("Regular")){
                    damage1 = damage1 * 2;
                    damage2 = damage2 / 2;
                    
                } else if (type1.equals("Water") && type2.equals("Regular")) {
                    damage1 = damage1 / 2;
                    damage2 = damage2 * 2;
                    
                } else if (type1.equals("Water") && type2.equals("Fire")) {
                    damage1 = damage1 * 2;
                    damage2 = damage2 / 2;
                    
                } else if (type1.equals("Regular") && type2.equals("Fire")) {
                    damage1 = damage1 / 2;
                    damage2 = damage2 * 2;
                    
                } else if (type1.equals("Regular") && type2.equals("Water")) {
                    damage1 = damage1 * 2;
                    damage2 = damage2 / 2;

                }
                history.append(damage1).append(" VS ").append(damage2).append("->");
                winner = (battleResult(damage1, damage2, map.get("player1"), map.get("player2")));

                if(winner.equals(map.get("player1"))){
                    history.append(name1).append(" defeats ").append(name2);
                    deck1.add(card2);
                    deck2.remove(c2);
                } else if (winner.equals(map.get("player2"))) {
                    history.append(name2).append(" defeats ").append(name1);
                    deck2.add(card1);
                    deck1.remove(c1);
                } else {
                    history.append("Draw!");
                }

                history.append(" \n");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
                    writer.write(history.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }

        String winner;
        System.out.println("kienboec points: " + p1 + "\naltenhof points: " + p2);
        if (this.p1 > this.p2){
            winner = "The winner is: " + map.get("player1");
        } else if (this.p1 < this.p2) {
            winner = "The winner is: " + map.get("player2");
        } else{
            winner = "It's a Draw!";
        }
        winner += " Rounds played: " + --i;
        writer(winner, path);
        /*try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(winner);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        resultsToDB(map);
        String history = history(path);

        return response.getResponse(history, 200);
    }

    public void writer(String history, String path){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(history);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String history(String path){
        String battleReport = null;
        try {
            File doc = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(doc));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
            reader.close();
            return battleReport = result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String battleResult(float damage1, float damage2, String user1, String user2){
        String winner;
        if (damage1 > damage2) {
            this.p1++;
            return winner = user1;
            //cards change deck logic
        } else if (damage1 < damage2) {
            this.p2++;
            return winner = user2;
        } else {
            return winner = "Draw";
        }
    }

    public void resultsToDB(HashMap<String, String> map){
        String winner;
        int result;
        if (this.p1 > this.p2){
            winner = map.get("player1");
            result = 1;
        } else if (this.p1 < this.p2) {
            winner = map.get("player2");
            result = 2;
        } else{
            winner = "Draw";
            result = 3;
        }
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(WINNER);
                PreparedStatement pstmt4 = con.prepareStatement(STATS_DRAW);
                PreparedStatement pstmt2 = con.prepareStatement(STATS_WINNER);
                PreparedStatement pstmt3 = con.prepareStatement(STATS_LOSER);
                ){
            pstmt.setInt(2, Integer.parseInt(map.get("id")));
            pstmt.setString(1, winner);
            pstmt.executeUpdate();

            if(result == 1){
                pstmt2.setString(1, map.get("player1"));
                pstmt3.setString(1, map.get("player2"));
                pstmt2.executeUpdate();
                pstmt3.executeUpdate();
            } else if (result == 2) {
                pstmt3.setString(1, map.get("player1"));
                pstmt2.setString(1, map.get("player2"));
                pstmt2.executeUpdate();
                pstmt3.executeUpdate();
            } else{
                pstmt4.setString(1, map.get("player1"));
                pstmt4.setString(2, map.get("player2"));
                pstmt4.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Response waitingForBattle(Request request, Response response){
        int id = 0;
        while(true){
            try(
                    Connection con = database.getConnection();
                    PreparedStatement pstmt = con.prepareStatement(GET_ID);
                    PreparedStatement pstmt2 = con.prepareStatement(CHECK_BATTLE);
                    ){
                pstmt.setString(1, request.getUsername());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    id = rs.getInt("id");
                }
                String path = "C:\\Users\\Anwender\\OneDrive\\Informatik_Studium\\3. Semester\\SWEN1\\MonsterTrading\\battles\\" + id + ".txt";
                pstmt2.setInt(1, id);
                rs = pstmt2.executeQuery();
                while (rs.next()){
                    return response.getResponse(history(path),200);
                }

            }catch (SQLException e){
                e.printStackTrace();
                return response.getError();
            }
        }
        //return response.getError();
    }
}
