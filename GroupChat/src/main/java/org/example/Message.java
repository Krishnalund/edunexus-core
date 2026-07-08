package org.example;
import org.example.User;
import java.util.Date;
public class Message {
    private int messageId;
    private User sender;
    private String content;
    private Date timestamp;

    // Constructor: sets timestamp to current
    public Message(User sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = new Date();
    }

    // Getter for content
    public String getContent() {
        return content;
    }

    // Optional helper getters
    public int getMessageId() {
        return messageId;
    }

    public User getSender() {
        return sender;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // Setter for messageId when stored/retrieved from DB
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
