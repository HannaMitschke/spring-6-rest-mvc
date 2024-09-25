/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.Beer;

public interface BeerService {

    void deleteBeerById(UUID beerId);

    Beer getBeerById(UUID beerId);

    List<Beer> listBeers();

    void patchBeerById(UUID beerId, Beer beer);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);
}
