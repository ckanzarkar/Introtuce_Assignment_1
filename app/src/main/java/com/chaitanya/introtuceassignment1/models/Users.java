package com.chaitanya.introtuceassignment1.models;

import com.google.firebase.Timestamp;

public class Users {

    private String name;
    private String lastName;
    private String dob;
    private String gender;
    private String country;
    private String state;
    private String town;
    private String phNo;
    private String teleNo;
    private String profileImage;
    private String id;
    private Timestamp timestamp;


//    public Users(String name, String lastName, String dob, String gender, String country, String state, String town, String phNo, String teleNo, String profileImage, String id) {
//        this.name = name;
//        this.lastName = lastName;
//        this.dob = dob;
//        this.gender = gender;
//        this.country = country;
//        this.state = state;
//        this.town = town;
//        this.phNo = phNo;
//        this.teleNo = teleNo;
//        this.profileImage = profileImage;
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getTeleNo() {
        return teleNo;
    }

    public void setTeleNo(String teleNo) {
        this.teleNo = teleNo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
