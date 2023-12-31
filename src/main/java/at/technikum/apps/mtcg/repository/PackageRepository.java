package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.Request;
//import at.technikum.apps.mtcg.service.TransactionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PackageRepository {

    private final Database database = new Database();
    private final String SAVE_CARDS_SQL = "Insert into cards values(?,?,?,?,?,?)";
    //db bereits angelegt, rows id, name, damage, type, category, taken (which user owns this card)

    public void SavePackage(List<Card> cards){
        //logic
        int i = 0;
        Card card = new Card();
        while(i < cards.size()){
            try (
                    Connection con = database.getConnection();
                    PreparedStatement pstmt = con.prepareStatement(SAVE_CARDS_SQL)
            )
            {
                card = cards.get(i);
                pstmt.setString(1, card.getId());
                pstmt.setString(2, card.getName());
                pstmt.setInt(3, card.getDamage());
                pstmt.setString(4, card.getType());
                pstmt.setString(5, card.getCategory());
                pstmt.setString(6, "free");
                i++;
                pstmt.execute();

            } catch (SQLException e) {
                System.out.println(e);
                System.exit(400);
                // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
            }

        }

    }
    private final List<Package> packages = new ArrayList();

    public List<Package> findAll() {
        return this.packages;
    }

    public PackageRepository(){

    }


    public Optional<Package> find(int id) {
        return Optional.empty();
    }
    public Package save(Package cardPackage){
        //cardPackage.setId(this.packages.size() + 1);
        //this.packages.add(cardPackage);
        return cardPackage;
    }

    public Package delete(Package cardPackage) {return cardPackage;}
}
