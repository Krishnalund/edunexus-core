package org.example;
import java.util.Stack;
public class ActivityFeedManager {
    private Stack<Activity> recentActivities;
    private int maxSize;
    public ActivityFeedManager(int maxSize){
        this.maxSize = maxSize;
        this.recentActivities = new Stack<>();
    }
    public void addActivity(String description, User user ){
        Activity activity = new Activity(description,user);
        if(recentActivities.size() >= maxSize){
            recentActivities.remove(0);
        }
        recentActivities.push(activity);
    }
    public Stack<Activity> getRecentActivities(int limit){
        Stack<Activity> clone = new Stack<>();
        int count = 0;
        for(int i=recentActivities.size()-1; i>=0 && count < limit; i--){
            clone.push(recentActivities.get(i));
            count++;
        }
        return clone;
    }
}
