package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class PackageController extends Controller{

    private final PackageService packageService = new PackageService();
    @Override
    public boolean supports(String route) {
        return route.equals("/packages");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/packages")) {
            switch (request.getMethod()) {
                case "POST":
                    return this.create(request);
            }

            return this.status(HttpStatus.BAD_REQUEST);
        }
        return this.status(HttpStatus.BAD_REQUEST);
    }
    public Response create(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Card card = null;
        String jsonString = request.getBody();

        List<Card> cards = null;
        try {
            cards = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Card.class));


            //for (Card obj : cards) {
            //System.out.println(obj);
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Card obj : cards){
            if(obj.getName().contains("Water")){
                obj.setType("Water");
            } else if (obj.getName().contains("Fire")) {
                obj.setType("Fire");
            } else {
                obj.setType("Regular");
            }

            if(obj.getName().contains("Spell")){
                obj.setCategory("Spell");
            } else{
                obj.setCategory("Monster");
            }
        }
        //for (Card obj : cards) {
        //    System.out.println(obj);
        //}


        // System.out.println(request.getBody());
        Response response = new Response();
        response = this.packageService.PackagesToDB(cards, request);


        return response;
    }
}
