/* Copyright Alcon 2023 */
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

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String    CUSTOMER_PATH    = "/api/v1/customers";

    public static final String    CUSTOMER_PATH_ID = CustomerController.CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @PostMapping(CustomerController.CUSTOMER_PATH)
    public ResponseEntity addCustomer(@RequestBody final Customer customer) {
        final Customer savedCustomer = this.customerService.saveNewCustomer(customer);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CustomerController.CUSTOMER_PATH + "/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(CustomerController.CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") final UUID customerId) {
        this.customerService.deleteCustomerById(customerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(CustomerController.CUSTOMER_PATH)
    public List<Customer> getAllCustomers() {
        return this.customerService.listCustomers();
    }

    @GetMapping(CustomerController.CUSTOMER_PATH_ID)
    public Customer getCustomerById(@PathVariable("customerId") final UUID customerId) {
        return this.customerService.getCustomerById(customerId);
    }

    @PatchMapping(CustomerController.CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") final UUID customerId,
                    @RequestBody final Customer customer) {
        this.customerService.patchCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CustomerController.CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") final UUID customerId,
                    @RequestBody final Customer customer) {
        this.customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
