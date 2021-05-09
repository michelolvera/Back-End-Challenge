package dev.michel.accountservice.client;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.IssuerResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MovementClientCircuitBreaker {

    private final MovementClient movementClient;

    @Bulkhead(name = "/movements/{accountId}", fallbackMethod = "movementsFallback")
    public ResponseEntity<List<IssuerResponse>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId) {
        return movementClient.getAllIssuersByAccountId(accountId);
    }

    @Bulkhead(name = "/movements/{accountId}", fallbackMethod = "movementsFallback")
    public ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @RequestBody IssuerRequest issuerRequest){
        return movementClient.createMovement(accountId, issuerRequest);
    }

    public ResponseEntity<List<IssuerResponse>> movementsFallback(Long accountId, Throwable throwable){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }

    public ResponseEntity<List<IssuerResponse>> movementsFallback(Long accountId, IssuerRequest issuerRequest, Throwable throwable){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }
}
