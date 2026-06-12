package Vaileleheights.ui;

import Vaileleheights.dao.AdminDAO;
import Vaileleheights.dao.AdminDAOImpl;
import Vaileleheights.model.Admin;
import Vaileleheights.model.Booking;
import Vaileleheights.model.DoubleRoom;
import Vaileleheights.model.Guest;
import Vaileleheights.model.Room;
import Vaileleheights.model.SingleRoom;
import Vaileleheights.model.Suite;
import Vaileleheights.service.Hotel;
import Vaileleheights.util.DatabaseConnection;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    // Colour scheme
    private static final Color DARK_NAVY    = new Color(15, 30, 60);
    private static final Color GOLD         = new Color(212, 175, 55);
    private static final Color SOFT_WHITE   = new Color(245, 245, 245);
    private static final Color DEEP_GREEN   = new Color(34, 100, 65);
    private static final Color DEEP_RED     = new Color(150, 40, 40);
    private static final Color LIGHT_GREY   = new Color(230, 235, 240);
    private static final Color DARK_GREY    = new Color(60, 60, 60);

    private Hotel hotel;
    private Admin admin;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int adminAttempts = 0;
    private static final int MAX_ADMIN_ATTEMPTS = 3;

    // Panels
    private JPanel loginPanel;
    private JPanel guestLoginPanel;
    private JPanel adminLoginPanel;
    private JPanel guestMenuPanel;
    private JPanel adminMenuPanel;

    // Guest fields
    private JTextField guestNameField;
    private JTextField guestEmailField;
    private JTextField guestPhoneField;
    private Guest currentGuest;

    // Admin fields
    private JPasswordField adminPasswordField;

    // Output areas
    private JTextArea outputArea;
    private JTextArea adminOutputArea;

    public MainFrame() {
        DatabaseConnection.getInstance();
        this.hotel = new Hotel("Vailele Heights");
        this.admin = new Admin("Falatugatuga", "Tuga@vaileleheights.com", "Kers123");
        hotel.loadRoomDetails();
        hotel.loadBookings();

        setTitle("Vailele Heights Hotel Booking System");
        setSize(750, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        buildLoginPanel();
        buildGuestLoginPanel();
        buildAdminLoginPanel();
        buildGuestMenuPanel();
        buildAdminMenuPanel();

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(guestLoginPanel, "GUEST_LOGIN");
        mainPanel.add(adminLoginPanel, "ADMIN_LOGIN");
        mainPanel.add(guestMenuPanel, "GUEST_MENU");
        mainPanel.add(adminMenuPanel, "ADMIN_MENU");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }

    // ===== HELPER: styled button =====
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ===== HELPER: header panel =====
    private JPanel buildHeader(String subtitle, Color color) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(color);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("VAILELE HEIGHTS");
        title.setFont(new Font("Georgia", Font.BOLD, 24));
        title.setForeground(GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(SOFT_WHITE);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        return header;
    }

    // ===== LOGIN PANEL =====
    private void buildLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(DARK_NAVY);

        loginPanel.add(buildHeader("Luxury Hotel & Suites", DARK_NAVY), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(DARK_NAVY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        JLabel welcomeLabel = new JLabel("Welcome. Please select your login.");
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        welcomeLabel.setForeground(new Color(180, 200, 220));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0;
        JButton guestBtn = styledButton("Login as Guest", DEEP_GREEN);
        guestBtn.setPreferredSize(new Dimension(180, 45));
        guestBtn.addActionListener(e -> cardLayout.show(mainPanel, "GUEST_LOGIN"));
        centerPanel.add(guestBtn, gbc);

        gbc.gridx = 1;
        JButton adminBtn = styledButton("Login as Admin", DEEP_RED);
        adminBtn.setPreferredSize(new Dimension(180, 45));
        adminBtn.addActionListener(e -> cardLayout.show(mainPanel, "ADMIN_LOGIN"));
        centerPanel.add(adminBtn, gbc);

        loginPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel footer = new JLabel("© 2026 Vailele Heights. All rights reserved.", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setForeground(new Color(100, 120, 140));
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));
        loginPanel.add(footer, BorderLayout.SOUTH);
    }

    // ===== GUEST LOGIN PANEL =====
    private void buildGuestLoginPanel() {
        guestLoginPanel = new JPanel(new BorderLayout());
        guestLoginPanel.setBackground(LIGHT_GREY);

        guestLoginPanel.add(buildHeader("Guest Login", DARK_NAVY), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(LIGHT_GREY);
        formPanel.setBorder(new EmptyBorder(20, 60, 20, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        guestNameField = new JTextField(22);
        guestNameField.setFont(fieldFont);
        formPanel.add(guestNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        guestEmailField = new JTextField(22);
        guestEmailField.setFont(fieldFont);
        formPanel.add(guestEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(labelFont);
        formPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        guestPhoneField = new JTextField(22);
        guestPhoneField.setFont(fieldFont);
        formPanel.add(guestPhoneField, gbc);

        guestLoginPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(LIGHT_GREY);
        JButton backBtn = styledButton("← Back", DARK_GREY);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        JButton loginBtn = styledButton("Login →", DEEP_GREEN);
        loginBtn.addActionListener(e -> handleGuestLogin());
        btnPanel.add(backBtn);
        btnPanel.add(loginBtn);
        guestLoginPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleGuestLogin() {
        String name = guestNameField.getText().trim();
        String email = guestEmailField.getText().trim();
        String phone = guestPhoneField.getText().trim();
        String emailRegex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this,
                "Invalid email format.\nPlease use a valid email like john@example.com",
                "Invalid Email", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!phone.matches("\\+?\\d{7,15}") || phone.matches("0{7,}") || phone.matches("1{7,}")) {
            JOptionPane.showMessageDialog(this,
                "Invalid phone number.\nEnter 7-15 digits, optionally starting with + for international numbers.",
                "Invalid Phone", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isReturning = hotel.getGuestDAO().isReturningGuest(email);
        if (isReturning) {
            currentGuest = hotel.getGuestDAO().getGuestByEmail(email);
            JOptionPane.showMessageDialog(this,
                "Welcome back, " + currentGuest.getName() + "!\nGreat to see you again.",
                "Returning Guest", JOptionPane.INFORMATION_MESSAGE);
        } else {
            currentGuest = new Guest(name, email, phone);
            JOptionPane.showMessageDialog(this,
                "Welcome, " + currentGuest.getName() + "!\nThank you for choosing Vailele Heights.",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);
        }

        refreshGuestMenu(isReturning);
        cardLayout.show(mainPanel, "GUEST_MENU");
    }

    // ===== ADMIN LOGIN PANEL =====
    private void buildAdminLoginPanel() {
        adminLoginPanel = new JPanel(new BorderLayout());
        adminLoginPanel.setBackground(LIGHT_GREY);

        adminLoginPanel.add(buildHeader("Admin Login", DARK_NAVY), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(LIGHT_GREY);
        formPanel.setBorder(new EmptyBorder(30, 80, 30, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel passLabel = new JLabel("Admin Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        adminPasswordField = new JPasswordField(22);
        adminPasswordField.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(adminPasswordField, gbc);

        adminLoginPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(LIGHT_GREY);
        JButton backBtn = styledButton("← Back", DARK_GREY);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        JButton loginBtn = styledButton("Login →", DEEP_RED);
        loginBtn.addActionListener(e -> handleAdminLogin());
        btnPanel.add(backBtn);
        btnPanel.add(loginBtn);
        adminLoginPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleAdminLogin() {
        String password = new String(adminPasswordField.getPassword());
        Admin foundAdmin = hotel.getAdminByPassword(password);

        if (foundAdmin != null) {
            adminAttempts = 0;
            admin = foundAdmin;
            refreshAdminMenu();
            cardLayout.show(mainPanel, "ADMIN_MENU");
        } else {
            adminAttempts++;
            int remaining = MAX_ADMIN_ATTEMPTS - adminAttempts;
            if (remaining > 0) {
                JOptionPane.showMessageDialog(this,
                    "Incorrect password. " + remaining + " attempt(s) remaining.",
                    "Access Denied", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Too many failed attempts. Access locked.",
                    "Locked", JOptionPane.ERROR_MESSAGE);
                adminAttempts = 0;
                cardLayout.show(mainPanel, "LOGIN");
            }
            adminPasswordField.setText("");
        }
    }

    // ===== GUEST MENU PANEL =====
    private void buildGuestMenuPanel() {
        guestMenuPanel = new JPanel(new BorderLayout());
        guestMenuPanel.setBackground(LIGHT_GREY);

        guestMenuPanel.add(buildHeader("Guest Portal", DEEP_GREEN), BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(Color.WHITE);
        outputArea.setForeground(DARK_GREY);
        outputArea.setMargin(new Insets(15, 15, 15, 15));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        guestMenuPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        buttonPanel.setBackground(LIGHT_GREY);

        JButton viewRoomsBtn = styledButton("View Rooms", DARK_NAVY);
        viewRoomsBtn.addActionListener(e -> showAvailableRooms());
        buttonPanel.add(viewRoomsBtn);

        JButton bookBtn = styledButton("Make Booking", DEEP_GREEN);
        bookBtn.addActionListener(e -> showBookingDialog());
        buttonPanel.add(bookBtn);

        JButton cancelBtn = styledButton("Cancel Booking", DEEP_RED);
        cancelBtn.addActionListener(e -> showCancelDialog());
        buttonPanel.add(cancelBtn);

        JButton logoutBtn = styledButton("Logout", DARK_GREY);
        logoutBtn.addActionListener(e -> {
            guestNameField.setText("");
            guestEmailField.setText("");
            guestPhoneField.setText("");
            cardLayout.show(mainPanel, "LOGIN");
        });
        buttonPanel.add(logoutBtn);

        guestMenuPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshGuestMenu(boolean isReturning) {
        outputArea.setText("");
        if (isReturning) {
            outputArea.append("Welcome back, " + currentGuest.getName() + "!\n");
            outputArea.append("─────────────────────────────────────\n");
            outputArea.append("YOUR PREVIOUS BOOKINGS:\n\n");
            List<Booking> bookings = hotel.getBookingDAO().getAllBookings();
            boolean found = false;
            for (Booking b : bookings) {
                if (b.getCustomer().getEmail().equalsIgnoreCase(currentGuest.getEmail())) {
                    outputArea.append("  Booking ID : " + b.getBookingId() + "\n");
                    outputArea.append("  Room       : " + b.getRoom().getRoomType() + "\n");
                    outputArea.append("  Check-in   : " + b.getStartDate() + "\n");
                    outputArea.append("  Check-out  : " + b.getEndDate() + "\n");
                    outputArea.append("─────────────────────────────────────\n");
                    found = true;
                }
            }
            if (!found) outputArea.append("  No previous bookings found.\n");
        } else {
            outputArea.append("Welcome, " + currentGuest.getName() + "!\n");
            outputArea.append("─────────────────────────────────────\n");
            outputArea.append("Thank you for choosing Vailele Heights.\n\n");
            outputArea.append("Use the buttons below to:\n");
            outputArea.append("  • View available rooms\n");
            outputArea.append("  • Make a booking\n");
            outputArea.append("  • Cancel an existing booking\n");
        }
    }

    private void showAvailableRooms() {
        outputArea.setText("AVAILABLE ROOMS\n");
        outputArea.append("─────────────────────────────────────\n\n");
        List<Room> rooms = hotel.getRoomDAO().getAllRooms();
        if (rooms.isEmpty()) {
            outputArea.append("No rooms found.\n");
        } else {
            for (Room room : rooms) {
                outputArea.append("  " + room.getRoomInfo() + "\n");
            }
        }
    }

    private void showBookingDialog() {
        JTextField roomTypeField = new JTextField();
        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();

        Object[] fields = {
            "Room Type (Single/Double/Suite):", roomTypeField,
            "Check-in Date (YYYY-MM-DD):", checkInField,
            "Check-out Date (YYYY-MM-DD):", checkOutField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
            "Make a Booking", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String roomType = roomTypeField.getText().trim();
            String startDate = checkInField.getText().trim();
            String endDate = checkOutField.getText().trim();

            if (roomType.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            Room room = hotel.checkAvailability(roomType, startDate, endDate);
            if (room == null) {
                JOptionPane.showMessageDialog(this,
                    "No " + roomType + " rooms available for those dates.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double total = hotel.calculateTotalPrice(startDate, endDate, room.getPrice());
            long nights = hotel.calculateDays(startDate, endDate);

            int confirm = JOptionPane.showConfirmDialog(this,
                "Room: " + room.getRoomInfo() + "\n" +
                "Check-in: " + startDate + "\n" +
                "Check-out: " + endDate + "\n" +
                "Duration: " + nights + " night(s)\n" +
                "Total: $" + total + "\n\nConfirm booking?",
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Booking booking = hotel.makeBooking(currentGuest, roomType, startDate, endDate);
                if (booking != null) {
                    outputArea.setText("BOOKING CONFIRMED\n");
                    outputArea.append("─────────────────────────────────────\n");
                    outputArea.append("  Booking ID : " + booking.getBookingId() + "\n");
                    outputArea.append("  Room       : " + room.getRoomInfo() + "\n");
                    outputArea.append("  Check-in   : " + startDate + "\n");
                    outputArea.append("  Check-out  : " + endDate + "\n");
                    outputArea.append("  Duration   : " + nights + " night(s)\n");
                    outputArea.append("  Total      : $" + total + "\n");
                    outputArea.append("─────────────────────────────────────\n");
                    outputArea.append("  Please keep your Booking ID safe.\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Booking failed. Please try again.");
                }
            }
        }
    }

    private void showCancelDialog() {
        String bookingId = JOptionPane.showInputDialog(this,
            "Enter your Booking ID to cancel:");
        if (bookingId != null && !bookingId.trim().isEmpty()) {
            if (hotel.cancelBooking(bookingId.trim(), currentGuest)) {
                outputArea.setText("BOOKING CANCELLED\n");
                outputArea.append("─────────────────────────────────────\n");
                outputArea.append("  Booking " + bookingId + " has been cancelled.\n");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Booking not found or access denied.\nYou can only cancel your own bookings.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== ADMIN MENU PANEL =====
    private void buildAdminMenuPanel() {
    adminMenuPanel = new JPanel(new BorderLayout());
    adminMenuPanel.setBackground(LIGHT_GREY);

    adminMenuPanel.add(buildHeader("Admin Portal", DEEP_RED), BorderLayout.NORTH);

    adminOutputArea = new JTextArea();
    adminOutputArea.setEditable(false);
    adminOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
    adminOutputArea.setBackground(Color.WHITE);
    adminOutputArea.setForeground(DARK_GREY);
    adminOutputArea.setMargin(new Insets(15, 15, 15, 15));
    JScrollPane scrollPane = new JScrollPane(adminOutputArea);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    adminMenuPanel.add(scrollPane, BorderLayout.CENTER);

    // Clean grid button panel
    JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 8, 8));
    buttonPanel.setBackground(LIGHT_GREY);
    buttonPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

    JButton viewRoomsBtn = styledButton("All Rooms", DARK_NAVY);
    viewRoomsBtn.addActionListener(e -> {
        adminOutputArea.setText("ALL ROOMS\n");
        adminOutputArea.append("─────────────────────────────────────\n\n");
        List<Room> rooms = hotel.getRoomDAO().getAllRooms();
        for (Room room : rooms) {
            adminOutputArea.append("  " + room.getRoomInfo() + "\n");
        }
    });
    buttonPanel.add(viewRoomsBtn);

    JButton viewBookingsBtn = styledButton("All Bookings", DARK_NAVY);
    viewBookingsBtn.addActionListener(e -> {
        adminOutputArea.setText("ALL BOOKINGS\n");
        adminOutputArea.append("─────────────────────────────────────\n\n");
        List<Booking> bookings = hotel.getBookingDAO().getAllBookings();
        if (bookings.isEmpty()) {
            adminOutputArea.append("  No bookings found.\n");
        } else {
            for (Booking b : bookings) {
                adminOutputArea.append("  ID     : " + b.getBookingId() + "\n");
                adminOutputArea.append("  Guest  : " + b.getCustomer().getName() + "\n");
                adminOutputArea.append("  Room   : " + b.getRoom().getRoomType() + "\n");
                adminOutputArea.append("  Dates  : " + b.getStartDate() + " to " + b.getEndDate() + "\n");
                adminOutputArea.append("─────────────────────────────────────\n");
            }
        }
    });
    buttonPanel.add(viewBookingsBtn);

    JButton addRoomBtn = styledButton("Add Room", DEEP_GREEN);
    addRoomBtn.addActionListener(e -> showAddRoomDialog());
    buttonPanel.add(addRoomBtn);

    JButton removeRoomBtn = styledButton("Remove Room", DEEP_RED);
    removeRoomBtn.addActionListener(e -> showRemoveRoomDialog());
    buttonPanel.add(removeRoomBtn);

    JButton cancelBookingBtn = styledButton("Cancel Booking", DEEP_RED);
    cancelBookingBtn.addActionListener(e -> showAdminCancelDialog());
    buttonPanel.add(cancelBookingBtn);

    JButton addAdminBtn = styledButton("Add Admin", DARK_GREY);
    addAdminBtn.addActionListener(e -> showAddAdminDialog());
    buttonPanel.add(addAdminBtn);

    JButton logoutBtn = styledButton("Logout", DARK_GREY);
    logoutBtn.addActionListener(e -> {
        adminPasswordField.setText("");
        adminAttempts = 0;
        cardLayout.show(mainPanel, "LOGIN");
    });
    buttonPanel.add(logoutBtn);

    // Empty placeholder to fill the 8th grid cell
    JPanel placeholder = new JPanel();
    placeholder.setBackground(LIGHT_GREY);
    buttonPanel.add(placeholder);

    adminMenuPanel.add(buttonPanel, BorderLayout.SOUTH);
}

    private void refreshAdminMenu() {
        adminOutputArea.setText("ADMIN PORTAL\n");
        adminOutputArea.append("─────────────────────────────────────\n\n");
        adminOutputArea.append("  Welcome, " + admin.getName() + "!\n\n");
        adminOutputArea.append("  Use the buttons below to manage\n");
        adminOutputArea.append("  the Vailele Heights system.\n");
    }

    private void showAddRoomDialog() {
        JTextField roomNumberField = new JTextField();
        JTextField roomTypeField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] fields = {
            "Room Number:", roomNumberField,
            "Room Type (Single/Double/Suite):", roomTypeField,
            "Price per Night ($):", priceField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
            "Add New Room", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
                String roomType = roomTypeField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());

                Room room;
                switch (roomType.toLowerCase()) {
                    case "single": room = new SingleRoom(roomNumber, price); break;
                    case "double": room = new DoubleRoom(roomNumber, price); break;
                    case "suite":  room = new Suite(roomNumber, price); break;
                    default:
                        JOptionPane.showMessageDialog(this,
                            "Invalid room type. Use Single, Double, or Suite.");
                        return;
                }
                hotel.addRoom(room);
                adminOutputArea.setText("ROOM ADDED\n");
                adminOutputArea.append("─────────────────────────────────────\n");
                adminOutputArea.append("  " + room.getRoomInfo() + "\n");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number entered.");
            }
        }
    }

    private void showRemoveRoomDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter room number to remove:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int roomNumber = Integer.parseInt(input.trim());
                if (hotel.removeRoom(roomNumber)) {
                    adminOutputArea.setText("ROOM REMOVED\n");
                    adminOutputArea.append("─────────────────────────────────────\n");
                    adminOutputArea.append("  Room " + roomNumber + " removed successfully.\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Room not found.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid room number.");
            }
        }
    }

    private void showAdminCancelDialog() {
        String bookingId = JOptionPane.showInputDialog(this,
            "Enter Booking ID to cancel:");
        if (bookingId != null && !bookingId.trim().isEmpty()) {
            if (hotel.adminCancelBooking(bookingId.trim())) {
                adminOutputArea.setText("BOOKING CANCELLED BY ADMIN\n");
                adminOutputArea.append("─────────────────────────────────────\n");
                adminOutputArea.append("  Booking " + bookingId + " cancelled.\n");
            } else {
                JOptionPane.showMessageDialog(this, "Booking ID not found.");
            }
        }
    }

    private void showAddAdminDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
            "Name:", nameField,
            "Email:", emailField,
            "Password:", passwordField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
            "Add New Admin", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            if (hotel.getAdminDAO().adminExists(email)) {
                JOptionPane.showMessageDialog(this,
                    "An admin with this email already exists.");
                return;
            }

            Admin newAdmin = new Admin(name, email, password);
            hotel.getAdminDAO().addAdmin(newAdmin);
            adminOutputArea.setText("NEW ADMIN ADDED\n");
            adminOutputArea.append("─────────────────────────────────────\n");
            adminOutputArea.append("  Name  : " + name + "\n");
            adminOutputArea.append("  Email : " + email + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}