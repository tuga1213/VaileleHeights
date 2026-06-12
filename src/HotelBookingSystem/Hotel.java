/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HotelBookingSystem;

/**
 *
 * @author 
 * Falatugatuga Kerslake
 * 22181971
 */
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Hotel {
    private String name;
    private List<Room> rooms;
    private List<Booking> bookings;
    private int bookingCounter = 1;

    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.bookings = new ArrayList<>();
        ensureFilesExist();
    }

    // ===== FILE I/O =====
    public void loadRoomDetails() {
        try (BufferedReader br = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    int roomNumber = Integer.parseInt(details[0].trim());
                    String roomType = details[1].trim();
                    double price = Double.parseDouble(details[2].trim());
                    boolean isBooked = Boolean.parseBoolean(details[3].trim());

                    Room room;
                    switch (roomType.toLowerCase()) {
                        case "single": room = new SingleRoom(roomNumber, price); break;
                        case "double": room = new DoubleRoom(roomNumber, price); break;
                        case "suite":  room = new Suite(roomNumber, price); break;
                        default:       room = new SingleRoom(roomNumber, price); break;
                    }
                    room.setBooked(isBooked);
                    rooms.add(room);
                }
            }
            System.out.println("Rooms loaded successfully.");
        } catch (IOException e) {
            System.out.println("No existing rooms found.");
        }
    }

    public void loadBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 9) {
                    String bookingId  = details[0].trim();
                    String guestName  = details[1].trim();
                    String guestEmail = details[2].trim();
                    String guestPhone = details[3].trim();
                    int roomNumber    = Integer.parseInt(details[4].trim());
                    String startDate  = details[7].trim();
                    String endDate    = details[8].trim();

                    Room room = getRoomByNumber(roomNumber);
                    if (room != null) {
                        Guest guest = new Guest(guestName, guestEmail, guestPhone);
                        Booking booking = new Booking(guest, room, startDate, endDate, bookingId);
                        booking.setBookingId(bookingId);
                        room.setBooked(true);
                        bookings.add(booking);
                        
                        try{
                            int num = Integer.parseInt(bookingId.replace("VH", ""));
                            if (num >= bookingCounter){
                                bookingCounter = num + 1;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
            System.out.println("Bookings loaded successfully.");
        } catch (IOException e) {
            System.out.println("No existing bookings found.");
        }
    }

    public void saveBookings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt"))) {
            for (Booking booking : bookings) {
                writer.write(
                    booking.getBookingId() + "," +
                    booking.getCustomer().getName() + "," +
                    booking.getCustomer().getEmail() + "," +
                    booking.getCustomer().getPhone() + "," +
                    booking.getRoom().getRoomNumber() + "," +
                    booking.getRoom().getRoomType() + "," +
                    booking.getRoom().getPrice() + "," +
                    booking.getStartDate() + "," +
                    booking.getEndDate()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public void saveRooms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (Room room : rooms) {
                writer.write(
                    room.getRoomNumber() + "," +
                    room.getRoomType() + "," +
                    room.getPrice() + "," +
                    room.isBooked()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    private void ensureFilesExist() {
        try {
            new File("rooms.txt").createNewFile();
            new File("customers.txt").createNewFile();
            new File("bookings.txt").createNewFile();
        } catch (IOException e) {
            System.out.println("Error ensuring files exist.");
        }
    }

    // ===== ROOM MANAGEMENT =====
    public void addRoom(Room room) {
        rooms.add(room);
        saveRooms();
    }

    public boolean removeRoom(int roomNumber) {
        boolean removed = rooms.removeIf(r -> r.getRoomNumber() == roomNumber);
        if (removed) saveRooms();
        return removed;
    }

    public void viewAvailableRooms() {
        boolean found = false;
        System.out.println("\n-- Available Rooms --");
        for (Room room : rooms) {
            if (!room.isBooked()) {
                System.out.println(room.getRoomInfo());
                found = true;
            }
        }
        if (!found) System.out.println("No rooms currently available.");
    }

    public void viewAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        System.out.println("\n-- All Bookings --");
        for (Booking booking : bookings) {
            System.out.println(
                "ID: " + booking.getBookingId() +
                " | Guest: " + booking.getCustomer().getName() +
                " | Room: " + booking.getRoom().getRoomType() +
                " | " + booking.getStartDate() +
                " to " + booking.getEndDate()
            );
        }
    }

    public Room checkAvailability(String roomType) {
        for (Room room : rooms) {
            if (room.getRoomType().equalsIgnoreCase(roomType) && !room.isBooked()) {
                return room;
            }
        }
        return null;
    }

    private Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // ===== BOOKING MANAGEMENT =====
    public Booking makeBooking(Guest guest, String roomType, String startDate, String endDate) {
        Room room = checkAvailability(roomType);
        if (room != null) {
            room.setBooked(true);
            String bookingId = "VH" + String.format("%03d", bookingCounter++);
            Booking booking = new Booking(guest, room, startDate, endDate, bookingId);
            bookings.add(booking);
            saveBookings();
            saveRooms();
            return booking;
        }
        return null;
    }

    public boolean cancelBooking(String bookingId, Guest guest) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                if (!booking.getCustomer().getName().equalsIgnoreCase(guest.getName())){
                     System.out.println("Access denied. This booking does not belong to you.");
                     return false;
                }
                booking.getRoom().setBooked(false);
                bookings.remove(booking);
                saveBookings();
                saveRooms();
                return true;
            }
        }
        return false;
    }

    // ===== CALCULATIONS =====
    public long calculateDays(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ChronoUnit.DAYS.between(start, end);
    }

    public double calculateTotalPrice(String startDate, String endDate, double pricePerNight) {
        long days = calculateDays(startDate, endDate);
        return days * pricePerNight;
    }
}




