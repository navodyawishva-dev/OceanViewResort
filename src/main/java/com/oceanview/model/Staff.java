package com.oceanview.model;

import java.util.Date;


public class Staff extends Person {

    private String username;
    private String password;   // stored as BCrypt hash
    private String role;       // e.g. "Receptionist"
    private String status;     // "Pending", "Active", "Inactive"
    private Date   createdAt;


    public Staff() {
        super();
    }


    public Staff(String id, String fullName, String email, String phone,
                 String username, String password, String role,
                 String status, Date createdAt) {
        super(id, fullName, email, phone);
        this.username  = username;
        this.password  = password;
        this.role      = role;
        this.status    = status;
        this.createdAt = createdAt;
    }


    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getRole()      { return role; }
    public String getStatus()    { return status; }
    public Date   getCreatedAt() { return createdAt; }


    public void setUsername(String username)  { this.username = username; }
    public void setPassword(String password)  { this.password = password; }
    public void setRole(String role)          { this.role = role; }
    public void setStatus(String status)      { this.status = status; }
    public void setCreatedAt(Date createdAt)  { this.createdAt = createdAt; }



    @Override
    public String toString() {
        return "Staff{id='" + getId() + "', username='" + username +
                "', status='" + status + "'}";
    }
}