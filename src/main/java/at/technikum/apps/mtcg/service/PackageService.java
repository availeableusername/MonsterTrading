package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.List;

public class PackageService {
    private final PackageRepository packageRepository = new PackageRepository();

    private final String allowedToken = "Bearer admin-mtcgToken";

    public Response PackagesToDB(List<Card> cards, Request request){
        Response response = new Response();
        //in controller verlegen
        if(request.getToken().equals(allowedToken)){
            packageRepository.SavePackage(cards);
            String msg = "Package successfully created";
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(msg);

        }else {
            String msg = "Only admin is allowed to create packages";
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(msg);
        }


        return response;
    }
}
