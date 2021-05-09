package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.Issuer;

import java.util.List;

public interface IssuerService {
    List<Issuer> createIssuer(Issuer issuer);
    List<Issuer> getAllIssuersByAccountId(Long accountId);
}
