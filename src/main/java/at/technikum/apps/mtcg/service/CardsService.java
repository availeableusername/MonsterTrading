package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.CardsRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class CardsService {

    private final CardsRepository cardsRepository = new CardsRepository();

    public Response showCards(Request request){
        return this.cardsRepository.showCards(request);
    }
}
