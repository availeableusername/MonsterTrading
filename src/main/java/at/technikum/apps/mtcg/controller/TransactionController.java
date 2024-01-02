package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TransactionController extends Controller{

    private final TransactionService transactionService = new TransactionService();
    @Override
    public boolean supports(String route) {
        return route.equals("/transactions/packages");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/transactions/packages")) {
            switch (request.getMethod()) {
                default:
                    return this.status(HttpStatus.BAD_REQUEST);
                case "POST":
                    return this.acquirePackages(request);
            }
            

            
        } else if (request.getRoute().equals("/transactions/trade")) {
            //trade logic

        }
        return this.status(HttpStatus.BAD_REQUEST);
    }
    public Response acquirePackages(Request request){
        Response response = new Response();
        String token = request.getToken();

        if(token.isEmpty()){
            String msg = "You need to be logged in for that action";
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(msg);
            return response;
        }

        response = transactionService.acquireCards(request);

        return response;
    }
}
