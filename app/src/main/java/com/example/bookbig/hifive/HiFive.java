package com.example.bookbig.hifive;

import java.sql.Timestamp;
import java.util.List;

public class HiFive {
    private List<String> userId;
    private Timestamp timestamp;;
    private String hifiveId;
    private String name;

    public String getHifiveId() {
        return hifiveId;
    }

    public List<String> getUserId() {
        return userId;
    }


    public HiFive(String name,String hifiveId) {
        this.name = name;
        this.hifiveId = hifiveId;
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
