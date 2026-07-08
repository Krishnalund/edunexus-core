package org.example;
import java.time.LocalDateTime;
import java.util.LinkedList;
public class Comment {
        private int commentId;                    // Unique ID from database
        private User author;                      // User who wrote the comment
        private String content;                   // Text content of the comment
        private LocalDateTime timestamp;          // When the comment was posted
        private Comment parentComment;            // Null if top-level; otherwise references parent
        private LinkedList<Comment> replies;      // Child comments (threaded replies)
        public Comment(User author, String content, Comment parentComment) {
            this.author = author;
            this.content = content;
            this.parentComment = parentComment;
            this.timestamp = LocalDateTime.now();
            this.replies = new LinkedList<>();  // DSA: LinkedList for dynamic replies
        }
        public int getCommentId() {
            return commentId;
        }

        public User getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public Comment getParentComment() {
            return parentComment;
        }

        public LinkedList<Comment> getReplies() {
            return replies;
        }
        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public void addReply(Comment reply) {
            if (reply != null) {
                replies.add(reply);  // Efficient insertion at end
            }
        }

        /**
         * Recursively counts total number of comments in this thread (including replies).
         * Demonstrates use of Recursion as required in the project.
         *
         * @return Total count of comments in subtree
         */
        public int getTotalReplyCount() {
            int count = 0;
            for (Comment reply : replies) {
                count += 1 + reply.getTotalReplyCount();  // Recursion on each child
            }
            return count;
        }

        /**
         * Checks if this is a top-level comment.
         *
         * @return true if no parent
         */
        public boolean isTopLevel() {
            return parentComment == null;
        }
        public String toString() {
            return "Comment{" +
                    "id=" + commentId +
                    ", author='" + author.getUsername() + '\'' +
                    ", content='" + content + '\'' +
                    ", timestamp=" + timestamp +
                    ", replyCount=" + replies.size() +
                    '}';
        }
    }

