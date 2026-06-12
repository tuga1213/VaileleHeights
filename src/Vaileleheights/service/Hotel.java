/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.service;

import Vaileleheights.dao.BookingDAO;
import Vaileleheights.dao.BookingDAOImpl;
import Vaileleheights.dao.GuestDAO;
import Vaileleheights.dao.GuestDAOImpl;
import Vaileleheights.dao.RoomDAO;
import Vaileleheights.dao.RoomDAOImpl;
import Vaileleheights.dao.AdminDAO;
import Vaileleheights.dao.AdminDAOImpl;
import Vaileleheights.model.Booking;
import Vaileleheights.model.DoubleRoom;
import Vaileleheights.model.Guest;
import Vaileleheights.model.Room;
import Vaileleheights.model.SingleRoom;
import Vaileleheights.model.Suite;
import Vaileleheights.model.Admin;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Hotel {

    private String name;
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;
    private GuestDAO guestDAO;
    private AdminDAO adminDAO;
    private int bookingCounter = 1;

    public Hotel(String name) {
        this.name = name;
        this.roomDAO = new RoomDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
        this.guestDAO = new GuestDAOImpl();
        this.adminDAO = new AdminDAOImpl();
    }

    // ===== LOAD DATA =====
    public void loadRoomDetails() {
        System.out.println("Rooms loaded successfully.");
    }

    public void loadBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
            String id = booking.getBookingId().replace("BK", "");
            try {
                int num = Integer.parseInt(id);
                if (num >= bookingCounter) {
                    bookingCounter = num + 1;
                }
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        System.out.println("Bookings loaded successfully.");
    }

    // ===== ROOM MANAGEMENT =====
    public void addRoom(Room room) {
        roomDAO.addRoom(room);
    }

    public boolean removeRoom(int roomNumber) {
        return roomDAO.removeRoom(roomNumber);
    }

    public void viewAvailableRooms() {
    List<Room> rooms = roomDAO.getAllRooms();
    System.out.println("\n-- All Rooms --");
    for (Room room : rooms) {
        System.out.println(room.getRoomInfo());
    }
}

    public void viewAllBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
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

    public Room checkAvailability(String roomType, String startDate, String endDate) {
    List<Room> rooms = roomDAO.getAllRooms();
    for (Room room : rooms) {
        if (room.getRoomType().equalsIgnoreCase(roomType)) {
            boolean hasConflict = bookingDAO.hasDateConflict(
                room.getRoomNumber(), startDate, endDate);
            if (!hasConflict) {
                return room;
            }
        }
    }
    return null;
}

    // ===== BOOKING MANAGEMENT =====
    public Booking makeBooking(Guest guest, String roomType, String startDate, String endDate) {
    Room room = checkAvailability(roomType, startDate, endDate);
        if (room != null) {
            // Room availability managed by date checking
            String bookingId = "BK" + String.format("%03d", bookingCounter++);
            Booking booking = new Booking(guest, room, startDate, endDate, bookingId);
            bookingDAO.addBooking(booking);
            guestDAO.addGuest(guest);
            return booking;
        }
        return null;
    }

    public boolean cancelBooking(String bookingId, Guest guest) {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking != null) {
            if (!booking.getCustomer().getName().equalsIgnoreCase(guest.getName())) {
                System.out.println("Access denied. This booking does not belong to you.");
                return false;
            }
            // Room availability managed by date checking
            bookingDAO.removeBooking(bookingId);
            return true;
        }
        return false;
    }

    public void saveBookings() {
        // Data is now saved to database automatically
        System.out.println("All data saved to database.");
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
    
   // ==== GUI CALL ====
    public RoomDAO getRoomDAO() {
        return roomDAO;
}

    public BookingDAO getBookingDAO() {
        return bookingDAO;
}

    public GuestDAO getGuestDAO() {
        return guestDAO;
}
    public AdminDAO getAdminDAO() {
    return adminDAO;
}

public Admin getAdminByPassword(String password) {
    return adminDAO.getAdminByPassword(password);
}

// Admin can cancel ANY booking without ownership check
public boolean adminCancelBooking(String bookingId) {
    Booking booking = bookingDAO.getBookingById(bookingId);
    if (booking != null) {
        bookingDAO.removeBooking(bookingId);
        return true;
    }
    return false;
}
}