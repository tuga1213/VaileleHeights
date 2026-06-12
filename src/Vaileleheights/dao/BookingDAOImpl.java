/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Booking;
import Vaileleheights.model.Guest;
import Vaileleheights.model.Room;
import Vaileleheights.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    private Connection conn;
    private RoomDAO roomDAO;

    public BookingDAOImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
        this.roomDAO = new RoomDAOImpl();
    }

    @Override
    public void addBooking(Booking booking) {
        String sql = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, booking.getBookingId());
            ps.setString(2, booking.getCustomer().getName());
            ps.setString(3, booking.getCustomer().getEmail());
            ps.setString(4, booking.getCustomer().getPhone());
            ps.setInt(5, booking.getRoom().getRoomNumber());
            ps.setString(6, booking.getRoom().getRoomType());
            ps.setDouble(7, booking.getRoom().getPrice());
            ps.setString(8, booking.getStartDate());
            ps.setString(9, booking.getEndDate());
            ps.executeUpdate();
            System.out.println("Booking saved to database.");
        } catch (SQLException e) {
            System.out.println("Error adding booking: " + e.getMessage());
        }
    }

    @Override
    public boolean removeBooking(String bookingId) {
        String sql = "DELETE FROM bookings WHERE bookingId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error removing booking: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String bookingId  = rs.getString("bookingId");
                String guestName  = rs.getString("guestName");
                String guestEmail = rs.getString("guestEmail");
                String guestPhone = rs.getString("guestPhone");
                int roomNumber    = rs.getInt("roomNumber");
                String startDate  = rs.getString("startDate");
                String endDate    = rs.getString("endDate");

                Guest guest = new Guest(guestName, guestEmail, guestPhone);
                Room room = roomDAO.getRoomByNumber(roomNumber);

                if (room != null) {
                    Booking booking = new Booking(guest, room, startDate, endDate, bookingId);
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting bookings: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public Booking getBookingById(String bookingId) {
        String sql = "SELECT * FROM bookings WHERE bookingId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String guestName  = rs.getString("guestName");
                String guestEmail = rs.getString("guestEmail");
                String guestPhone = rs.getString("guestPhone");
                int roomNumber    = rs.getInt("roomNumber");
                String startDate  = rs.getString("startDate");
                String endDate    = rs.getString("endDate");

                Guest guest = new Guest(guestName, guestEmail, guestPhone);
                Room room = roomDAO.getRoomByNumber(roomNumber);

                if (room != null) {
                    return new Booking(guest, room, startDate, endDate, bookingId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting booking: " + e.getMessage());
        }
        return null;
    }
    @Override
public boolean hasDateConflict(int roomNumber, String startDate, String endDate) {
    String sql = "SELECT COUNT(*) FROM bookings WHERE roomNumber = ? " +
                 "AND startDate < ? AND endDate > ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, roomNumber);
        ps.setString(2, endDate);
        ps.setString(3, startDate);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.out.println("Error checking date conflict: " + e.getMessage());
    }
    return false;
}
}
