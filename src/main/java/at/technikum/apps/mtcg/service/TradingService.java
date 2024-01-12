package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.server.http.Response;

import java.util.HashMap;

public class TradingService {
    private final TradingRepository tradingRepository = new TradingRepository();
    private Response response = new Response();

    public Response createTrade(HashMap<String, Object> hashMap){
        if(!tradingRepository.checkOwner(hashMap.get("CardToTrade").toString(), hashMap.get("Username").toString())){
            return response.getResponse("Card not owned by you.", 401);
        }
        return tradingRepository.createTrade(hashMap);
    }
    public Response showTrades(){
        return tradingRepository.showTrades();
    }
    public Response deleteTrade(String id, String username){
        return tradingRepository.deleteTrade(id, username);
    }
    public Response completeTrade(String id, String tradeCard, String username){
        if(!tradingRepository.checkOwner(tradeCard, username)){
            return response.getResponse("Card not owned by you.", 401);
        }
        if(tradingRepository.checkOwnerTradeDeal(id, username)){
            return response.getResponse("Can't trade with yourself.", 400);
        }
        if(!tradingRepository.checkValidityOfCard(id, tradeCard)){
            return response.getResponse("Damage to low or wrong card type.", 400);
        }
        return tradingRepository.completeTrade(id, tradeCard, username);
    }
}
