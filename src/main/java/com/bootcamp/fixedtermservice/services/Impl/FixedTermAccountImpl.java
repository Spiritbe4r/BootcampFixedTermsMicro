package com.bootcamp.fixedtermservice.services.Impl;

import com.bootcamp.fixedtermservice.models.dto.Client;
import com.bootcamp.fixedtermservice.models.entities.FixedTermAccount;
import com.bootcamp.fixedtermservice.repositories.FixedTermRepository;
import com.bootcamp.fixedtermservice.services.IFixedTermAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class FixedTermAccountImpl implements IFixedTermAccountService {

    private static final Logger LOGGER  =   LoggerFactory.getLogger(FixedTermAccountImpl.class);
    @Autowired
    private FixedTermRepository repository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     *
     * @param fixedTeramAccound
     * @return
     */
    @Override
    public Mono<FixedTermAccount> create(final FixedTermAccount fixedTeramAccound) {
        return repository.save(fixedTeramAccound);
    }

    /**
     * @return flux
     */
    @Override
    public Flux<FixedTermAccount> findAll() {
        return repository.findAll();
    }

    /**
     * sobrescribir metodo findById para buscar por parametro.
     * @param id the id
     * @return
     */
    @Override
    public Mono<FixedTermAccount> findById(final String id) {
        return repository.findById(id);
    }

    /**
     * sobrescribir metodo update para actualizar.
     * @param fixedTermAccount
     * @return mono
     */
    @Override
    public Mono<FixedTermAccount> update(final FixedTermAccount fixedTermAccount) {
        return repository.save(fixedTermAccount);
    }

    /**
     * sobrescribir metodo delete para eliminar.
     * @param fixedTermAccount
     * @return void
     */
    @Override
    public Mono<Void> delete(final FixedTermAccount fixedTermAccount) {
        return repository.delete(fixedTermAccount);
    }

    /**
     * sobrescribir el metodo getCustomer para consumir el servicio externo.
     * @param clientIdNumber the customer identity number
     * @return
     */
    @Override
    public Mono<Client> getClient(final String clientIdNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("initializing client query");
        params.put("clientIdNumber", clientIdNumber);
        return webClient
                .get()
                .uri("/findClientCredit/{clientIdNumber}", clientIdNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Client.class))
                .doOnNext(c -> LOGGER.info("Customer Response: Customer={}", c.getName()));
    }

    /**
     * sobrescribir el metodo validateClientIdNumber
     * para buscar si existe el customer por número de identidad.
     * @param clientIdNumber the client identity number
     * @return
     */
    @Override
    public Mono<FixedTermAccount> validateClientIdNumber(final String clientIdNumber) {
        return repository.findByClientIdNumber(clientIdNumber)
                .switchIfEmpty(Mono.just(FixedTermAccount.builder()
                        .clientIdNumber(null).build()));
    }

    /**
     * sobrescribir el metodo findByClientIdNumber
     * para buscar por número de identidad.
     * @param clientIdNumber the customer identity number
     * @return
     */
    @Override
    public Mono<FixedTermAccount> findByClientIdNumber(final String clientIdNumber) {
        return repository.findByClientIdNumber(clientIdNumber);
    }

    /**
     * sobrescribir el metodo findByAccountNumber
     * para buscar por número de cuenta.
     * @param accountNumber the account number
     * @return
     */
    @Override
    public Mono<FixedTermAccount> findByAccountNumber(final String accountNumber) {
        LOGGER.info("El AccountNumber es" + accountNumber);
        return repository.findByAccountNumber(accountNumber);
    }
}
