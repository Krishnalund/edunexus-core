package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class DiscussionPostsScreen extends JFrame {
    private AppContext context;
    private JPanel postsPanel;

    public DiscussionPostsScreen(AppContext context) {
        this.context = context;

        setTitle("Discussion Posts");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Header with title and create button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BG_PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel title = UIHelper.createTitleLabel("Discussion Posts");
        JButton btnCreate = UIHelper.createSuccessButton("+ Create Post");
        btnCreate.addActionListener(e -> showCreatePostDialog());
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnCreate, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Posts panel
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(UIConstants.BG_PRIMARY);

        JScrollPane scrollPane = UIHelper.createScrollPane(postsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadPosts();
        setVisible(true);
    }

    private void loadPosts() {
        postsPanel.removeAll();
        LinkedList<Post> posts = context.discussionManager.getAllPosts();

        if (posts.isEmpty()) {
            JLabel emptyLabel = new JLabel("No discussion posts yet. Create the first one!", SwingConstants.CENTER);
            emptyLabel.setFont(UIConstants.BODY_LARGE_FONT);
            emptyLabel.setForeground(UIConstants.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(Box.createVerticalGlue());
            postsPanel.add(emptyLabel);
            postsPanel.add(Box.createVerticalGlue());
        } else {
            for (Post p : posts) {
                postsPanel.add(createPostCard(p));
                postsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    private JPanel createPostCard(Post post) {
        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Header with author and time
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblAuthor = new JLabel(post.getAuthor().getUsername());
        lblAuthor.setFont(UIConstants.HEADER_FONT);
        lblAuthor.setForeground(UIConstants.PRIMARY);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd 'at' hh:mm a");
        JLabel lblTime = new JLabel(sdf.format(post.getTimestamp()));
        lblTime.setFont(UIConstants.BODY_FONT);
        lblTime.setForeground(UIConstants.TEXT_SECONDARY);
        
        headerPanel.add(lblAuthor, BorderLayout.WEST);
        headerPanel.add(lblTime, BorderLayout.EAST);
        card.add(headerPanel, BorderLayout.NORTH);

        // Content preview
        String preview = post.getContent();
        if (preview.length() > 150) {
            preview = preview.substring(0, 150) + "...";
        }
        JLabel lblContent = new JLabel("<html>" + preview + "</html>");
        lblContent.setFont(UIConstants.BODY_FONT);
        card.add(lblContent, BorderLayout.CENTER);

        // Footer with likes and comments
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        footerPanel.setOpaque(false);
        
        JLabel lblLikes = new JLabel("❤️ " + post.getLikes() + " likes");
        lblLikes.setFont(UIConstants.BODY_FONT);
        lblLikes.setForeground(UIConstants.TEXT_SECONDARY);
        
        JLabel lblComments = new JLabel("💬 " + post.getCommentCount() + " comments");
        lblComments.setFont(UIConstants.BODY_FONT);
        lblComments.setForeground(UIConstants.TEXT_SECONDARY);
        
        footerPanel.add(lblLikes);
        footerPanel.add(lblComments);
        card.add(footerPanel, BorderLayout.SOUTH);

        // Click to open details
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new PostDetailsScreen(context, post);
            }
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(UIConstants.PRIMARY_LIGHT);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(UIConstants.BG_CARD);
            }
        });

        return card;
    }

    private void showCreatePostDialog() {
        JDialog dialog = new JDialog(this, "Create New Post", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIConstants.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Content area
        JLabel lblContent = UIHelper.createLabel("Post Content:");
        JTextArea txtContent = UIHelper.createTextArea();
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setRows(10);
        
        JScrollPane scrollPane = UIHelper.createScrollPane(txtContent);
        
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setBackground(UIConstants.BG_PRIMARY);
        contentPanel.add(lblContent, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BG_PRIMARY);
        
        JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());
        
        JButton btnPost = UIHelper.createPrimaryButton("Post");
        btnPost.addActionListener(e -> {
            String content = txtContent.getText().trim();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Post content cannot be empty!");
                return;
            }
            
            try {
                // Get first group or create a default one
                StudyGroup[] groups = context.groupManager.getAllGroups();
                StudyGroup group = groups.length > 0 ? groups[0] : 
                    new StudyGroup(1, "General", "General Discussion", context.currentUser);
                
                Post newPost = context.discussionManager.addPost(group, context.currentUser, content);
                if (newPost != null) {
                    JOptionPane.showMessageDialog(dialog, "Post created successfully!");
                    dialog.dispose();
                    loadPosts();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to create post!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnPost);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
}