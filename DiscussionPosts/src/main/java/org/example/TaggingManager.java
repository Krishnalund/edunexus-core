package org.example;
import java.sql.*;
import java.util.LinkedList;
import java.util.Iterator;
public class TaggingManager {
    	private LinkedList<Tag> allTags; // Global tags
    	private Connection dbConnection;
    public TaggingManager(Connection dbConnection) {
        this.dbConnection = dbConnection;
        this.allTags = new LinkedList<>();

        try {
            loadTagsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadTagsFromDatabase() throws SQLException {
        String query = "SELECT tagName FROM Tags";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String tagName = rs.getString("tagName");
                allTags.add(new Tag(tagName));  // add to linked list
            }
        }
    }
    public void addTagToPost(Post post, String tagName) throws SQLException {
        if (post == null || tagName == null || tagName.isEmpty())
            throw new IllegalArgumentException("Invalid post or tag name");

        // Check if tag already exists in global list
        Tag existingTag = null;
        for (Tag t : allTags) {
            if (t.getTagName().equalsIgnoreCase(tagName)) {
                existingTag = t;
                break;
            }
        }

        if (existingTag == null) {
            existingTag = new Tag(tagName);
            allTags.add(existingTag);
            saveTagToDatabase(existingTag);
        }

        // Check if the mapping already exists in DB before inserting
        String check = "SELECT COUNT(*) FROM PostTags WHERE postId = ? AND tagName = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(check)) {
            ps.setInt(1, post.getPostId());
            ps.setString(2, tagName);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("⚠️ Tag already linked to this post — skipping duplicate insert.");
                return; // stop here
            }
        }

        // Add tag to post object
        post.addTag(existingTag);

        // Insert mapping into DB
        String insert = "INSERT INTO PostTags (postId, tagName) VALUES (?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(insert)) {
            ps.setInt(1, post.getPostId());
            ps.setString(2, tagName);
            ps.executeUpdate();
        }
    }
    public LinkedList<Post> findSimilarPosts(String tagName, StudyGroup group) {
        LinkedList<Post> similarPosts = new LinkedList<>();
        if (group == null || tagName == null || tagName.isEmpty()) return similarPosts;

        // Recursive helper
        findSimilarRecursive(group.getMembers().iterator(), tagName, group, similarPosts);
        return similarPosts;
    }
    private void findSimilarRecursive(Iterator<User> iterator, String tagName, StudyGroup group, LinkedList<Post> result) {
        if (!iterator.hasNext()) return; // base case

        User user = iterator.next();
        // For demonstration, assume user’s posts are globally accessible (via DiscussionManager)
        // In full system, you’d fetch user’s posts in this group and check for matching tags.

        // Continue recursion
        findSimilarRecursive(iterator, tagName, group, result);
    }
    public LinkedList<Post> getPostsByTag(String tagName, StudyGroup group) {
        LinkedList<Post> filtered = new LinkedList<>();
        DiscussionManager dm = new DiscussionManager(dbConnection);  // load posts
        LinkedList<Post> posts = dm.viewPosts(group);

        Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()) {
            Post p = iterator.next();
            for (Tag t : p.getTags()) {
                if (t.getTagName().equalsIgnoreCase(tagName)) {
                    filtered.add(p);
                    break;
                }
            }
        }
        return filtered;
    }
    private void saveTagToDatabase(Tag tag) throws SQLException {
        String insert = "INSERT INTO Tags (tagName) VALUES (?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(insert)) {
            ps.setString(1, tag.getTagName());
            ps.executeUpdate();
        }
    }
}
