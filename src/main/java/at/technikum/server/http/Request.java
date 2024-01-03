package at.technikum.server.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {

    // GET, POST, PUT, DELETE
    private String method;

    // /, /home, /package
    private String route;

    private String host;

    // application/json, text/plain
    private String contentType;

    // 0, 17
    private int contentLength;

    // none, "{ "name": "foo" }"
    private String body;
    //token neu
    private String token;

    public String getMethod() {
        return method;
    }

    public void setMethod(HttpMethod httpMethod) {
        this.method = httpMethod.getMethod();
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    //token neu
    public String getToken(){
        return this.token;
    }
    public void setToken(String token){
        this.token = token;
    }

    public String getUsername(){
        Pattern pattern = Pattern.compile("Bearer\\s(\\w+)-mtcgToken");
        Matcher matcher = pattern.matcher(getToken());
        String username;

        if (matcher.find()) {
            username = matcher.group(1);
            return username;
        } else {
            System.out.println("Matcher Problem");
            return null;
        }
    }
}

