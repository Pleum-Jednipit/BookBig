package com.example.bookbig.bookcover;

public class Bookcover {
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

}
