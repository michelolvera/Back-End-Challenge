package dev.michel.accountservice.repository;

import dev.michel.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio en el que se almacenan las cuentas
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
