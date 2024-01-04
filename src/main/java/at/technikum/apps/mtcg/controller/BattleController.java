package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.BattleRepository;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class BattleController extends Controller{

    private final BattleService battleService = new BattleService();
    @Override
    public boolean supports(String route) {
        return route.equals("/battles");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/battles")) {
            switch (request.getMethod()) {
                case "POST":
                    return this.battle(request);
                default:
                    return this.status(HttpStatus.BAD_REQUEST);
            }
        }
        return this.status(HttpStatus.BAD_REQUEST);
    }

    public Response battle(Request request){
        Response response = new Response();
        if(request.getToken() == null){
            return response.getResponse("You need to be logged in.", 400);
        }
        return battleService.battle(request, response);
    }
}
