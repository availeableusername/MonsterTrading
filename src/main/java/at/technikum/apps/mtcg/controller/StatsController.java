package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.StatsService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class StatsController extends Controller{

    private final StatsService statsService = new StatsService();
    @Override
    public boolean supports(String route) {
        return route.equals("/stats") || route.equals("/scoreboard");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/stats")) {
            if (request.getMethod().equals("GET")) {
                return this.showUserStats(request);
            }

        }
        if (request.getRoute().equals("/scoreboard")) {
            if (request.getMethod().equals("GET")) {
                return this.showAllStats(request);
            }
        }
        return this.status(HttpStatus.BAD_REQUEST);
    }

    public Response showUserStats(Request request){
        Response response = new Response();
        return statsService.showUserStats(request, response);
    }

    public Response showAllStats(Request request){
        Response response = new Response();
        return statsService.showAllStats(request, response);
    }

}
