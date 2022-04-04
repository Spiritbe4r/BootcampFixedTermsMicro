package com.bootcamp.fixedtermservice.repositories;

import com.bootcamp.fixedtermservice.models.entities.FixedTermAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FixedTermRepository extends ReactiveMongoRepository<FixedTermAccount, String> {
    /**
     * Find by customer identity number mono.
     *
     * @param clientIdNumber the customer identity number
     * @return the mono
     */
    public Mono<FixedTermAccount> findByClientIdNumber(String clientIdNumber);

    /**
     * Find by account number mono.
     *
     * @param accountNumber the account number
     * @return the mono
     */
    public Mono<FixedTermAccount> findByAccountNumber(String accountNumber);
}
