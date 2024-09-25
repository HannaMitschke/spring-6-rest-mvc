/* Copyright Alcon 2023 */
package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.Customer;

public interface CustomerService {
    void deleteCustomerById(UUID customerId);

    Customer getCustomerById(UUID customerId);

    List<Customer> listCustomers();

    void patchCustomerById(UUID customerId, Customer customer);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);
}
