package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Session;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.apps.mtcg.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class SessionsController extends Controller {

    private final SessionService sessionService = new SessionService();

    public SessionsController(){

    }


    @Override
    public boolean supports(String route) {
        return route.equals("/sessions");
    }

    public Response handle(Request request) {
        //wenn noch ein / vorhanden ist (users/1) dann wird user mit id 1 gelöscht, geupdatet oder zurückgegeben
        //wenn kein / vorhanden ist /users, dann wird entweder ein neuer erstellt oder alle User werden zurückgegeben
        if (request.getRoute().equals("/sessions")) {
            switch (request.getMethod()) {
                case "GET":
                    System.out.println("in Get");
                    //get gibts eigentlich nicht für session
                case "POST":
                    return this.mapToTokenRequest(request);
            }

            return this.status(HttpStatus.BAD_REQUEST);
            //else wahrscheinlich unnötig
        }
        return this.status(HttpStatus.BAD_REQUEST);
        /*
        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("user controller");

        return response;
        */
    }

    public Response mapToTokenRequest(Request request){
        ObjectMapper objectMapper = new ObjectMapper();
        //user verwenden, warum Session extra schreiben?
        User user = new User(); //Konstruktur löschen in Session?

        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(this.sessionService.CheckUserLogin(user)){
            String token = user.getUsername() + "-mtcgToken";
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(token); //response.setBody(usersJson);
            System.out.println(response.getBody());
            return response;
        }
        else{
            String msg = "Username or Password wrong";
            Response response = new Response();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(msg); //response.setBody(usersJson);
            System.out.println(response.getBody());
            return response;
            //response logic
        }

    }

}
