package org.example;
import java.util.LinkedList;
import java.util.NoSuchElementException;
//import org.example.User;
class CapacityExceededException extends Exception {
    public CapacityExceededException(String message) {
        super(message);
    }
}
public class StudyGroup {
    private int groupID;
    private String groupName;
    private String description;
    private User admin;
    private LinkedList<User> members;
    private int maxCapacity;
    private static int idCounter=1;
    // Constructor for new group
    public StudyGroup(String groupName, String description, User admin){
            if(groupName==null || groupName.isEmpty()){
                throw new IllegalArgumentException("Group name cannot be empty");
            }
            this.groupID=idCounter++;
            this.groupName =groupName;
            this.description=description;
            this.admin=admin;
            this.members=new LinkedList<>();
            this.maxCapacity=50;
    }
    public StudyGroup(int groupID, String groupName, String description, User admin) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.description = description;
        this.admin = admin;
        this.members = new LinkedList<>();
        this.maxCapacity = 50;
        if (groupID >= idCounter) {
            idCounter = groupID + 1; // ensure future IDs are unique
        }
    }
    public int getgroupID() {
        return groupID;
    }
    public String getName() {
        return groupName;
    }
    public User getAdmin() {
        return admin;
    }
    public boolean addMember(User user) throws CapacityExceededException{
        if(user==null) return false;
        if(members.size()>=maxCapacity )
            throw new CapacityExceededException("Can not add member.Group is at full capacity");
        if(members.contains(user)){
            System.out.println("User is already a member of this group");
            return false;
        }
        members.addLast(user);
        return true;
    }
    public void removeMember(User user){
        if(!members.contains(user)) {
            throw new NoSuchElementException("User not found in group");
        }
        members.remove(user);
        if(admin.equals(user) && !members.isEmpty()){
            admin=members.getFirst();
            System.out.println("Admin removed.Transferred admin rights to "+ admin.getUsername());
        }else if(admin.equals(user)){
            admin=null;
            System.out.println("Admin removed.No members left in group");
        }
    }
    LinkedList<User> getMembers(){
        return members;
    }
    public void displayGroupInfo() {
        System.out.println("Group ID: " + groupID);
        System.out.println("Name: " + groupName);
        System.out.println("Description: " + description);
        System.out.println("Admin: " + (admin != null ? admin.getUsername() : "None"));
        System.out.println("Members count: " + members.size());
    }

    public void setGroupID(int i) {
        this.groupID = i;
    }

    public String getDescription() {
        return description;
    }

    public String getgroupName() {
        return groupName;
    }
}
