package com.example.vehiclesist;

public class ViewModel {


    String firstName, lastName, email, address, rating, latitude, longitude ,number;



    public ViewModel(String firstName, String lastName, String email, String address, String rating, String latitude, String longitude , String number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getfirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public String getrating() {
        return rating;
    }

    public void setrating(String rating) {
        this.rating = rating;
    }

    public String getlatitude() {
        return latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }



}
