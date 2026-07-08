package org.example;

import java.awt.*;

/**
 * UI Design System Constants
 * Defines all colors, fonts, and spacing used throughout the application
 */
public class UIConstants {
    
    // ========== COLOR PALETTE ==========
    
    // Primary Colors
    public static final Color PRIMARY = new Color(70, 130, 180);      // Steel Blue
    public static final Color PRIMARY_DARK = new Color(47, 79, 79);   // Dark Slate Gray
    public static final Color PRIMARY_LIGHT = new Color(176, 196, 222); // Light Steel Blue
    
    // Accent Colors
    public static final Color ACCENT = new Color(60, 179, 113);       // Medium Sea Green
    public static final Color ACCENT_HOVER = new Color(46, 139, 87);  // Sea Green
    
    // Background Colors
    public static final Color BG_PRIMARY = new Color(245, 245, 250);  // Light Gray Blue
    public static final Color BG_SECONDARY = Color.WHITE;
    public static final Color BG_CARD = Color.WHITE;
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);   // Almost Black
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117); // Gray
    
    // Status Colors
    public static final Color SUCCESS = new Color(34, 139, 34);       // Forest Green
    public static final Color WARNING = new Color(255, 140, 0);       // Dark Orange
    public static final Color ERROR = new Color(220, 20, 60);         // Crimson
    public static final Color INFO = new Color(70, 130, 180);         // Steel Blue
    
    // Difficulty Colors (for challenges/quizzes)
    public static final Color DIFFICULTY_EASY = new Color(34, 139, 34);
    public static final Color DIFFICULTY_MEDIUM = new Color(255, 140, 0);
    public static final Color DIFFICULTY_HARD = new Color(220, 20, 60);
    
    // Border Colors
    public static final Color BORDER_LIGHT = new Color(220, 220, 220);
    public static final Color BORDER_MEDIUM = new Color(200, 200, 200);
    
    // ========== TYPOGRAPHY ==========
    
    // Headers
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    // Body
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_LARGE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    
    // Buttons
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BUTTON_LARGE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    // Dashboard
    public static final Font DASHBOARD_BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 18);
    
    // ========== SPACING ==========
    
    public static final int PADDING_SMALL = 10;
    public static final int PADDING_MEDIUM = 15;
    public static final int PADDING_LARGE = 20;
    public static final int PADDING_XLARGE = 30;
    
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_HEIGHT_LARGE = 50;
    
    public static final int BORDER_RADIUS = 5;
    
    // ========== DIMENSIONS ==========
    
    public static final Dimension WINDOW_SIZE_SMALL = new Dimension(600, 500);
    public static final Dimension WINDOW_SIZE_MEDIUM = new Dimension(700, 600);
    public static final Dimension WINDOW_SIZE_LARGE = new Dimension(900, 700);
    public static final Dimension WINDOW_SIZE_DASHBOARD = new Dimension(1000, 700);
}
