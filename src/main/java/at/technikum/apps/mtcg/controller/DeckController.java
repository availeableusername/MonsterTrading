package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.CardsService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class DeckController extends Controller {

    private final CardsService cardsService = new CardsService();
    @Override
    public boolean supports(String route) {
        return route.equals("/deck");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/deck")) {
            return switch (request.getMethod()) {
                case "GET" -> this.showDeck(request);
                case "PUT" -> null;
                default -> this.status(HttpStatus.BAD_REQUEST);
            };
        }
        return this.status(HttpStatus.BAD_REQUEST);
    }

    public Response showDeck(Request request){
        Response response = new Response();
        if(request.getToken() == null){
            String msg = "You need to be logged in.";
            return response.getResponse(msg, 401);
        }
        return this.cardsService.showCards(request);
    }
}
