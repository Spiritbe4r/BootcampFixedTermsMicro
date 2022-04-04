package com.bootcamp.fixedtermservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    private String contractNumber;

    private ClientDTO client;

    private boolean debitor;
}
