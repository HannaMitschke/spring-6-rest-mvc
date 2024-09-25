
// * Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController // return json body rather than html
// @RequestMapping("/api/v1/beer")
public class BeerController {

    // paths to utilize here and in tests, DRY
    public static final String BEER_PATH    = "/api/v1/beer";

    public static final String BEER_PATH_ID = BeerController.BEER_PATH + "/{beerId}";

    private final BeerService  beerService;

    @PostMapping(BeerController.BEER_PATH)
    public ResponseEntity addBeer(@RequestBody final Beer beer) { // request body to post the body given
        final Beer savedBeer = this.beerService.saveNewBeer(beer);

        final HttpHeaders headers = new HttpHeaders();
        // location allows to client to see where beer was added
        headers.add("Location", BeerController.BEER_PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(BeerController.BEER_PATH_ID)
    public ResponseEntity deleteBeerById(@PathVariable("beerId") final UUID beerId) {
        this.beerService.deleteBeerById(beerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(BeerController.BEER_PATH) // same as @RequestMapping(method = RequestMethod.GET), method so that it will only be
                                          // invoked if it is a GET
    public List<Beer> getAllBeers() {
        return this.beerService.listBeers();
    }

    @GetMapping(BeerController.BEER_PATH_ID) // same as @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") final UUID beerId) { // path variable to bind beer id
        BeerController.log.debug("Get Beer by Id - in controller");

        return this.beerService.getBeerById(beerId);
    }

    @PatchMapping(BeerController.BEER_PATH_ID)
    public ResponseEntity patchBeerById(@PathVariable("beerId") final UUID beerId, @RequestBody final Beer beer) {
        this.beerService.patchBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BeerController.BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable("beerId") final UUID beerId, @RequestBody final Beer beer) {
        this.beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
