package com.bootcamp.fixedtermservice.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICrudService <T, ID>{
    /**
     * Create mono.
     *
     * @param o the o
     * @return the mono
     */
    Mono<T> create(T o);

    /**
     * Find all flux.
     *
     * @return the flux
     */
    Flux<T> findAll();

    /**
     * Find by id mono.
     *
     * @param id the id
     * @return the mono
     */
    Mono<T> findById(ID id);

    /**
     * Update mono.
     *
     * @param o the o
     * @return the mono
     */
    Mono<T> update(T o);

    /**
     * Delete mono.
     *
     * @param o the o
     * @return the mono
     */
    Mono<Void> delete(T o);
}
