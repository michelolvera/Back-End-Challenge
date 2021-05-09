package dev.michel.accountservice.service;

import dev.michel.accountservice.model.Issuer;
import dev.michel.accountservice.model.OperationResponse;

public interface OperationService {
    OperationResponse createOperation(Long id, Issuer issuer);
}
