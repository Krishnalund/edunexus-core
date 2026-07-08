package org.example;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

    public class CommentManager {
        private Connection dbConnection;

        // Cache to hold loaded comments for quick access (optional)
        private Map<Integer, Comment> commentMap;

        public CommentManager(Connection dbConnection) {
            this.dbConnection = dbConnection;
            this.commentMap = new HashMap<>();
        }

        /**
         * Add a new top-level comment to a post
         */
        public Comment addComment(Post post, User author, String content) throws SQLException {
            return addComment(post, author, content, null); // parentComment = null
        }

        /**
         * Add a reply to an existing comment
         */
        public Comment addComment(Post post, User author, String content, Comment parentComment) throws SQLException {
            if (post == null || author == null || content == null || content.isEmpty()) {
                throw new IllegalArgumentException("Invalid comment data");
            }
            // ✅ Duplicate check: prevent same user posting same content on same post
            String checkQuery = "SELECT COUNT(*) FROM Comments WHERE postId = ? AND authorUsername = ? AND content = ?";
            try (PreparedStatement checkStmt = dbConnection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, post.getPostId());
                checkStmt.setString(2, author.getUsername());
                checkStmt.setString(3, content);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("⚠️ Duplicate comment detected — skipping insert.");
                    return null; // stop here, don't insert duplicate
                }
            }
            String insert = "INSERT INTO Comments (postId, parentCommentId, authorUsername, content, commentDate) VALUES (?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();

            try (PreparedStatement ps = dbConnection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, post.getPostId());
                ps.setObject(2, parentComment != null ? parentComment.getCommentId() : null);
                ps.setString(3, author.getUsername());
                ps.setString(4, content);
                ps.setTimestamp(5, Timestamp.valueOf(now));

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int id = 0;
                if (rs.next()) id = rs.getInt(1);

                Comment newComment = new Comment(author, content, parentComment);
                newComment.setCommentId(id);

                if (parentComment != null) {
                    parentComment.addReply(newComment);
                }
                commentMap.put(id, newComment);
                return newComment;
            }
        }

        /**
         * Load all comments for a post, including replies
         */
        public LinkedList<Comment> getCommentsForPost(Post post) throws SQLException {
            LinkedList<Comment> topLevelComments = new LinkedList<>();
            commentMap.clear();

            String query = "SELECT commentId, parentCommentId, authorUsername, content, commentDate FROM Comments WHERE postId = ? ORDER BY commentDate ASC";
            try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
                ps.setInt(1, post.getPostId());
                ResultSet rs = ps.executeQuery();

                // First pass: create Comment objects
                while (rs.next()) {
                    int id = rs.getInt("commentId");
                    Integer parentId = rs.getObject("parentCommentId", Integer.class);
                    String username = rs.getString("authorUsername");
                    String content = rs.getString("content");
                    Timestamp ts = rs.getTimestamp("commentDate");

                    User author = new User(username, "dummy", username + "@mail.com"); // load real user as needed
                    Comment comment = new Comment(author, content, null);
                    comment.setCommentId(id);
                    comment.setTimestamp(ts.toLocalDateTime());
                    commentMap.put(id, comment);

                    // For now, store parentId in commentMap (will link later)
                    commentMap.put(id, comment);
                }
            }

            // Second pass: link parent-child relationships
            for (Comment comment : commentMap.values()) {
                String queryParent = "SELECT parentCommentId FROM Comments WHERE commentId = ?";
                try (PreparedStatement ps = dbConnection.prepareStatement(queryParent)) {
                    ps.setInt(1, comment.getCommentId());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        Integer parentId = rs.getObject("parentCommentId", Integer.class);
                        if (parentId != null) {
                            Comment parent = commentMap.get(parentId);
                            if (parent != null) {
                                parent.addReply(comment);
                            }
                        } else {
                            topLevelComments.add(comment);
                        }
                    }
                }
            }

            return topLevelComments;
        }
    }
