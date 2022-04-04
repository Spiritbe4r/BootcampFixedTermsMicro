package com.bootcamp.fixedtermservice.services.Impl;

import com.bootcamp.fixedtermservice.models.entities.FixedTermAccount;
import com.bootcamp.fixedtermservice.repositories.FixedTermRepository;
import com.bootcamp.fixedtermservice.util.FixedTermAccountCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class FixedTermAccountImplTest {

    @InjectMocks
    FixedTermAccountImpl fixedTermAccountImpl;
    @Mock
    FixedTermRepository fixedTermRepositoryMock;
    private final FixedTermAccount fixedTermAccount = FixedTermAccountCreator.createValidFixedTermAccount();

    @BeforeEach
    public void setUp(){
        BDDMockito.when(fixedTermRepositoryMock.save(ArgumentMatchers.any(FixedTermAccount.class)))
                .thenReturn(Mono.just(fixedTermAccount));
        BDDMockito.when(fixedTermRepositoryMock.findAll())
                .thenReturn(Flux.just(fixedTermAccount));
        BDDMockito.when(fixedTermRepositoryMock.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(fixedTermAccount));
        BDDMockito.when(fixedTermRepositoryMock.delete(ArgumentMatchers.any(FixedTermAccount.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(fixedTermRepositoryMock.findByClientIdNumber(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(fixedTermAccount));
        BDDMockito.when(fixedTermRepositoryMock.findByAccountNumber(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(fixedTermAccount));
    }

    @Test
    void create() {
        FixedTermAccount fixedTermAccountToBeSaved = FixedTermAccountCreator.createFixedTermAccountToBeSaved();
        StepVerifier.create(fixedTermAccountImpl.create(fixedTermAccountToBeSaved))
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }

    @Test
    void findAll() {
        StepVerifier.create(fixedTermAccountImpl.findAll())
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }

    @Test
    void findById() {
        StepVerifier.create(fixedTermAccountImpl.findById("q234ddfhr7583"))
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }

    @Test
    void update() {
        StepVerifier.create(fixedTermAccountImpl.update(fixedTermAccount))
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }

    @Test
    void delete() {
        StepVerifier.create(fixedTermAccountImpl.delete(fixedTermAccount))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void validateCustomerIdentityNumber() {
        StepVerifier.create(fixedTermAccountImpl.validateClientIdNumber("77380599"))
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }

    @Test
    void findByCustomerIdentityNumber() {
        StepVerifier.create(fixedTermAccountImpl.findByClientIdNumber("77380599"))
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }

    @Test
    void findByAccountNumber() {
        StepVerifier.create(fixedTermAccountImpl.findByAccountNumber("1234-1234-1235-3456"))
                .expectSubscription()
                .expectNext(fixedTermAccount)
                .verifyComplete();
    }
}