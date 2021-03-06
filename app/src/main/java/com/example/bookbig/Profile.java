package com.example.bookbig;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Profile implements Parcelable {
    private String name;
    private String age;
    private String gender;
    private String userId;
    private String latitude;
    private String longtitude;
    private String phoneNumber;
    private String profilePicture;
    private long maxDistance;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public long getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(long maxDistance) {
        this.maxDistance = maxDistance;
    }

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

    public Profile(String name, String age, String gender,String profilePicture) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.profilePicture = profilePicture;

    }

    public Profile(String name, String age, String gender,String userId,String phoneNumber,long maxDistance,String profilePicture) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.maxDistance = maxDistance;
        this.profilePicture = profilePicture;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.age);
        dest.writeString(this.gender);
        dest.writeString(this.userId);
        dest.writeString(this.latitude);
        dest.writeString(this.longtitude);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.profilePicture);
        dest.writeLong(this.maxDistance);
    }

    protected Profile(Parcel in) {
        this.name = in.readString();
        this.age = in.readString();
        this.gender = in.readString();
        this.userId = in.readString();
        this.latitude = in.readString();
        this.longtitude = in.readString();
        this.phoneNumber = in.readString();
        this.profilePicture = in.readString();
        this.maxDistance = in.readLong();
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
