package org.example;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;

public class DiscussionManager {
    private LinkedList<Post> posts;
    private Connection dbConnection;

    public DiscussionManager(Connection dbConnection) {
        this.dbConnection = dbConnection;
        this.posts = new LinkedList<>();

        try {
            loadPostsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPostsFromDatabase() throws SQLException {
        String query = "SELECT postId, groupId, authorUsername, content, postDate FROM Posts ORDER BY postDate ASC";
        try (Statement stmt = dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int postId = rs.getInt("postId");
                int groupId = rs.getInt("groupId");
                String authorName = rs.getString("authorUsername");
                String content = rs.getString("content");
                Timestamp ts = rs.getTimestamp("postDate");

                // You can fetch group or user using their managers later; for now create temp
                // objects
                User author = new User(authorName, "dummy", authorName + "@mail.com");
                StudyGroup group = new StudyGroup(groupId, "Group#" + groupId, "Loaded from DB", author);

                posts.addLast(new Post(postId, group, author, content, ts));
            }
        }
    }

    public Post addPost(StudyGroup group, User author, String content) throws SQLException {
        if (group == null || author == null || content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Invalid post data");
        }

        String insert = "INSERT INTO Posts (groupId, authorUsername, content, postDate) VALUES (?, ?, ?, ?)";
        Timestamp now = new Timestamp(System.currentTimeMillis());

        try (PreparedStatement ps = dbConnection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, group.getgroupID());
            ps.setString(2, author.getUsername());
            ps.setString(3, content);
            ps.setTimestamp(4, now);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next())
                id = rs.getInt(1);

            Post newPost = new Post(id, group, author, content, now);
            posts.addLast(newPost); // DSA: LinkedList addLast
            return newPost;
        }
    }

    public LinkedList<Post> viewPosts(StudyGroup group) {
        LinkedList<Post> groupPosts = new LinkedList<>();
        Iterator<Post> iterator = posts.iterator();

        while (iterator.hasNext()) {
            Post p = iterator.next();
            if (p.getGroup().getgroupID() == group.getgroupID()) {
                groupPosts.add(p);
            }
        }
        return groupPosts;
    }

    public boolean deletePost(int postId, User user) throws SQLException {
        Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()) {
            Post p = iterator.next();
            if (p.getPostId() == postId) {
                if (p.getAuthor().equals(user) || p.getGroup().getAdmin().equals(user)) {
                    iterator.remove();
                    String delete = "DELETE FROM Posts WHERE postId = ?";
                    try (PreparedStatement ps = dbConnection.prepareStatement(delete)) {
                        ps.setInt(1, postId);
                        ps.executeUpdate();
                    }
                    return true;
                } else {
                    System.out.println("Permission denied: only author or admin can delete this post.");
                    return false;
                }
            }
        }
        return false;
    }

    LinkedList<Post> getAllPosts() {
        return posts;
    }

    public Post getPostById(int postId) {
        for (Post p : posts) {
            if (p.getPostId() == postId) {
                return p;
            }
        }
        return null;
    }

    public boolean likePost(int postId, User user) {
        Post post = getPostById(postId);
        if (post != null && user != null) {
            post.likePost(user);
            return true;
        }
        return false;
    }

    public boolean unlikePost(int postId, User user) {
        Post post = getPostById(postId);
        if (post != null && user != null) {
            post.unlikePost(user);
            return true;
        }
        return false;
    }
}
