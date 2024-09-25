/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Customer {

    LocalDateTime createdDate;

    UUID          id;

    String        name;

    LocalDateTime updatedDate;

    Integer       version;
}
