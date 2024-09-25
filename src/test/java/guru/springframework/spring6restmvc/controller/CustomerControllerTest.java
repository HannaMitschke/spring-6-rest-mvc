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

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @MockBean
    CustomerService          customerService;

    CustomerServiceImpl      customerServiceImpl;

    @Autowired
    MockMvc                  mockMvc;

    @Autowired
    ObjectMapper             objectMapper;

    @Captor
    ArgumentCaptor<UUID>     uuidArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.customerServiceImpl = new CustomerServiceImpl();
    }

    @Test // post
    void testAddCustomer() throws Exception {
        final Customer testCustomer = this.customerServiceImpl.listCustomers().get(0);
        testCustomer.setVersion(null);
        testCustomer.setId(null);

        BDDMockito.given(this.customerService.saveNewCustomer(ArgumentMatchers.any(Customer.class)))
                        .willReturn(this.customerServiceImpl.listCustomers().get(1));

        this.mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(testCustomer)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andExpect(MockMvcResultMatchers.header().exists("Location"));
    }

    @Test // delete
    void testDeleteCustomer() throws Exception {
        final Customer testCustomer = this.customerServiceImpl.listCustomers().get(0);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.customerService).deleteCustomerById(this.uuidArgumentCaptor.capture());
        Assertions.assertThat(this.uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
    }

    @Test // get
    void testGetAllCustomers() throws Exception {
        BDDMockito.given(this.customerService.listCustomers()).willReturn(this.customerServiceImpl.listCustomers());

        this.mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Is.is(3)));
    }

    @Test // get
    void testGetCustomerById() throws Exception {
        final Customer testCustomer = this.customerServiceImpl.listCustomers().get(0);
        BDDMockito.given(this.customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        this.mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(testCustomer.getId().toString())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(testCustomer.getName())));
    }

    @Test // patch
    void testPatchCustomerById() throws Exception {
        final Customer testCustomer = this.customerServiceImpl.listCustomers().get(0);

        final Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New Customer Name");

        this.mockMvc.perform(MockMvcRequestBuilders.patch(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(customerMap)))
                        .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.customerService).patchCustomerById(this.uuidArgumentCaptor.capture(),
                        this.customerArgumentCaptor.capture());
        Assertions.assertThat(this.uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
        Assertions.assertThat(this.customerArgumentCaptor.getValue().getName()).isEqualTo(customerMap.get("name"));
    }

    @Test // put
    void testUpdateCustomerById() throws Exception {
        final Customer testCustomer = this.customerServiceImpl.listCustomers().get(0);

        this.mockMvc.perform(MockMvcRequestBuilders.put(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(testCustomer)))
                        .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.customerService).updateCustomerById(this.uuidArgumentCaptor.capture(),
                        ArgumentMatchers.any(Customer.class));
        Assertions.assertThat(this.uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
    }

}
