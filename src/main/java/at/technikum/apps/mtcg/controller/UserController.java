package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.UUID;

import java.util.List;

public class UserController extends Controller {

    private final UserService userService = new UserService();

    public UserController(){

    }
    @Override
    public boolean supports(String route) {
        return route.startsWith("/users");
    }

    @Override
    public Response handle(Request request){
        //wenn noch ein / vorhanden ist (users/1) dann wird user mit id 1 gelöscht, geupdatet oder zurückgegeben
        //wenn kein / vorhanden ist /users, dann wird entweder ein neuer erstellt oder alle User werden zurückgegeben
        if (request.getRoute().equals("/users")) {
            switch (request.getMethod()) {
                case "GET":
                    return this.readAll(request);
                case "POST":
                    return this.create(request);
            }

            return this.status(HttpStatus.BAD_REQUEST);
        } else {
            String[] routeParts = request.getRoute().split("/");
            String username  = (routeParts[2]);
            switch (request.getMethod()) {
                case "GET":
                    return this.read(username, request);
                case "PUT":
                    return this.update(username, request);
                case "DELETE":
                    return null;
            }

            return this.status(HttpStatus.BAD_REQUEST);
        }
    }
    public Response readAll(Request request) {
        List<User> user = this.userService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String usersJson = null;

        try {
            usersJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(usersJson);
        //System.out.println(response.getBody());
        return response;
    }

    public Response create(Request request){
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        //System.out.println("In create");

        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();
        user.setId(userId);
        //###############################
        //System.out.println(request.getBody());
        //System.out.println(user.getId());
        //System.out.println(user.getUsername());
        //###############################
        user = this.userService.save(user);
        String taskJson = null;

        try {
            taskJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }
        if(!user.getExists()){
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(taskJson);
            //System.out.println("User does not exist");
        }
        else {
            try{
                String msg = objectMapper.writeValueAsString("User already exists");
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody(msg);
                //System.out.println("User exists");
                return response;
            } catch (JsonProcessingException var6) {
                throw new RuntimeException(var6);
            }

        }

        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(taskJson);
        return response;
    }
    public Response read(String username, Request request) {
        Response response = new Response();
        if(!Objects.equals(request.getUsername(), username)){
            return response.getResponse("You can't access this users information.", 400);
        }
        return this.userService.showUserData(request, response);
    }

    public Response update(String username, Request request) {
        Response response = new Response();
        if(!Objects.equals(request.getUsername(), username)){
            return response.getResponse("You can't update this users information.", 400);
        }
        return userService.updateUserData(request, response);
    }

    public Response delete(int id, Request request) {
        return null;
    }
}
