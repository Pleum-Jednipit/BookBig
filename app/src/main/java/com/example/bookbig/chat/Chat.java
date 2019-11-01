package com.example.bookbig.chat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class Chat {
    private String message;
    private Boolean currentUser;
    private String timestamp;

    @Override
    public boolean equals(@Nullable Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Chat)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Chat chat = (Chat) obj;

        // Compare the data members and return accordingly
        return this.message.equals(chat.message) && this.currentUser == chat.currentUser
                && this.timestamp.equals(chat.timestamp);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "message='" + message + '\'' +
                ", currentUser=" + currentUser +
                ", timestamp=" + timestamp +
                '}';
    }

    public Chat(String message, Boolean currentUser, String timestamp) {
        this.message = message;
        this.currentUser = currentUser;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }
}
