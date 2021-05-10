package dev.michel.accountservice.service;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.OperationResponse;
import org.springframework.http.ResponseEntity;

public interface OperationService {
    ResponseEntity<OperationResponse> createOperation(Long id, IssuerRequest issuerRequest);
}
