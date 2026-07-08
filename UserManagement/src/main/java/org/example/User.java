package org.example;
import java.util.Date;
public class User {
        private String userId;
        private String username;
        private String passwordHash;
        private String email;
        private boolean isLoggedIn;
        private Date lastLogin;
    // Constructor for loading from DB
    public User(String userId, String username, String passwordHash, String email, boolean isLoggedIn, Date lastLogin) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.isLoggedIn = isLoggedIn;
        this.lastLogin = lastLogin;
    }
    // Constructor for new user (before DB insert)
    public User(String username, String passwordHash, String email) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.email = email;
            this.isLoggedIn = false;
            lastLogin = null;
    }
        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
        this.userId = userId;
        }
        public String getUsername(){
            return username;
        }
        public String getPasswordHash(){
            return passwordHash;
        }
        public String getEmail(){
        return email;
        }
        public boolean isLoggedIn() {
            return isLoggedIn;
        }
        public Date getLastLogin() {
        return lastLogin;
        }
        public void setPassword(String newPassword) {
            if (newPassword == null || newPassword.length() < 8) {
                throw new IllegalArgumentException("Invalid Password!");
            }
            this.passwordHash = newPassword;
        }
        public void login(){
            isLoggedIn=true;
            lastLogin=new Date();
            System.out.println(username + " logged in at "+lastLogin);
        }
        public void logout(){
            isLoggedIn=false;
            System.out.println(username+ " Logged out");
        }
    }

