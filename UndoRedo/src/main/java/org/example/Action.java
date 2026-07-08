package org.example;

import java.util.Date;

public class Action {
    private String actionType;  // e.g., "addPost"
    private Object data;        // e.g., Post object or Message object
    private Date timestamp;

    // Constructor
    public Action(String actionType, Object data) {
        this.actionType = actionType;
        this.data = data;
        this.timestamp = new Date(); // current timestamp
    }

    // Reverse logic for undo
    public void reverse() throws UnsupportedActionException {
        switch (actionType) {
            case "addPost":
                System.out.println("Reversing action: Removing recently added post.");
                // Logic to remove post (placeholder)
                break;

            case "deletePost":
                System.out.println("Reversing action: Restoring deleted post.");
                // Logic to restore post (placeholder)
                break;

            case "sendMessage":
                System.out.println("Reversing action: Deleting recently sent message.");
                // Logic to delete sent message (placeholder)
                break;

            default:
                throw new UnsupportedActionException("Action type not supported for reversal: " + actionType);
        }
    }

    // Getters (optional, if needed elsewhere)
    public String getActionType() {
        return actionType;
    }

    public Object getData() {
        return data;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
class UnsupportedActionException extends Exception {
    public UnsupportedActionException(String message) {
        super(message);
    }
}
