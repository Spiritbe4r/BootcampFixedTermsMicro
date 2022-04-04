package com.bootcamp.fixedtermservice.models.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerRequest
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private String name;
    private String clientIdType;
    private String clientIdNumber;
    private ClientType clientType;
}
