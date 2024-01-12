package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.json.JSONObject;

import java.util.HashMap;

public class TradingController extends Controller{

    private final TradingService tradingService = new TradingService();

    private Response response = new Response();
    @Override
    public boolean supports(String route) {
        return route.startsWith("/tradings");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/tradings")) {
            switch (request.getMethod()) {
                case "GET":
                    return this.checkDeals(request);
                case "POST":
                    return this.createTrade(request);
            }
            return this.status(HttpStatus.BAD_REQUEST);
        } else {
            String[] routeParts = request.getRoute().split("/");
            String tradeId  = (routeParts[2]);
            switch (request.getMethod()) {
                case "POST":
                    return this.completeTrade(tradeId, request);
                case "DELETE":
                    return this.deleteTrade(tradeId, request);
            }

            return this.status(HttpStatus.BAD_REQUEST);
        }
    }

    private Response createTrade(Request request) {
        if(request.getToken().isEmpty()){
            return response.getResponse("Please log in to create trade deals.", 401);
        }
        JSONObject jsonObject = new JSONObject(request.getBody());
        HashMap<String, Object> hashMap = new HashMap<>(jsonObject.toMap());
        hashMap.put("Username", request.getUsername());
        System.out.println(hashMap);
        return tradingService.createTrade(hashMap);
    }

    private Response checkDeals(Request request) {
        return this.tradingService.showTrades();
    }

    private Response deleteTrade(String tradeId, Request request) {
        String username = request.getUsername();
        return this.tradingService.deleteTrade(tradeId, username);
    }

    private Response completeTrade(String tradeId, Request request) {
        String tradeCard = request.getBody();
        tradeCard = tradeCard.replace("\"", "");
        String username = request.getUsername();
        return this.tradingService.completeTrade(tradeId, tradeCard, username);
    }


}
