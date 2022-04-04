package com.bootcamp.fixedtermservice.services;

import com.bootcamp.fixedtermservice.models.dto.Client;
import com.bootcamp.fixedtermservice.models.entities.FixedTermAccount;
import reactor.core.publisher.Mono;

public interface IFixedTermAccountService extends ICrudService<FixedTermAccount, String> {

    /**
     * Gets customer.
     *
     * @param clientIdNumber the customer identity number
     * @return the customer
     */
    public Mono<Client> getClient(String clientIdNumber);

    /**
     * Validate customer identity number mono.
     *
     * @param clientIdNumber the customer identity number
     * @return the mono
     */
    public Mono<FixedTermAccount> validateClientIdNumber(String clientIdNumber);

    /**
     * Find by customer identity number mono.
     *
     * @param customerIdentityNumber the customer identity number
     * @return the mono
     */
    public Mono<FixedTermAccount> findByClientIdNumber(String customerIdentityNumber);

    /**
     * Find by account number mono.
     *
     * @param accountNumber the account number
     * @return the mono
     */
    public Mono<FixedTermAccount> findByAccountNumber(String accountNumber);
}
