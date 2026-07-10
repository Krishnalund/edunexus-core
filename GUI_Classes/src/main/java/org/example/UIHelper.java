package org.example;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * UI Helper Class
 * Provides reusable UI component creation methods
 */
public class UIHelper {
    
    /**
     * Create a styled primary button
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBackground(UIConstants.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, UIConstants.BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.PRIMARY);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled success button
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBackground(UIConstants.ACCENT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, UIConstants.BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.ACCENT_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.ACCENT);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled secondary button (outlined)
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(UIConstants.PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(UIConstants.PRIMARY, 2));
        button.setPreferredSize(new Dimension(150, UIConstants.BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.PRIMARY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled large button for dashboard
     */
    public static JButton createDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.DASHBOARD_BUTTON_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(UIConstants.TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.PRIMARY_LIGHT);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIConstants.PRIMARY, 2),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIConstants.BORDER_LIGHT, 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled text field
     */
    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(UIConstants.BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    /**
     * Create a styled password field
     */
    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(UIConstants.BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    /**
     * Create a styled label
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.BODY_FONT);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a title label
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(UIConstants.TITLE_FONT);
        label.setForeground(UIConstants.PRIMARY_DARK);
        return label;
    }
    
    /**
     * Create a subtitle label
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.SUBTITLE_FONT);
        label.setForeground(UIConstants.PRIMARY_DARK);
        return label;
    }
    
    /**
     * Create a card panel with border and padding
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, 
                                           UIConstants.PADDING_MEDIUM, 
                                           UIConstants.PADDING_MEDIUM, 
                                           UIConstants.PADDING_MEDIUM)
        ));
        return panel;
    }
    
    /**
     * Create a styled text area
     */
    public static JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(UIConstants.BODY_LARGE_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);
        area.setForeground(UIConstants.TEXT_PRIMARY);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }
    
    /**
     * Create a styled scroll pane
     */
    public static JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_MEDIUM, 1));
        return scrollPane;
    }
    public static Icon createDotIcon(Color color) {
    return new Icon() {
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(x, y, getIconWidth(), getIconHeight());
            g2.dispose();
        }
        public int getIconWidth() { return 14; }
        public int getIconHeight() { return 14; }
    };
}
}
