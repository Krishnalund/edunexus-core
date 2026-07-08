package org.example;

public class Contributor {
    private User user;
    private int points;
    public Contributor(User user, int points){
        this.user = user;
        this.points = points;
    }
    public void addPoints(int amount){
        points+=amount;
    }
    public int getPoints(){
        return points;
    }

    public User getUser() {
        return user;
    }
}
