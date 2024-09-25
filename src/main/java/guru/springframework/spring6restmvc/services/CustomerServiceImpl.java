/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> customerMap;

    // constructor
    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        final Customer customer1 = Customer.builder().id(UUID.randomUUID()).version(1).name("Tom")
                        .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();
        final Customer customer2 = Customer.builder().id(UUID.randomUUID()).version(1).name("Sally")
                        .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();
        final Customer customer3 = Customer.builder().id(UUID.randomUUID()).version(1).name("May")
                        .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();

        this.customerMap.put(customer1.getId(), customer1);
        this.customerMap.put(customer2.getId(), customer2);
        this.customerMap.put(customer3.getId(), customer3);
    }

    // delete
    @Override
    public void deleteCustomerById(final UUID customerId) {
        this.customerMap.remove(customerId);
    }

    // get one
    @Override
    public Customer getCustomerById(final UUID customerId) {
        return this.customerMap.get(customerId);
    }

    // get all
    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(this.customerMap.values());
    }

    // patch
    @Override
    public void patchCustomerById(final UUID customerId, final Customer customer) {
        final Customer exisitingCustomer = this.customerMap.get(customerId);

        if (exisitingCustomer.getName() != null) {
            exisitingCustomer.setName(customer.getName());
        }

    }

    // post
    @Override
    public Customer saveNewCustomer(final Customer customer) {
        final Customer newCustomer = Customer.builder().id(UUID.randomUUID()).version(1).name(customer.getName())
                        .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();
        this.customerMap.put(newCustomer.getId(), newCustomer);

        return newCustomer;
    }

    // put
    @Override
    public void updateCustomerById(final UUID customerId, final Customer customer) {
        final Customer existingCustomer = this.customerMap.get(customerId);
        existingCustomer.setName(customer.getName());
    }

}
