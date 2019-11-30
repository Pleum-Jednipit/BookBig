package com.example.bookbig.bookcover;

import android.os.Parcel;
import android.os.Parcelable;

public class Bookcover implements Parcelable {
    private String userId;
    private String name;
    private String description;
    private String photoUrl;
    private String bookcoverType;
    private String bookcoverId;



    public String getBookcoverType() {
        return bookcoverType;
    }

    public void setBookcoverType(String bookcoverType) {
        this.bookcoverType = bookcoverType;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBookcoverId() {
        return bookcoverId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Bookcover(String name, String description, String photoUrl, String userId,String bookcoverId,String bookcoverType) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.bookcoverId = bookcoverId;
        this.bookcoverType = bookcoverType;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Bookcover{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", bookcoverId='" + bookcoverId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.photoUrl);
        dest.writeString(this.bookcoverType);
        dest.writeString(this.bookcoverId);
    }

    protected Bookcover(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.photoUrl = in.readString();
        this.bookcoverType = in.readString();
        this.bookcoverId = in.readString();
    }

    public static final Parcelable.Creator<Bookcover> CREATOR = new Parcelable.Creator<Bookcover>() {
        @Override
        public Bookcover createFromParcel(Parcel source) {
            return new Bookcover(source);
        }

        @Override
        public Bookcover[] newArray(int size) {
            return new Bookcover[size];
        }
    };
}
