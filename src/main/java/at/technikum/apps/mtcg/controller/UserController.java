package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

import java.util.List;

public class UserController extends Controller {

    private final UserService userService = new UserService();

    public UserController(){

    }
    @Override
    public boolean supports(String route) {
        return route.equals("/users");
    }

    @Override
    public Response handle(Request request) {
        //wenn noch ein / vorhanden ist (users/1) dann wird user mit id 1 gelöscht, geupdatet oder zurückgegeben
        //wenn kein / vorhanden ist /users, dann wird entweder ein neuer erstellt oder alle User werden zurückgegeben
        if (request.getRoute().equals("/users")) {
            switch (request.getMethod()) {
                case "GET":
                    System.out.println("in Get");
                    return this.readAll(request);
                case "POST":
                    return this.create(request);
            }

            return this.status(HttpStatus.BAD_REQUEST);
        } else {
            String[] routeParts = request.getRoute().split("/");
            int taskId = Integer.parseInt(routeParts[2]);
            switch (request.getMethod()) {
                case "GET":
                    return this.read(taskId, request);
                case "PUT":
                    return this.update(taskId, request);
                case "DELETE":
                    return this.delete(taskId, request);
            }

            return this.status(HttpStatus.BAD_REQUEST);
        }
        /*
        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("user controller");

        return response;
        */
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
        System.out.println(response.getBody());
        return response;
    }

    public Response create(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;

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
        System.out.println(user.getId());
        System.out.println(user.getName());
        //###############################
        user = this.userService.save(user);
        String taskJson = null;

        try {
            taskJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException var6) {
            throw new RuntimeException(var6);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(taskJson);
        return response;
    }
    public Response read(int id, Request request) {
        return null;
    }

    public Response update(int id, Request request) {
        return null;
    }

    public Response delete(int id, Request request) {
        return null;
    }
}
