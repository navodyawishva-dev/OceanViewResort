package com.oceanview.model;

import java.util.Date;

public class Admin extends Person {

    private String username;
    private String password;
    private Date createdAt;

    public Admin() {
        super();
    }

    public Admin(String id, String fullName, String email, String phone,
                 String username, String password, Date createdAt) {
        super(id, fullName, email, phone);
        this.username  = username;
        this.password  = password;
        this.createdAt = createdAt;
    }

    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public Date   getCreatedAt() { return createdAt; }

    public void setUsername(String username)   { this.username = username; }
    public void setPassword(String password)   { this.password = password; }
    public void setCreatedAt(Date createdAt)   { this.createdAt = createdAt; }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public String toString() {
        return "Admin{id='" + getId() + "', username='" + username + "'}";
    }
}