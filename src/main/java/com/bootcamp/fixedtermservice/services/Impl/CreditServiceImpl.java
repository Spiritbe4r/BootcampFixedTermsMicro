package com.bootcamp.fixedtermservice.services.Impl;

import com.bootcamp.fixedtermservice.models.dto.Credit;
import com.bootcamp.fixedtermservice.models.dto.Creditcard;
import com.bootcamp.fixedtermservice.services.ICreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreditServiceImpl implements ICreditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditServiceImpl.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${microservices-urls.api-credit}")
    private String apiCredit;

    @Value("${microservices-urls.api-creditcard}")
    private String apiCreditcard;


    @Override
    public Flux<Credit> getCredit(String clientIdNumber) {
        Map<String, Object> params = new HashMap<String,Object>();
        LOGGER.info("initializing credit query");
        params.put("clientIdNumber",clientIdNumber);
        return webClientBuilder
                .baseUrl(apiCredit)
                .build()
                .get()
                .uri("/client/{clientIdNumber}",clientIdNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Credit.class))
                .switchIfEmpty(Flux.just(Credit.builder()
                        .debitor(false).build()))
                .doOnNext(c -> LOGGER.info("Credit Response: Contract={}", c.getContractNumber()));
    }

    @Override
    public Mono<Creditcard> getCreditcard(String clientIdNumber) {
        Map<String, Object> params = new HashMap<String,Object>();
        LOGGER.info("initializing creditcard query");
        params.put("clientIdNumber",clientIdNumber);
        return webClientBuilder
                .baseUrl(apiCreditcard)
                .build()
                .get()
                .uri("/client/{clientIdNumber}",clientIdNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Creditcard.class))
                .switchIfEmpty(Mono.just(Creditcard.builder()
                        .debitor(false).build()))
                .doOnNext(c -> LOGGER.info("CreditCard Response: Pan={}", c.getPan()));
    }

    @Override
    public Mono<Boolean> validateDebtorCredit(String clientIdNumber) {

        Mono<List<Credit>> credit = getCredit(clientIdNumber)
                .filter(creditFound -> creditFound.isDebitor()).collectList();
        Mono<Creditcard> creditcard= getCreditcard(clientIdNumber);

        return Mono.zip(credit,creditcard, (a,b) -> {

            if(a.size()>0){
                return true;
            }else if (b.isDebitor()){
                return true;
            } else return false;
        }).doOnNext(value -> LOGGER.info("the value of Debtor validate is: {}"+ value));

        // other form
//        Mono<Creditcard> creditcard= getCreditcard(customerIdentityNumber);
//
//        return getCredit(customerIdentityNumber).zipWith(creditcard, (a,b) -> {
//
//            if(a.isDebtor()){
//                return true;
//            }else if (b.isDebtor()){
//                return true;
//            } else return false;
//        }).doOnNext(value -> LOGGER.info("the value of Debtor validate is: {}"+ value));
//    }
    }
}
