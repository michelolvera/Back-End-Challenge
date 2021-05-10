package dev.michel.accountservice.service;

import dev.michel.accountservice.entity.Account;

/**
 * Servicio que permite realizar CRUD de cuentas
 */
public interface AccountService {

    /**
     * Método que obtiene una cuenta buscándola por ID
     *
     * @param id ID de la cuenta que realizará la operación
     * @return Account el objeto cuenta requerido
     */
    Account getAccount(Long id);

    /**
     * Método que crea una nueva cuenta
     *
     * @param account El objeto cuenta con el saldo inicial
     * @return Account el objeto cuenta requerido
     */
    Account createAccount(Account account);

    /**
     * Método que actualiza el saldo de una cuenta
     *
     * @param account El objeto cuenta con el saldo nuevo
     * @return Account el objeto cuenta actualizado
     */
    Account updateAccount(Account account);

    /**
     * Método que permite la eliminación de una cuenta
     *
     * @param id ID de la cuenta que realizará la operación
     * @return Account el objeto cuenta actualizado
     */
    Account deleteAccount(Long id);

}
