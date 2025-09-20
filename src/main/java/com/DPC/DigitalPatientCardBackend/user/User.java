package com.DPC.DigitalPatientCardBackend.user;


import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
    protected String name;
    protected String username;
    private String gender;
    protected String password;
    protected int age;
    protected String address;
    protected String phoneNumber;
    protected String email;

    // Constructors
    public User() {}

    public User(String name, String username, int age, String address, String phoneNumber, String email, String password,String gender) {
        this.name = name;
        this.username = username;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) {
        if(gender!=null && (gender.equals("male")|| gender.equals("female"))) {
            this.gender = gender;
        }
    }
}
