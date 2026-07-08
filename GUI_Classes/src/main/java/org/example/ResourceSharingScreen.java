package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class ResourceSharingScreen extends JFrame {
    private AppContext context;
    private JPanel resourceListPanel;

    public ResourceSharingScreen(AppContext context) {
        this.context = context;

        setTitle("Resource Sharing");
        setSize(UIConstants.WINDOW_SIZE_LARGE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("Resources");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Resource list
        resourceListPanel = new JPanel();
        resourceListPanel.setLayout(new BoxLayout(resourceListPanel, BoxLayout.Y_AXIS));
        resourceListPanel.setBackground(UIConstants.BG_PRIMARY);

        JScrollPane scrollPane = UIHelper.createScrollPane(resourceListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Upload button
        JButton btnUpload = UIHelper.createSuccessButton("📤 Upload Resource");
        btnUpload.setPreferredSize(new Dimension(200, UIConstants.BUTTON_HEIGHT_LARGE));
        btnUpload.addActionListener(e -> {
            try { uploadResource(); }
            catch (SQLException | IOException ex) { ex.printStackTrace(); }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UIConstants.BG_PRIMARY);
        bottomPanel.add(btnUpload);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadResources();
        setVisible(true);
    }

    private void loadResources() {
        resourceListPanel.removeAll();
        LinkedList<Resource> resources = context.resourceManager.getAllResources();

        for (Resource r : resources) {
            resourceListPanel.add(Box.createVerticalStrut(10));
            resourceListPanel.add(createResourceCard(r));
        }

        resourceListPanel.revalidate();
        resourceListPanel.repaint();
    }

    private JPanel createResourceCard(Resource r) {
        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblTitle = new JLabel("<html><b>" + r.getTitle() + "</b><br><i>by " + r.getUploader().getUsername() + "</i></html>");
        lblTitle.setFont(UIConstants.BODY_LARGE_FONT);
        card.add(lblTitle, BorderLayout.CENTER);

        JButton btnDownload = UIHelper.createPrimaryButton("⬇ Download");
        btnDownload.addActionListener(e -> downloadResource(r));
        card.add(btnDownload, BorderLayout.EAST);

        return card;
    }

    private void uploadResource() throws SQLException, IOException {
        JTextField txtTitle = UIHelper.createTextField();
        JTextField txtPath = UIHelper.createTextField();
        JTextField txtDesc = UIHelper.createTextField();

        Object[] fields = {"Title:", txtTitle, "File Path:", txtPath, "Description:", txtDesc};
        int option = JOptionPane.showConfirmDialog(this, fields, "Upload Resource", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            context.resourceManager.uploadResource(null, context.currentUser,
                    txtTitle.getText(), txtPath.getText(), txtDesc.getText());
            loadResources();
        }
    }

    private void downloadResource(Resource r) {
        JOptionPane.showMessageDialog(this, "Downloading: " + r.getFilePath());
    }
}