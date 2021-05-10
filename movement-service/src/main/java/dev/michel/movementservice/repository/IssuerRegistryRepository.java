package dev.michel.movementservice.repository;

import dev.michel.movementservice.entity.IssuerRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuerRegistryRepository extends JpaRepository<IssuerRegistry, Long> {
    IssuerRegistry findByAccountIdAndIssuerNameAndTotalSharesAndSharePriceAndOperation(Long accountId, String issuerName, Integer totalShares, Double sharePrice, String operation);
}
