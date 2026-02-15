package com.oceanview.model;

import java.util.Date;


public class Customer extends Person {

    private String nationalId;  // NIC number — must be unique
    private String address;
    private Date   createdAt;


    public Customer() {
        super();
    }


    public Customer(String id, String fullName, String email, String phone,
                    String nationalId, String address, Date createdAt) {
        super(id, fullName, email, phone);
        this.nationalId = nationalId;
        this.address    = address;
        this.createdAt  = createdAt;
    }


    public String getNationalId() { return nationalId; }
    public String getAddress()    { return address; }
    public Date   getCreatedAt()  { return createdAt; }


    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public void setAddress(String address)       { this.address = address; }
    public void setCreatedAt(Date createdAt)     { this.createdAt = createdAt; }


    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public String toString() {
        return "Customer{id='" + getId() + "', name='" + getFullName() +
                "', nationalId='" + nationalId + "'}";
    }
}
