package com.example.registration;

public class UserData {
    private String userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String age;
    private String address;
    private String city;
    private String gender;
    private String email;
    private String password;
    private String course;
    private String yearLevel;
    private String schoolID;
    private String vaccine;
    private String vaccineDosage;
    private String imageURL;

    public UserData(String userId, String firstName, String lastName, String phoneNumber, String age,
                    String address, String city, String gender, String email, String password, String course,
                    String yearLevel, String schoolID, String vaccine, String vaccineDosage, String imageURL) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
        this.city = city;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.course = course;
        this.yearLevel = yearLevel;
        this.schoolID = schoolID;
        this.vaccine = vaccine;
        this.vaccineDosage = vaccineDosage;
        this.imageURL = imageURL;
    }

    public UserData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getVaccineDosage() {
        return vaccineDosage;
    }

    public void setVaccineDosage(String vaccineDosage) {
        this.vaccineDosage = vaccineDosage;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
