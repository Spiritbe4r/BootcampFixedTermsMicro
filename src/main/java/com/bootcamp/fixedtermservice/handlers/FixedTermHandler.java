package com.bootcamp.fixedtermservice.handlers;

import com.bootcamp.fixedtermservice.models.dto.ClientDTO;
import com.bootcamp.fixedtermservice.models.entities.FixedTermAccount;
import com.bootcamp.fixedtermservice.services.ICreditService;
import com.bootcamp.fixedtermservice.services.IFixedTermAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FixedTermHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedTermHandler.class);

    @Autowired
    private IFixedTermAccountService service;

    @Autowired
    private ICreditService creditService;
    /**
     * Find all mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), FixedTermAccount.class);
    }

    /**
     * New fixed term accound mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> newFixedTermAccount(ServerRequest request) {
        Mono<FixedTermAccount> fixedTermAccountMono = request.bodyToMono(FixedTermAccount.class);

        return fixedTermAccountMono.flatMap(fixedTermAccount -> service.getClient(fixedTermAccount.getClientIdNumber())
                        .filter(customer -> customer.getClientType().getCode().equals("1001")||customer.getClientType().getCode().equals("1002"))
                        .flatMap(customerDTO -> {
                            fixedTermAccount.setTypeOfAccount("FIXEDTERM_ACCOUNT");
                            fixedTermAccount.setClient(ClientDTO.builder()
                                    .name(customerDTO.getName()).code(customerDTO.getClientType().getCode())
                                    .clientIdNumber(customerDTO.getClientIdNumber()).build());
                            fixedTermAccount.setMaxLimitMovementPerMonth(fixedTermAccount.getMaxLimitMovementPerMonth());
                            fixedTermAccount.setMovementPerMonth(0);
                            return creditService.validateDebtorCredit(fixedTermAccount.getClientIdNumber())
                                    .flatMap(debtor -> {
                                        if(debtor == true) {
                                            return Mono.empty();
                                        }else return service.validateClientIdNumber(fixedTermAccount.getClientIdNumber());
                                    })
                                    .flatMap(accountFound -> {
                                        if(accountFound.getClientIdNumber() != null){
                                            LOGGER.info("La cuenta encontrada es: " + accountFound.getClientIdNumber());
                                            return Mono.empty();
                                        }else {
                                            LOGGER.info("No se encontró la cuenta ");
                                            return service.create(fixedTermAccount);
                                        }
                                    });
                        }))
                .flatMap( c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(c))
                ).switchIfEmpty(ServerResponse.badRequest().build());
    }

    /**
     * Update fixed term accound mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> updateFixedTermAccound(ServerRequest request) {
        Mono<FixedTermAccount> fixedTermAccountMono = request.bodyToMono(FixedTermAccount.class);
        String id = request.pathVariable("id");

        return service.findById(id).zipWith(fixedTermAccountMono, (db,req) -> {
                    db.setAmount(req.getAmount());
                    db.setMovementPerMonth(req.getMovementPerMonth());
                    return db;
                }).flatMap( c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.update(c), FixedTermAccount.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Delete fixed term accound mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> deleteFixedTermAccound(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<FixedTermAccount> creditMono = service.findById(id);

        return creditMono
                .doOnNext(c -> LOGGER.info("deleteConsumption: consumptionId={}", c.getId()))
                .flatMap(c -> service.delete(c).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Find by customer identity number mono.
     *
     * @param serverRequest the server request
     * @return the mono
     */
    public Mono<ServerResponse> findByCustomerIdentityNumber(ServerRequest serverRequest) {
        String customerIdentityNumber =  serverRequest.pathVariable("clientIdNumber");
        return  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findByClientIdNumber(customerIdentityNumber), FixedTermAccount.class);
    }

    /**
     * Find by account number mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findByAccountNumber(ServerRequest request) {
        String accountNumber = request.pathVariable("accountNumber");
        LOGGER.info("El AccountNumber es " + accountNumber);
        return service.findByAccountNumber(accountNumber).flatMap(c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(c)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
