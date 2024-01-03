package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.StatsRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class StatsService {
    private final StatsRepository statsRepository = new StatsRepository();

    public Response showUserStats(Request request, Response response){
        return statsRepository.showUserStats(request, response);
    }

    public Response showAllStats(Request request, Response response){
        return statsRepository.showAllStats(request, response);
    }
}
