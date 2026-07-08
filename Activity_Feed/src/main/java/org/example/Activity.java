package org.example;

import java.util.Date;

public class Activity {
    private String description;
    private Date timestamp;
    private User involvedUser;
    public Activity(String description, User involvedUser){
        this.description = description;
        this.involvedUser = involvedUser;
        this.timestamp = new Date();
    }
    public String getDescription() {
        return description;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public User getInvolvedUser() {
        return involvedUser;
    }

}
