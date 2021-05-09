package dev.michel.accountservice.client;

import dev.michel.accountservice.model.Issuer;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "movement-service")
public interface MovementClient {

    @GetMapping(value = "/movements/{accountId}")
    ResponseEntity<List<Issuer>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId);

    @PostMapping(value = "/movements/{accountId}")
    ResponseEntity<List<Issuer>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody Issuer issuer);

}
