package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DeckRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class DeckService {
    private final DeckRepository deckRepository = new DeckRepository();

    public Response showDeck(Request request){
        return this.deckRepository.showDeck(request);
    }

    public Response configureDeck(Request request){
        return this.deckRepository.configureDeck(request);
    }
}
