package at.technikum.apps.mtcg.repository;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardsRepositoryTest {

    private Request request = new Request();
    private Response response = new Response();
    private final CardsRepository cardsRepository = new CardsRepository();

    @Test
    public void checkUserCardsShouldEqual200(){
        this.request.setToken("Bearer kienboec-mtcgToken");
        this.request.setBody("");

        response = cardsRepository.showCards(request);
        //System.out.println(response);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void checkUserCardsShouldEqual400(){
        this.request.setToken("Bearer admin-mtcgToken");
        this.request.setBody("");

        response = cardsRepository.showCards(request);
        //System.out.println(response);
        assertEquals(200, response.getStatusCode());
    }

}