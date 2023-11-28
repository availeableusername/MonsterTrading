package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.HttpContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public abstract class Controller {

    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    protected Response status(HttpStatus httpStatus) {
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody("{ \"error\": \""+ httpStatus.getMessage() + "\"}");

        return response;

        /*

        ObjectMapper objectMapper = new ObjectMapper();
        Task task = null;
        try {
            task = objectMapper.readValue(request.getBody(), Task.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        task = toObject(request.getBody(), Task.class);

        task = taskService.save(task);

        String taskJson = null;
        try {
            taskJson = objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        // THOUGHT: better status 201 Created
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(taskJson);

        return response;
        */
    }

    //zuerst die methode für jeden Controller erstellen, dann abstrakte klasse probieren
    protected String toObject(Request request, Class<?> obj){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            obj = objectMapper.readValue(request.getBody(), obj.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // task = toObject(request.getBody(), Task.class);

        obj = Service.save(obj);

        String classJson = null;
        try {
            classJson = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return classJson;

    }



}
