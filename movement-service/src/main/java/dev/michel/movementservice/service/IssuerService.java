package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;

import java.util.List;

public interface IssuerService {
    List<IssuerResponse> createIssuer(IssuerRequest issuerRequest);
    List<IssuerResponse> getAllIssuersByAccountId(Long accountId);
}
