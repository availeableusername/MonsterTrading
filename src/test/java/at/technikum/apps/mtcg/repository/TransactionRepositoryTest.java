package at.technikum.apps.mtcg.repository;

import at.technikum.server.http.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private final TransactionRepository transactionRepository = new TransactionRepository();
    private  Request request = new Request();
    @Test
    void checkGoldGoldLeft() {
        assertTrue(transactionRepository.checkGold(request, "admin"));
    }
    @Test
    void checkGoldNoGold() {
        assertFalse(transactionRepository.checkGold(request, "kienboec"));
    }
}