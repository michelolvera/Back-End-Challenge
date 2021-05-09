package dev.michel.accountservice.client;

import dev.michel.accountservice.model.Issuer;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MovementClientCircuitBreaker {

    private final MovementClient movementClient;

    @Bulkhead(name = "/movements/{accountId}", fallbackMethod = "movementsFallback")
    public ResponseEntity<List<Issuer>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId) {
        return movementClient.getAllIssuersByAccountId(accountId);
    }

    @Bulkhead(name = "/movements/{accountId}", fallbackMethod = "movementsFallback")
    public ResponseEntity<List<Issuer>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody Issuer issuer){
        return movementClient.createMovement(accountId, issuer);
    }

    public ResponseEntity<List<Issuer>> movementsFallback(Long accountId, Throwable throwable){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }

    public ResponseEntity<List<Issuer>> movementsFallback(Long accountId, Issuer issuer, Throwable throwable){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }
}
