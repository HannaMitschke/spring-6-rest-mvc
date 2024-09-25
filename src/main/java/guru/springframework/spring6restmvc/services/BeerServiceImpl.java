/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    // constructor
    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        final Beer beer1 = Beer.builder().id(UUID.randomUUID()).version(1).beerName("Galaxy Cat").beerStyle(BeerStyle.PALE_ALE)
                        .upc("123456").price(new BigDecimal("12.99")).quantityOnHand(122).createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now()).build();
        final Beer beer2 = Beer.builder().id(UUID.randomUUID()).version(1).beerName("Crank").beerStyle(BeerStyle.PALE_ALE)
                        .upc("123456222").price(new BigDecimal("11.99")).quantityOnHand(392).createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now()).build();
        final Beer beer3 = Beer.builder().id(UUID.randomUUID()).version(1).beerName("Sunshine City").beerStyle(BeerStyle.IPA)
                        .upc("12356").price(new BigDecimal("13.99")).quantityOnHand(144).createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now()).build();

        this.beerMap.put(beer1.getId(), beer1);
        this.beerMap.put(beer2.getId(), beer2);
        this.beerMap.put(beer3.getId(), beer3);
    }

    // delete
    @Override
    public void deleteBeerById(final UUID beerId) {
        this.beerMap.remove(beerId);
    }

    // get one
    @Override
    public Beer getBeerById(final UUID beerId) {
        BeerServiceImpl.log.debug("Get Beer by Id - in service. Id: " + beerId.toString());

        return this.beerMap.get(beerId);
    }

    // get all
    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(this.beerMap.values());
    }

    // patch
    @Override
    public void patchBeerById(final UUID beerId, final Beer beer) {
        final Beer existingBeer = this.beerMap.get(beerId);
        // check if property included in beer, and then update it
        if (beer.getBeerName() != null) {
            existingBeer.setBeerName(beer.getBeerName());
        }
        if (beer.getPrice() != null) {
            existingBeer.setPrice(beer.getPrice());
        }
        if (beer.getUpc() != null) {
            existingBeer.setUpc(beer.getUpc());
        }
        if (beer.getQuantityOnHand() != null) {
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if (beer.getBeerStyle() != null) {
            existingBeer.setBeerStyle(beer.getBeerStyle());
        }
    }

    // post
    @Override
    public Beer saveNewBeer(final Beer beer) {
        // mimicking database operation
        final Beer savedBeer = Beer.builder().id(UUID.randomUUID()).version(1).beerName(beer.getBeerName())
                        .beerStyle(beer.getBeerStyle()).upc(beer.getUpc()).price(beer.getPrice())
                        .quantityOnHand(beer.getQuantityOnHand()).createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now()).build();

        this.beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    // put
    @Override
    public void updateBeerById(final UUID beerId, final Beer beer) {
        final Beer existingBeer = this.beerMap.get(beerId);
        // set all the properties that could change
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setBeerStyle(beer.getBeerStyle());
    }

}
