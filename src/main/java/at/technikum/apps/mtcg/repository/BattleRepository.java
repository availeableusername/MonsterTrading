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
        /*
        System.out.println(deckList.get(0));
        System.out.println(deckList.get(1));
        System.out.println(deckList.get(2));
        System.out.println(deckList.get(3));
         */

        return deckList;
    }
    public Response battleLogic(List<HashMap<String, String>> deck1, List<HashMap<String, String>> deck2, HashMap<String, String> map, Response response) {
        String path = "C:\\Users\\Anwender\\OneDrive\\Informatik_Studium\\3. Semester\\SWEN1\\MonsterTrading\\battles\\" + map.get("id") + ".txt";
        int i = 0;

        while(i < 10) {
            Random random = new Random();
            int c1 = random.nextInt(deck1.size());
            int c2 = random.nextInt(deck2.size());
            //System.out.println(deck1.get(0));
            //System.out.println(deck1.get(1));
            //System.out.println(deck1.get(2));
            //System.out.println(deck1.get(3));
            //System.out.println(c2);
            HashMap<String, String> card1 = deck1.get(c1);
            HashMap<String, String> card2 = deck2.get(c2);

            boolean monsterFight = card1.get("category").equals("Monster") && card2.get("category").equals("Monster");
            float damage1 = Float.parseFloat(card1.get("damage"));
            float damage2 = Float.parseFloat(card2.get("damage"));
            String type1 = card1.get("type");
            String type2 = card2.get("type");
            String name1 = card1.get("name");
            String name2 = card2.get("name");

            StringBuilder history = new StringBuilder();

            history.append(map.get("player1")).append(": ").append(name1).append(" (").append(card1.get("damage")).append(") vs ");
            history.append(map.get("player2")).append(": ").append(name2).append(" (").append(card2.get("damage")).append(") => ");
            //history += map.get("player2") + ": " + card2.get("name") + " (" + card2.get("damage") +") => ";

            if (monsterFight) {
                history.append(battleResult(damage1, damage2, name1, name2));

            } else {
                if (type1.equals(type2)) {
                    history.append(battleResult(damage1, damage2, name1, name2));
                    //history += battleResult(damage1, damage2, name1, name2);

                    //in Funktion auslagern?
                } else if (type1.equals("Fire") && type2.equals("Water")) {
                    damage1 = damage1 / 2;
                    damage2 = damage2 * 2;
                    history.append(damage1).append(" VS ").append(damage2);
                    history.append(battleResult(damage1, damage2, name1, name2));

                } else if(type1.equals("Fire") && type2.equals("Regular")){
                    damage1 = damage1 * 2;
                    damage2 = damage2 / 2;
                    history.append(damage1).append(" VS ").append(damage2);
                    history.append(battleResult(damage1, damage2, name1, name2));
                    
                } else if (type1.equals("Water") && type2.equals("Regular")) {
                    damage1 = damage1 / 2;
                    damage2 = damage2 * 2;
                    history.append(damage1).append(" VS ").append(damage2);
                    history.append(battleResult(damage1, damage2, name1, name2));
                    
                } else if (type1.equals("Water") && type2.equals("Fire")) {
                    damage1 = damage1 * 2;
                    damage2 = damage2 / 2;
                    history.append(damage1).append(" VS ").append(damage2);
                    history.append(battleResult(damage1, damage2, name1, name2));
                    
                } else if (type1.equals("Regular") && type2.equals("Fire")) {
                    damage1 = damage1 / 2;
                    damage2 = damage2 * 2;
                    history.append(damage1).append(" VS ").append(damage2);
                    history.append(battleResult(damage1, damage2, name1, name2));
                    
                } else if (type1.equals("Regular") && type2.equals("Water")) {
                    damage1 = damage1 * 2;
                    damage2 = damage2 / 2;
                    history.append(damage1).append(" VS ").append(damage2);
                    history.append(battleResult(damage1, damage2, name1, name2));

                }
                history.append(" \n");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
                    // Text in die Datei schreiben
                    writer.write(history.toString());

                    //System.out.println("Text wurde erfolgreich in die Datei geschrieben.");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            i++;
        }
        //stats updaten hinzufügen?
        resultsToDB(map);

        String winner;
        if (this.p1 > this.p2){
            winner = "The winner is: " + map.get("player1");
        } else if (this.p1 < this.p2) {
            winner = "The winner is: " + map.get("player2");
        } else{
            winner = "It's a Draw";
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(winner);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            battleReport = result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(battleReportString);
        return response.getResponse(battleReport, 200);
    }

    public String battleResult(float damage1, float damage2, String name1, String name2){
        String history;
        if (damage1 > damage2) {
            this.p1++;
            return history = " " + name1 + " defeats " + name2;
            //cards change deck logic
        } else if (damage1 < damage2) {
            this.p2++;
            return history = " " + name2 + " defeats " + name1;
        } else {
            return history = "Draw!";
        }
    }

    public void resultsToDB(HashMap<String, String> map){
        String winner;
        if (this.p1 > this.p2){
            winner = map.get("player1");
        } else if (this.p1 < this.p2) {
            winner = map.get("player2");
        } else{
            winner = "Draw";
        }
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(WINNER);
                ){
            pstmt.setInt(2, Integer.parseInt(map.get("id")));
            pstmt.setString(1, winner);
            pstmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Response waitingForBattle(Request request, Response response){
        //checks if battle is finished, returns battle report
        return response.getError();
    }
}
