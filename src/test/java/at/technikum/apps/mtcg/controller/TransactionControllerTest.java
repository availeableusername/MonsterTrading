package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionControllerTest {

    private Request request = new Request();
    private final TransactionController transactionController = new TransactionController();
    @Test
    void acquirePackagesFail() {
        request.setToken("");
        Response response = transactionController.acquirePackages(request);

       assertEquals(401,response.getStatusCode());

    }
}