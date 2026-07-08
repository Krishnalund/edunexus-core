package org.example;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
public class UserManager {
    	private User[] users;
    	private int userCount;
    	private Connection dbConnection;
        public UserManager(Connection dbConnection){
            this.dbConnection=dbConnection;
            this.users=new User[100];
            this.userCount=0;
            try {
                loadUsersFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        private void loadUsersFromDatabase() throws SQLException{
            String query = "SELECT userId , username, passwordHash , email, isLoggedIn , lastLogin FROM Users";
            try (Statement stmt = dbConnection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String userId = rs.getString("userId");
                    String username = rs.getString("username");
                    String passwordHash = rs.getString("passwordHash");
                    String email = rs.getString("email");
                    boolean isLoggedIn = rs.getBoolean("isLoggedIn");
                    Timestamp lastLogin = rs.getTimestamp("lastLogin");
                    users[userCount++] = new User(userId , username, passwordHash, email, isLoggedIn ,lastLogin);
                }
            }
        }
    public boolean registerUser(String username, String password, String email) throws SQLException {
        if (username == null || password == null || email == null)
            throw new IllegalArgumentException("Invalid input");
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equalsIgnoreCase(username)) {
                return false; // duplicate found
            }
        }
        String passwordHash = hashPassword(password);  // NEW
        User newUser = new User(username, passwordHash, email);
        String insert = "INSERT INTO Users (username, passwordHash, email, isLoggedIn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, email);
            ps.setBoolean(4, false);
            ps.executeUpdate();

            // Get generated userId from Access
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUser.setUserId(generatedKeys.getString(1)); // NEW
                }
            }
        }

// Add to in-memory array
        if (userCount == users.length) resizeArray();
        users[userCount++] = newUser;
        return true;
    }
    public User loginUser(String username, String password) {
        String passwordHash = hashPassword(password);
        for (int i = 0; i < userCount; i++) {
            User u = users[i];
            if (u.getUsername().equalsIgnoreCase(username) && u.getPasswordHash().equals(passwordHash)) {
                u.login();
                // Update DB login status and timestamp
                String updateSQL = "UPDATE Users SET isLoggedIn = ?, lastLogin = ? WHERE userId = ?";
                try (PreparedStatement ps = dbConnection.prepareStatement(updateSQL)) {
                    ps.setInt(1, 1); // 1 = TRUE
                    ps.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // current date/time
                    ps.setString(3, u.getUserId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return u;
            }
        }
        return null;
    }
    public void logoutUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        user.logout();

        try (PreparedStatement ps = dbConnection.prepareStatement(
                "UPDATE Users SET isLoggedIn = ? WHERE userId = ?")) {
            ps.setInt(1, 0);
            ps.setString(2, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public User[] getAllUsers() {
        return Arrays.copyOf(users, userCount);
    }
    private void resizeArray() {
        User[] newArray = new User[users.length * 2];
        System.arraycopy(users, 0, newArray, 0, users.length);
        users = newArray;
    }
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(User u) {
            this.users = new User[]{u};
    }
    public User[] searchUsers(String query) {
        if (query == null || query.isEmpty()) return new User[0];

        // Temporary list to store matching users
        ArrayList<User> results = new ArrayList<>();

        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().toLowerCase().contains(query.toLowerCase())) {
                results.add(users[i]);
            }
        }

        return results.toArray(new User[0]);
    }


}



