package at.technikum.apps.mtcg.curl;

import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurlTest {

    MtcgApp mtcgApp = new MtcgApp();

    MtcgApp mock = mock();

    Database mockDatabase = mock(Database.class);

    UserRepository userRepository = mock(UserRepository.class);

    Response response = new Response();

    @Test
    void createUser() {
        Request request = new Request();
        request.setMethod(HttpMethod.POST);
        request.setRoute("/users"); //http://localhost:10001
        request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
        request.setBody("{\"username\":\"kienboec2\", \"password\":\"daniel\"}");

        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
    }
    @Test
    void getUserData(){
        Request request = new Request();
        request.setMethod(HttpMethod.GET);
        request.setRoute("/users/altenhof");
        request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
        request.setToken("Bearer altenhof-mtcgToken");
        request.setBody("{\"username\":\"kienboec2\", \"password\":\"daniel\"}");

        Response response = mtcgApp.handle(request);
        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
    }
    @Test
    void updateUserData(){
        Request request = new Request();
        request.setMethod(HttpMethod.PUT);
        request.setRoute("/users/kienboec2");
        request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
        request.setToken("Bearer kienboec2-mtcgToken");
        request.setBody("{\"Name\": \"Testbub\",  \"Bio\": \"me testin...\", \"Image\": \":(\"}");

        response.setStatus(HttpStatus.OK);
        when(mock.handle(request)).thenReturn(response);

        response = mock.handle(request);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        //verify(mock, never()).handle(request);

    }
}