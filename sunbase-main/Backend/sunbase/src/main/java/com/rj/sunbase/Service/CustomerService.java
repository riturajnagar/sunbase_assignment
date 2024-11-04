package com.rj.sunbase.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rj.sunbase.Exception.CustomerNotFoundException;
import com.rj.sunbase.Model.Customer;

import java.util.List;

public interface CustomerService {
	
	public Customer createCustomer(Customer customer) throws CustomerNotFoundException;
	public Customer updateCustomer(String id, Customer customer) throws CustomerNotFoundException;
	public Customer getCustomerById(String id) throws CustomerNotFoundException;
    public void deleteCustomer(String id) throws CustomerNotFoundException;
	public List<Customer> getCustomers() throws CustomerNotFoundException;
	
	public 	Page<Customer> searchAndFilterCustomers(String searchTerm, String city, String state, String email,
			Pageable pageable) throws CustomerNotFoundException;
}
