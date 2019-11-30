package com.example.bookbig.hifive;

import java.sql.Timestamp;
import java.util.List;

public class HiFive {
    private List<String> userId;
    private Timestamp timestamp;;
    private String hifiveId;
    private String name;
    private String chatUserId;
    private String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getHifiveId() {
        return hifiveId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public HiFive(String name,String hifiveId,String chatUserId,String profilePicture) {
        this.name = name;
        this.hifiveId = hifiveId;
        this.chatUserId = chatUserId;
        this.profilePicture = profilePicture;
    }


    public HiFive(String name,String hifiveId,String chatUserId) {
        this.name = name;
        this.hifiveId = hifiveId;
        this.chatUserId = chatUserId;
    }

    public String getChatUserId() {
        return chatUserId;
    }

    public HiFive(List<String> userId, String hifiveId) {
        this.userId = userId;
        this.hifiveId = hifiveId;
    }

    @Override
    public String toString() {
        return "HiFive{" +
                "userId=" + userId +
                ", timestamp=" + timestamp +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
