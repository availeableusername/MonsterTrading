package at.technikum.apps.mtcg.repository;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsRepositoryTest {
    private Response response = new Response();
    private Request request = new Request();
    private final StatsRepository statsRepository = new StatsRepository();
    @Test
    void showUserStats() {
        request.setToken("Bearer kienboec-mtcgToken");
        response = statsRepository.showUserStats(request, response);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void showAllStats() {
        response = statsRepository.showAllStats(request, response);
        assertEquals(200, response.getStatusCode());
    }
}