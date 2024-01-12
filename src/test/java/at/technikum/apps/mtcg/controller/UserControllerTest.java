package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.mockito.Spy;


public class UserControllerTest {
    private final UserController userController = new UserController();

    private Request request = new Request();
    private Response response = new Response();

    @Test
    public void wrongToken(){
        request.setToken("Bearer altenhof-mtcgToken");
        response = userController.update("kienboec", request);
        assertEquals(400, response.getStatusCode());
    }
    @Test
    public void noToken(){
        request.setToken("");
        response = userController.update("kienboec", request);
        assertEquals(400, response.getStatusCode());
    }

}