package com.bootcamp.fixedtermservice.models.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Creditcard {
    private String pan;

    private ClientDTO client;

    private boolean debitor;
}
