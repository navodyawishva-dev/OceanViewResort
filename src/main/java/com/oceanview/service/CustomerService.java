package com.oceanview.service;

import com.oceanview.dao.CustomerDAO;
import com.oceanview.model.Customer;
import com.oceanview.util.IdGenerator;
import com.oceanview.util.ValidationUtil;

import java.util.List;


public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();


    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }


    public Customer getCustomerById(String customerId) {
        return customerDAO.findById(customerId);
    }


    public Customer getCustomerByNationalId(String nationalId) {
        return customerDAO.findByNationalId(nationalId);
    }

    public Customer getCustomerByPhone(String phone) {
        return customerDAO.findByPhone(phone);
    }


    public List<Customer> searchByName(String name) {
        if (!ValidationUtil.isNotEmpty(name)) {
            return customerDAO.findAll();
        }
        return customerDAO.searchByName(name.trim());
    }


    public Customer registerCustomer(String fullName, String email,
                                     String phone, String nationalId,
                                     String address) throws Exception {


        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new Exception("Full name is required");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Valid 10-digit phone number is required");
        }
        if (!ValidationUtil.isValidNIC(nationalId)) {
            throw new Exception("Valid NIC number is required (e.g. 123456789V or 200012345678)");
        }


        if (ValidationUtil.isNotEmpty(email) && !ValidationUtil.isValidEmail(email)) {
            throw new Exception("Invalid email format");
        }


        if (customerDAO.nationalIdExists(nationalId.trim())) {
            Customer existing = customerDAO.findByNationalId(nationalId.trim());
            if (existing != null) {
                return existing;
            }
        }


        Customer customer = new Customer();
        customer.setId(IdGenerator.generateCustomerId());
        customer.setFullName(fullName.trim());
        customer.setEmail(email != null ? email.trim() : "");
        customer.setPhone(phone.trim());
        customer.setNationalId(nationalId.trim().toUpperCase());
        customer.setAddress(address != null ? address.trim() : "");


        if (!customerDAO.insert(customer)) {
            throw new Exception("Failed to register customer. Please try again.");
        }

        return customer; // return saved customer (needed for reservation)
    }


    public void updateCustomer(String customerId, String fullName, String email,
                               String phone, String address) throws Exception {


        Customer existing = customerDAO.findById(customerId);
        if (existing == null) {
            throw new Exception("Customer not found: " + customerId);
        }


        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new Exception("Full name is required");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Valid 10-digit phone number is required");
        }
        if (ValidationUtil.isNotEmpty(email) && !ValidationUtil.isValidEmail(email)) {
            throw new Exception("Invalid email format");
        }


        existing.setFullName(fullName.trim());
        existing.setEmail(email != null ? email.trim() : "");
        existing.setPhone(phone.trim());
        existing.setAddress(address != null ? address.trim() : "");

        if (!customerDAO.update(existing)) {
            throw new Exception("Failed to update customer. Please try again.");
        }
    }


    public void deleteCustomer(String customerId) throws Exception {
        Customer existing = customerDAO.findById(customerId);
        if (existing == null) {
            throw new Exception("Customer not found: " + customerId);
        }
        if (!customerDAO.delete(customerId)) {
            throw new Exception(
                    "Cannot delete customer. They may have existing reservations.");
        }
    }


    public int getTotalCustomers() {
        return customerDAO.countAll();
    }
}
