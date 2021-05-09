package dev.michel.accountservice.client;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.IssuerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "movement-service")
public interface MovementClient {

    @GetMapping(value = "/movements/{accountId}")
    ResponseEntity<List<IssuerResponse>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId);

    @PostMapping(value = "/movements/{accountId}")
    ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody IssuerRequest issuerRequest);

}
