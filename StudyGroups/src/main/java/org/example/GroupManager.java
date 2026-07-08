package org.example;
import java.sql.*;
public class GroupManager {
    	private StudyGroup[] groups;
        private int groupCount;
    	private Connection dbConnection;
        public GroupManager(Connection dbConnection){
            this.dbConnection = dbConnection;
            this.groups = new StudyGroup[50];
            this.groupCount = 0;

            try {
                loadGroupsFromDatabase();
                loadGroupMembers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    private void loadGroupsFromDatabase() throws SQLException {
        String query = "SELECT groupID, groupName, description, creatorUsername FROM Groups";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("groupID");
                String name = rs.getString("groupName");
                String desc = rs.getString("description");
                String creatorName = rs.getString("creatorUsername");
                if (name == null) name = "Unnamed Group";
                // You may fetch creator from DB later; for now null-safe
                User creator = new User("0" , creatorName, "dummy", creatorName + "@mail.com", false , null);

                groups[groupCount++] = new StudyGroup(id, name, desc, creator);
            }
        }
    }
    private void loadGroupMembers() throws SQLException {
        for (int i = 0; i < groupCount; i++) {
            StudyGroup group = groups[i];
            String query = "SELECT username FROM GroupMembers WHERE groupId = ?";
            try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
                ps.setInt(1, group.getgroupID());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String username = rs.getString("username");
                    User member = new User("0", username, "dummy", username + "@mail.com", false, null);
                    try {
                        group.addMember(member);
                    } catch (CapacityExceededException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public StudyGroup createGroup(String name, String description, User creator)
            throws SQLException, DuplicateGroupException {
        // Check database first
        String checkQuery = "SELECT COUNT(*) FROM Groups WHERE groupName = ?";
        try (PreparedStatement psCheck = dbConnection.prepareStatement(checkQuery)) {
            psCheck.setString(1, name);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new DuplicateGroupException("Group already exists in database: " + name);
            }
        }
        // Linear search for duplicates
        for (int i = 0; i < groupCount; i++) {
            String existingName = groups[i].getName();
            if (existingName != null && existingName.equalsIgnoreCase(name)) {
                throw new DuplicateGroupException("Group already exists: " + name);
            }
        }

        // Resize if full
        if (groupCount == groups.length) resizeArray();

        // Insert into DB
        String insert = "INSERT INTO Groups (groupName, description, creatorUsername) VALUES (?, ?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, creator.getUsername());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);

            StudyGroup newGroup = new StudyGroup(id, name, description, creator);
            groups[groupCount++] = newGroup;
            return newGroup;
        }
    }
    public boolean joinGroup(int groupId, User user)
            throws SQLException, NoSuchGroupException,CapacityExceededException {
        StudyGroup group = getGroupById(groupId);
        if (group.getMembers().contains(user)) {
            System.out.println("User already a member in memory");
            return false;
        }

// Database check
        String checkQuery = "SELECT COUNT(*) FROM GroupMembers WHERE groupID = ? AND username = ?";
        try (PreparedStatement psCheck = dbConnection.prepareStatement(checkQuery)) {
            psCheck.setInt(1, groupId);
            psCheck.setString(2, user.getUsername());
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("User already a member in database");
                return false;
            }
        }
        group.addMember(user);

        // Update DB: Insert into GroupMembers table
        String insert = "INSERT INTO GroupMembers (groupID, username) VALUES (?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(insert)) {
            ps.setInt(1, groupId);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        }
        return true;
    }
    public void leaveGroup(int groupId, User user)
            throws SQLException, NoSuchGroupException {
        StudyGroup group = getGroupById(groupId);
        group.removeMember(user);

        String delete = "DELETE FROM GroupMembers WHERE groupId = ? AND username = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(delete)) {
            ps.setInt(1, groupId);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        }
    }
    public StudyGroup getGroupById(int groupId) throws NoSuchGroupException {
        for (int i = 0; i < groupCount; i++) {
            if (groups[i].getgroupID() == groupId) {
                return groups[i];
            }
        }
        throw new NoSuchGroupException("Group ID not found: " + groupId);
    }
    private void resizeArray() {
        StudyGroup[] newArray = new StudyGroup[groups.length * 2];
        System.arraycopy(groups, 0, newArray, 0, groups.length);
        groups = newArray;
    }
    public int getGroupCount() {
        return groupCount;
    }
    public StudyGroup[] getAllGroups() {
        StudyGroup[] result = new StudyGroup[groupCount];
        System.arraycopy(groups, 0, result, 0, groupCount);
        return result;
    }
}
class DuplicateGroupException extends Exception {
    public DuplicateGroupException(String message) {
        super(message);
    }
}
class NoSuchGroupException extends Exception {
    public NoSuchGroupException(String message) {
        super(message);
    }
}


