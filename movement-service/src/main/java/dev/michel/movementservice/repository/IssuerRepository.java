package dev.michel.movementservice.repository;

import dev.michel.movementservice.entity.IssuerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuerRepository extends JpaRepository<IssuerRequest, Long> {
    List<IssuerRequest> findAllByAccountId(Long accountId);
    IssuerRequest findByAccountIdAndIssuerName(Long accountId, String issuerName);
}
