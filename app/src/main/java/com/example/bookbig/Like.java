package com.example.bookbig;

public class Like {
    private String likeId;
    private String bookcoverId;
    private String ownerId;
    private String userLikeId;
    private String hifiveId;

    public Like(String likeId, String bookcoverId, String ownerId, String userLikeId, String hifiveId) {
        this.likeId = likeId;
        this.bookcoverId = bookcoverId;
        this.ownerId = ownerId;
        this.userLikeId = userLikeId;
        this.hifiveId = hifiveId;
    }

    public Like(String likeId, String bookcoverId, String ownerId, String userLikeId) {
        this.likeId = likeId;
        this.bookcoverId = bookcoverId;
        this.ownerId = ownerId;
        this.userLikeId = userLikeId;
    }

    public String getLikeId() {
        return likeId;
    }

    public String getBookcoverId() {
        return bookcoverId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getUserLikeId() {
        return userLikeId;
    }

    public String getHifiveId() {
        return hifiveId;
    }
}
