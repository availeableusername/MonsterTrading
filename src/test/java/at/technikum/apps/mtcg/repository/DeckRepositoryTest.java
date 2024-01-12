package at.technikum.apps.mtcg.repository;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckRepositoryTest {
    private final DeckRepository deckRepository = new DeckRepository();
    private Request request = new Request();
    @Test
    void showDeckNoDeckConfigured() {
        request.setToken("Bearer admin-mtcgToken");
        Response response = deckRepository.showDeck(request);
        assertEquals(400 ,response.getStatusCode());
    }

    @Test
    void configureDeckLessThan4Cards() {
        request.setBody("[\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"d60e23cf-2238-4d49-844f-c7589ee5342e\"]");
        Response response = deckRepository.configureDeck(request);
        assertEquals(400, response.getStatusCode());
    }
    @Test
    void checkOwnerWrongOwner(){
        request.setToken("Bearer altenhof-mtcgToken");
        List<String> ids = new ArrayList<>();
        //["aa9999a0-734c-49c6-8f4a-651864b14e62", "d6e9c720-9b5a-40c7-a6b2-bc34752e3463", "d60e23cf-2238-4d49-844f-c7589ee5342e", "02a9c76e-b17d-427f-9240-2dd49b0d3bfd"]
        ids.add("aa9999a0-734c-49c6-8f4a-651864b14e62");
        ids.add("d6e9c720-9b5a-40c7-a6b2-bc34752e3463");
        ids.add("d60e23cf-2238-4d49-844f-c7589ee5342e");
        ids.add("02a9c76e-b17d-427f-9240-2dd49b0d3bfd");

        Response response = deckRepository.checkOwner(request, ids);
    }
}
