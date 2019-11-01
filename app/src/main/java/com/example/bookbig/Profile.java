package com.example.bookbig;

import androidx.annotation.Nullable;

public class Profile {
    private String name;
    private String age;
    private String gender;
    private String userId;
    private String latitude;
    private String longtitude;
    private String phoneNumber;
    private int maxDistance;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Profile)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Profile profile = (Profile) obj;

        // Compare the data members and return accordingly
        return this.userId.equals(profile.userId);
    }

    public String getUserId() {
        return userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public Profile(String name, String age, String gender,String userId,String phoneNumber,int maxDistance) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.maxDistance = maxDistance;
    }

    public Profile(String userId, String latitude, String longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.userId = userId;
    }

    public Profile(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public Profile(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", userId='" + userId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                '}';
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
