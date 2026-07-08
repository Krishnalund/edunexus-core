package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class PostDetailsScreen extends JFrame {
    private AppContext context;
    private Post post;
    private JButton btnLike;
    private JLabel lblLikeCount;
    private JPanel commentsPanel;

    public PostDetailsScreen(AppContext context, Post post) {
        this.context = context;
        this.post = post;

        setTitle("Post Details");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Post content panel
        JPanel postPanel = UIHelper.createCardPanel();
        postPanel.setLayout(new BorderLayout(10, 10));

        // Header with author and time
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblAuthor = new JLabel("Posted by " + post.getAuthor().getUsername());
        lblAuthor.setFont(UIConstants.HEADER_FONT);
        lblAuthor.setForeground(UIConstants.PRIMARY);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        JLabel lblTime = new JLabel(sdf.format(post.getTimestamp()));
        lblTime.setFont(UIConstants.BODY_FONT);
        lblTime.setForeground(UIConstants.TEXT_SECONDARY);
        
        headerPanel.add(lblAuthor, BorderLayout.WEST);
        headerPanel.add(lblTime, BorderLayout.EAST);
        postPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JTextArea txtContent = UIHelper.createTextArea();
        txtContent.setText(post.getContent());
        txtContent.setEditable(false);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setFont(UIConstants.BODY_LARGE_FONT);
        postPanel.add(txtContent, BorderLayout.CENTER);

        // Like panel
        JPanel likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        likePanel.setOpaque(false);
        
        btnLike = post.isLikedBy(context.currentUser) 
            ? UIHelper.createSuccessButton("❤️ Unlike") 
            : UIHelper.createPrimaryButton("🤍 Like");
        btnLike.addActionListener(e -> toggleLike());
        
        lblLikeCount = new JLabel(post.getLikes() + " likes");
        lblLikeCount.setFont(UIConstants.BODY_LARGE_FONT);
        lblLikeCount.setForeground(UIConstants.TEXT_SECONDARY);
        
        likePanel.add(btnLike);
        likePanel.add(lblLikeCount);
        postPanel.add(likePanel, BorderLayout.SOUTH);

        mainPanel.add(postPanel, BorderLayout.NORTH);

        // Comments section
        JPanel commentsSection = new JPanel(new BorderLayout(0, 10));
        commentsSection.setBackground(UIConstants.BG_PRIMARY);

        JLabel lblCommentsTitle = UIHelper.createSubtitleLabel("Comments (" + post.getCommentCount() + ")");
        lblCommentsTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        commentsSection.add(lblCommentsTitle, BorderLayout.NORTH);

        // Comments list
        commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBackground(UIConstants.BG_PRIMARY);
        
        JScrollPane commentsScroll = UIHelper.createScrollPane(commentsPanel);
        commentsScroll.setPreferredSize(new Dimension(500, 200));
        commentsSection.add(commentsScroll, BorderLayout.CENTER);

        // Add comment input
        JPanel addCommentPanel = new JPanel(new BorderLayout(10, 0));
        addCommentPanel.setBackground(UIConstants.BG_PRIMARY);
        addCommentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JTextField txtComment = UIHelper.createTextField();
        txtComment.setPreferredSize(new Dimension(400, 40));
        
        JButton btnAddComment = UIHelper.createPrimaryButton("Post Comment");
        btnAddComment.addActionListener(e -> addComment(txtComment));
        
        addCommentPanel.add(txtComment, BorderLayout.CENTER);
        addCommentPanel.add(btnAddComment, BorderLayout.EAST);
        commentsSection.add(addCommentPanel, BorderLayout.SOUTH);

        mainPanel.add(commentsSection, BorderLayout.CENTER);

        // Load comments
        loadComments();

        setVisible(true);
    }

    private void toggleLike() {
        if (post.isLikedBy(context.currentUser)) {
            context.discussionManager.unlikePost(post.getPostId(), context.currentUser);
            btnLike.setText("🤍 Like");
            UIHelper.createPrimaryButton("temp"); // Reset style
            btnLike.setBackground(UIConstants.PRIMARY);
        } else {
            context.discussionManager.likePost(post.getPostId(), context.currentUser);
            btnLike.setText("❤️ Unlike");
            btnLike.setBackground(UIConstants.SUCCESS);
        }
        lblLikeCount.setText(post.getLikes() + " likes");
    }

    private void loadComments() {
        commentsPanel.removeAll();
        
        try {
            LinkedList<Comment> comments = context.commentManager.getCommentsForPost(post);
            
            if (comments.isEmpty()) {
                JLabel emptyLabel = new JLabel("No comments yet. Be the first to comment!");
                emptyLabel.setFont(UIConstants.BODY_FONT);
                emptyLabel.setForeground(UIConstants.TEXT_SECONDARY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                commentsPanel.add(Box.createVerticalGlue());
                commentsPanel.add(emptyLabel);
                commentsPanel.add(Box.createVerticalGlue());
            } else {
                for (Comment comment : comments) {
                    commentsPanel.add(createCommentCard(comment));
                    commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading comments");
            errorLabel.setForeground(UIConstants.ERROR);
            commentsPanel.add(errorLabel);
        }
        
        commentsPanel.revalidate();
        commentsPanel.repaint();
    }

    private JPanel createCommentCard(Comment comment) {
        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblAuthor = new JLabel(comment.getAuthor().getUsername());
        lblAuthor.setFont(UIConstants.BODY_LARGE_FONT);
        lblAuthor.setForeground(UIConstants.PRIMARY);

        JLabel lblContent = new JLabel("<html>" + comment.getContent() + "</html>");
        lblContent.setFont(UIConstants.BODY_FONT);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd 'at' hh:mm a");
        JLabel lblTime = new JLabel(sdf.format(java.sql.Timestamp.valueOf(comment.getTimestamp())));
        lblTime.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblTime.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(lblAuthor, BorderLayout.WEST);
        topPanel.add(lblTime, BorderLayout.EAST);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(lblContent, BorderLayout.CENTER);

        return card;
    }

    private void addComment(JTextField txtComment) {
        String content = txtComment.getText().trim();
        
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Comment cannot be empty!");
            return;
        }
        
        try {
            Comment newComment = context.commentManager.addComment(post, context.currentUser, content);
            if (newComment != null) {
                post.addComment(newComment);
                txtComment.setText("");
                loadComments();
                
                // Update comment count in title
                JLabel lblCommentsTitle = (JLabel) ((JPanel) getContentPane().getComponent(1)).getComponent(0);
                lblCommentsTitle.setText("Comments (" + post.getCommentCount() + ")");
            } else {
                JOptionPane.showMessageDialog(this, "Duplicate comment detected!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding comment: " + e.getMessage());
        }
    }
}
