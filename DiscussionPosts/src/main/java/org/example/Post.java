package org.example;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

public class Post {
    private int postId;
    private User author;
    private String content;
    private Date timestamp;
    private LinkedList<Comment> comments;
    private StudyGroup group;
    private LinkedList<Tag> tags;
    private int likes;
    private HashSet<String> likedBy;

    public Post(int postId, StudyGroup group, User author, String content, Date timestamp) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }
        this.postId = postId;
        this.group = group;
        this.timestamp = timestamp != null ? timestamp : new Date();
        this.comments = new LinkedList<>();
        this.author = author;
        this.content = content;
        this.tags = new LinkedList<>();
        this.likes = 0;
        this.likedBy = new HashSet<>();
    }

    public void addTag(Tag tag) {
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public LinkedList<Tag> getTags() {
        return tags;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public LinkedList<Comment> getComments() {
        return comments;
    }

    public int getPostId() {
        return postId;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public StudyGroup getGroup() {
        return group;
    }

    // Like system methods
    public void likePost(User user) {
        if (user != null && !likedBy.contains(user.getUsername())) {
            likedBy.add(user.getUsername());
            likes++;
        }
    }

    public void unlikePost(User user) {
        if (user != null && likedBy.contains(user.getUsername())) {
            likedBy.remove(user.getUsername());
            likes--;
        }
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLikedBy(User user) {
        return user != null && likedBy.contains(user.getUsername());
    }

    public int getCommentCount() {
        return comments.size();
    }
}
