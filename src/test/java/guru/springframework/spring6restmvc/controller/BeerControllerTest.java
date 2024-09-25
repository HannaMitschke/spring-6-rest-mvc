/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;

@WebMvcTest(BeerController.class) // test splice, limit test to just this
class BeerControllerTest {

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @MockBean // tell mockito to provide a mock of this into spring context, return null response by default
    BeerService          beerService;        // dependency for BeerController

    BeerServiceImpl      beerServiceImpl;

    @Autowired
    MockMvc              mockMvc;

    @Autowired
    ObjectMapper         objectMapper;       // gets sprintboot's defaults, configured by springboot

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor; // bringing it up here allows it to be reused

    @BeforeEach
    void setUp() {
        // can modify beers in beerList and it will get reset before each test
        this.beerServiceImpl = new BeerServiceImpl();
    }

    @Test // post
    void testAddBeer() throws Exception {
        // below beer will be our request body so we need to clear out some stuff
        final Beer testBeer = this.beerServiceImpl.listBeers().get(0);
        testBeer.setVersion(null);
        testBeer.setId(null);

        // get 2nd item in list which is unaltered
        BDDMockito.given(this.beerService.saveNewBeer(ArgumentMatchers.any(Beer.class)))
                        .willReturn(this.beerServiceImpl.listBeers().get(1));

        this.mockMvc.perform(MockMvcRequestBuilders.post(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(testBeer))) // serialize
                        .andExpect(MockMvcResultMatchers.status().isCreated()) // 201 status
                        .andExpect(MockMvcResultMatchers.header().exists("Location"));

    }

    @Test // delete
    void testDeleteBeer() throws Exception {
        final Beer testBeer = this.beerServiceImpl.listBeers().get(0);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent()); // 204
                                                                                                                      // status

        // listen for value that is actually passed in to the delete operation
        // final ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class); // can also have it inline
        Mockito.verify(this.beerService).deleteBeerById(this.uuidArgumentCaptor.capture());

        // get that value that was passed in, and run assertion
        Assertions.assertThat(this.uuidArgumentCaptor.getValue()).isEqualTo(testBeer.getId());
    }

    @Test // get
    void testGetAllBeers() throws Exception {
        BDDMockito.given(this.beerService.listBeers()).willReturn(this.beerServiceImpl.listBeers());

        this.mockMvc.perform(MockMvcRequestBuilders.get(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Is.is(3)));
    }

    @Test // get
    void testGetBeerById() throws Exception { // mockito perform can throw exception
        // give it a mock beer object (json) for it to return
        final Beer testBeer = this.beerServiceImpl.listBeers().get(0);
        BDDMockito.given(this.beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        // perform a get against given url, should get back ok status
        this.mockMvc.perform(MockMvcRequestBuilders.get(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(testBeer.getId().toString()))) // $. = from root
                        .andExpect(MockMvcResultMatchers.jsonPath("$.beerName", Is.is(testBeer.getBeerName())));
    }

    @Test // patch
    void testPatchBeerById() throws Exception {
        final Beer testBeer = this.beerServiceImpl.listBeers().get(0);

        // easy way to make a partial beer object for request body that will get converted to json
        final Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Beer Name");

        this.mockMvc.perform(MockMvcRequestBuilders.patch(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(beerMap)))
                        .andExpect(MockMvcResultMatchers.status().isNoContent()); // 204 status

        // using captors to get the actual object passed in during the test so we can use them to compare (assert)
        Mockito.verify(this.beerService).patchBeerById(this.uuidArgumentCaptor.capture(), this.beerArgumentCaptor.capture());
        Assertions.assertThat(this.uuidArgumentCaptor.getValue()).isEqualTo(testBeer.getId());
        // check that that beer name did in fact get updated
        Assertions.assertThat(this.beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }

    @Test // put
    void testUpdateBeerById() throws Exception {
        final Beer testBeer = this.beerServiceImpl.listBeers().get(0);

        // accept = response format (in header)
        // content type = request body format (in header)
        // content = request body
        this.mockMvc.perform(MockMvcRequestBuilders.put(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(testBeer)))
                        .andExpect(MockMvcResultMatchers.status().isNoContent()); // 204 status

        // verify that the function got called 1 time, parameters are actual uuid and actual beer (w/ the captors)
        Mockito.verify(this.beerService).updateBeerById(this.uuidArgumentCaptor.capture(), this.beerArgumentCaptor.capture());
    }

}
