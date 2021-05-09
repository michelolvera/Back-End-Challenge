package dev.michel.accountservice.service;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.OperationResponse;

public interface OperationService {
    OperationResponse createOperation(Long id, IssuerRequest issuerRequest);
}
