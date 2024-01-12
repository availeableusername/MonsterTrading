package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckControllerTest {
    private Request request = new Request();
    private Response response = new Response();

    private final DeckController deckController = new DeckController();

    @Test
    void showDeckNoToken() {
        response = deckController.showDeck(request);
        assertEquals(401, response.getStatusCode());
    }

    @Test
    void configureDeckNoToken() {
        response = deckController.showDeck(request);
        assertEquals(401, response.getStatusCode());
    }
}