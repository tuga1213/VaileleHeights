/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Vaileleheights.ui;

/**
 *
 * @author 
 * Falatugatuga Kerslake
 * 22181971
 */
import Vaileleheights.service.Hotel;
import Vaileleheights.model.Booking;
import Vaileleheights.model.DoubleRoom;
import Vaileleheights.model.Room;
import Vaileleheights.model.SingleRoom;
import Vaileleheights.model.Suite;
import Vaileleheights.model.Admin;
import Vaileleheights.model.Guest;
import java.util.Scanner;

public class HotelBookingSystem {
    private Hotel hotel;
    private Scanner scanner;
    private Admin admin;

    public HotelBookingSystem() {
        this.hotel = new Hotel("Vailele Heights");
        this.scanner = new Scanner(System.in);
        this.admin = new Admin("Falatugatuga", "Tuga@vaileleheights.com", "Kers123");
        hotel.loadRoomDetails();
        hotel.loadBookings();
    }

    public void start() {
        
        System.out.println("=============================");
        System.out.println("  Welcome to Vailele Heights Bookings ");
        System.out.println("=============================");

        while (true) {
            System.out.println("\nMain Menu");
            System.out.println("1. Login as Guest");
            System.out.println("2. Login as Admin");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = readInt();
            switch (choice) {
                case 1: guestLogin(); break;
                case 2: adminLogin(); break;
                case 3:
                    hotel.saveBookings();
                    System.out.println("Thank you for using Ulalei Bookings. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ===== GUEST =====
    private void guestLogin() {
    System.out.print("\nEnter your name: ");
    String name = scanner.nextLine();

    // Email validation
    String email;
    while (true) {
        System.out.print("Enter your email: ");
        email = scanner.nextLine();
        if (isValidEmail(email)) {
            break;
        }
        System.out.println("Invalid email. Please enter a valid email (e.g. john@email.com)");
    }

    // Phone validation
    String phone;
    while (true) {
        System.out.print("Enter your phone number: ");
        phone = scanner.nextLine().trim();
        if (phone.matches("\\d{7,15}")) {
            break;
        }
        System.out.println("Invalid phone number. Please try again.");
    }

    Guest guest = new Guest(name, email, phone);
    System.out.println("\nWelcome, " + guest.getName() + "!");
    guestMenu(guest);
}

    private void guestMenu(Guest guest) {
        while (true) {
            System.out.println("\n-- Guest Menu --");
            System.out.println("1. View available rooms");
            System.out.println("2. Make a booking");
            System.out.println("3. Cancel a booking");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = readInt();
            switch (choice) {
                case 1: hotel.viewAvailableRooms(); break;
                case 2: makeBooking(guest); break;
                case 3: cancelBooking(guest); break;
                case 4:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ===== ADMIN =====
    private void adminLogin() {
    int attempts = 0;
    int maxAttempts = 3;

    while (attempts < maxAttempts) {
        System.out.print("\nEnter admin password: ");
        String code = scanner.nextLine();

        if (admin.authenticate(code)) {
            System.out.println("Welcome, " + admin.getName() + "!");
            adminMenu();
            return;
        } else {
            attempts++;
            int remaining = maxAttempts - attempts;
            if (remaining > 0) {
                System.out.println("Incorrect password. " + remaining + " attempt(s) remaining.");
            }
        }
    }

    System.out.println("\nToo many failed attempts. System shutting down for security.");
    scanner.close();
    System.exit(0);
}

    private void adminMenu() {
        while (true) {
            System.out.println("\n-- Admin Menu --");
            System.out.println("1. View all rooms");
            System.out.println("2. Add a room");
            System.out.println("3. Remove a room");
            System.out.println("4. View all bookings");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = readInt();
            switch (choice) {
                case 1: hotel.viewAvailableRooms(); break;
                case 2: addRoom(); break;
                case 3: removeRoom(); break;
                case 4: hotel.viewAllBookings(); break;
                case 5:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ===== GUEST ACTIONS =====
    private void makeBooking(Guest guest) {
        System.out.print("\nEnter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine().trim();

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        Room room = hotel.checkAvailability(roomType);
        if (room != null) {
            double total = hotel.calculateTotalPrice(startDate, endDate, room.getPrice());
            long nights = hotel.calculateDays(startDate, endDate);
            System.out.println("Total: $" + total + " for " + nights + " night(s). Confirm? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y") || confirm.equals("yes")) {
                Booking booking = hotel.makeBooking(guest, roomType, startDate, endDate);
                if (booking != null) {
                    System.out.println("Booking confirmed! Your booking ID is: " + booking.getBookingId());
                } else {
                    System.out.println("Booking failed. Please try again.");
                }
            } else {
                System.out.println("Booking cancelled.");
            }
        } else {
            System.out.println("Sorry, no " + roomType + " rooms are available.");
        }
    }

    private void cancelBooking(Guest guest) {
        System.out.print("\nEnter your booking ID: ");
        String bookingId = scanner.nextLine().trim();

        if (hotel.cancelBooking(bookingId, guest)) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Booking ID not found or access denied.");
        }
    }

    // ===== ADMIN ACTIONS =====
    private void addRoom() {
        System.out.print("\nEnter room number: ");
        int roomNumber = readInt();

        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine().trim();

        System.out.print("Enter price per night: $");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price entered.");
            return;
        }

        Room room;
        switch (roomType.toLowerCase()) {
            case "single": room = new SingleRoom(roomNumber, price); break;
            case "double": room = new DoubleRoom(roomNumber, price); break;
            case "suite":  room = new Suite(roomNumber, price); break;
            default:
                System.out.println("Invalid room type.");
                return;
        }
        hotel.addRoom(room);
        System.out.println("Room " + roomNumber + " added successfully.");
    }

    private void removeRoom() {
        System.out.print("\nEnter room number to remove: ");
        int roomNumber = readInt();

        if (hotel.removeRoom(roomNumber)) {
            System.out.println("Room " + roomNumber + " removed successfully.");
        } else {
            System.out.println("Room not found.");
        }
    }

    // ===== HELPER =====
    private int readInt() {
    while (true) {
        try {
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Returning to menu.");
                return -1;
            }
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Please enter a valid number: ");
        }
    }
}
    
    // ==== VALIDATION ====
    private boolean isValidEmail(String email) {
          return email.contains("@") && email.contains(".")
                  && email.indexOf("@") < email.lastIndexOf(".");
    }

    public static void main(String[] args) {
        new HotelBookingSystem().start();
    }
}
