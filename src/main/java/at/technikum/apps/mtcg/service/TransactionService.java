package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.TransactionRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TransactionService {

    private final TransactionRepository transactionRepository = new TransactionRepository();
    public Response acquireCards(Request request){
        //Response response = new Response();
        return transactionRepository.acquireCards(request);
    }

}
