package com.bootcamp.fixedtermservice.models.dto;

import lombok.*;

/**
 * CustomerCommand
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {
  private String name;
  private String code;
  private String clientIdNumber;
}
