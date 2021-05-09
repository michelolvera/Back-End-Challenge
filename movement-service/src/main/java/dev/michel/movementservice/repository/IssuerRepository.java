package dev.michel.movementservice.repository;

import dev.michel.movementservice.entity.Issuer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuerRepository extends JpaRepository<Issuer, Long> {
    List<Issuer> findAllByAccountId(Long accountId);
}
