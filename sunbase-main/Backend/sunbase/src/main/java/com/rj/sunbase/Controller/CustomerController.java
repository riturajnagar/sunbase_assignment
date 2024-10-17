package com.rj.sunbase.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rj.sunbase.Exception.CustomerNotFoundException;
import com.rj.sunbase.Model.Customer;
import com.rj.sunbase.Service.CustomerServiceImpl;
import com.rj.sunbase.Service.SyncService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private CustomerServiceImpl customerService;
	
	@Autowired
	private SyncService syncService;
	
	/**
     * Create a new customer.
     * 
     * @param customer The customer object to be created.
     * @return ResponseEntity with the created customer and status code.
     * @throws CustomerNotFoundException if customer creation fails.
     */
	@PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws CustomerNotFoundException {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }
	
	/**
     * Update an existing customer.
     * 
     * @param id The ID of the customer to be updated.
     * @param customer The customer object with updated details.
     * @return ResponseEntity with the updated customer and status code.
     * @throws CustomerNotFoundException if the customer is not found.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer) throws CustomerNotFoundException {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    /**
     * Get a paginated and sorted list of customers.
     *
     * @return ResponseEntity with a page of customers and status code.
     */
    @GetMapping("/")
    public ResponseEntity<List<Customer>> getCustomers() throws CustomerNotFoundException {

        return ResponseEntity.ok(customerService.getCustomers());
    }
    /**
     * Get a paginated and sorted list of customers.
     * 
     * @param page The page number to retrieve (default is 0).
     * @param size The number of customers per page (default is 10).
     * @param sort The sorting criteria in the format "property,direction" (default is "id,asc").
     * @return ResponseEntity with a page of customers and status code.
     */
    @GetMapping("/get")
    public ResponseEntity<Page<Customer>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0]).with(Sort.Direction.fromString(sort[1]))));
        return ResponseEntity.ok(customerService.getCustomers(pageable));
    }

    /**
     * Get a customer by its ID.
     * 
     * @param id The ID of the customer to retrieve.
     * @return ResponseEntity with the customer and status code.
     * @throws CustomerNotFoundException if the customer is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) throws CustomerNotFoundException {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
    
    /**
     * Delete a customer by its ID.
     * 
     * @param uuid The ID of the customer to delete.
     * @return ResponseEntity with no content and status code.
     * @throws CustomerNotFoundException if the customer is not found.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String uuid) throws CustomerNotFoundException {
    	System.out.println("customer id: "+ uuid);
        customerService.deleteCustomer(uuid);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/sync")
    public ResponseEntity<List<Customer>> syncApi() {
    	List<Customer> customers = this.syncService.syncCustomerListFromApi();
    	
    	return new ResponseEntity<List<Customer>>(customers, HttpStatus.ACCEPTED);
    }
}
