package com.oceanview.model;


public abstract class Person {

    private String id;
    private String fullName;
    private String email;
    private String phone;


    public Person() {}


    public Person(String id, String fullName, String email, String phone) {
        this.id       = id;
        this.fullName = fullName;
        this.email    = email;
        this.phone    = phone;
    }


    public String getId()       { return id; }
    public String getFullName() { return fullName; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }


    public void setId(String id)             { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email)       { this.email = email; }
    public void setPhone(String phone)       { this.phone = phone; }


    public abstract String getRole();

    @Override
    public String toString() {
        return "Person{id='" + id + "', name='" + fullName + "', role='" + getRole() + "'}";
    }
}