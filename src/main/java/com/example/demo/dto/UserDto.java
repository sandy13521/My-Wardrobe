package com.example.demo.dto;

import java.util.List;

public class UserDto {
    private String id;
    private String username;
    private String email;
    private int age;
    private String phoneNumber;
    private String gender;
    private List<String> wardrobeIds;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getWardrobeIds() {
        return wardrobeIds;
    }

    public void setWardrobeIds(List<String> wardrobeIds) {
        this.wardrobeIds = wardrobeIds;
    }
}
