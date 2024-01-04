package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.BattleRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class BattleService {
    private final BattleRepository battleRepository = new BattleRepository();

    public Response battle(Request request, Response response){
        //checks if battle finished, if not calls func waitingForBattle
        response = battleRepository.battle(request, response);

        if(response.getBody().equals("Waiting")){
            //System.out.println("Waiting");
            battleRepository.waitingForBattle(request, response);
        }
        return response;
    }
}
