/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Beer {

    private String        beerName;

    private BeerStyle     beerStyle;

    private LocalDateTime createdDate;

    private UUID          id;

    private BigDecimal    price;

    private Integer       quantityOnHand;

    private String        upc;

    private LocalDateTime updatedDate;

    private Integer       version;

}
