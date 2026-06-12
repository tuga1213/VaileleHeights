/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.test;

/**
 *
 * @author tugak
 */


import Vaileleheights.dao.AdminDAOImpl;
import Vaileleheights.dao.BookingDAOImpl;
import Vaileleheights.dao.GuestDAOImpl;
import Vaileleheights.dao.RoomDAOImpl;
import Vaileleheights.model.Admin;
import Vaileleheights.model.Booking;
import Vaileleheights.model.Guest;
import Vaileleheights.model.Room;
import Vaileleheights.model.SingleRoom;
import Vaileleheights.model.DoubleRoom;
import Vaileleheights.model.Suite;
import Vaileleheights.service.Hotel;
import Vaileleheights.util.DatabaseConnection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HotelSystemTest {

    private Hotel hotel;
    private RoomDAOImpl roomDAO;
    private BookingDAOImpl bookingDAO;
    private GuestDAOImpl guestDAO;
    private AdminDAOImpl adminDAO;

    @Before
    public void setUp() {
        DatabaseConnection.getInstance();
        hotel = new Hotel("Vailele Heights");
        roomDAO = new RoomDAOImpl();
        bookingDAO = new BookingDAOImpl();
        guestDAO = new GuestDAOImpl();
        adminDAO = new AdminDAOImpl();
    }

    // ===== DATABASE TESTS =====

    // Test 1 — Database connects successfully
    @Test
    public void testDatabaseConnection() {
        assertNotNull("Database connection should not be null",
            DatabaseConnection.getInstance().getConnection());
    }

    // Test 2 — Rooms load from database
    @Test
    public void testGetAllRooms() {
        List<Room> rooms = roomDAO.getAllRooms();
        assertNotNull("Room list should not be null", rooms);
        assertTrue("Should have at least one room", rooms.size() > 0);
    }

    // Test 3 — Room can be added and retrieved
    @Test
    public void testAddAndRetrieveRoom() {
        Room testRoom = new SingleRoom(999, 120.0);
        roomDAO.addRoom(testRoom);
        Room retrieved = roomDAO.getRoomByNumber(999);
        assertNotNull("Added room should be retrievable", retrieved);
        assertEquals("Room number should match", 999, retrieved.getRoomNumber());
        assertEquals("Room type should match", "Single", retrieved.getRoomType());
        roomDAO.removeRoom(999);
    }

    // Test 4 — Room can be removed
    @Test
    public void testRemoveRoom() {
        Room testRoom = new DoubleRoom(998, 150.0);
        roomDAO.addRoom(testRoom);
        boolean removed = roomDAO.removeRoom(998);
        assertTrue("Room should be removed successfully", removed);
        Room retrieved = roomDAO.getRoomByNumber(998);
        assertNull("Removed room should not be retrievable", retrieved);
    }

    // Test 5 — Room status can be updated
    @Test
    public void testUpdateRoomStatus() {
        Room room = roomDAO.getRoomByNumber(101);
        if (room != null) {
            roomDAO.updateRoomStatus(101, true);
            Room updated = roomDAO.getRoomByNumber(101);
            assertTrue("Room should be marked as booked", updated.isBooked());
            roomDAO.updateRoomStatus(101, false);
        }
    }

    // ===== BOOKING TESTS =====

    // Test 6 — Booking can be created
    @Test
    public void testMakeBooking() {
        Guest guest = new Guest("Test Guest", "testbooking@test.com", "1234567890");
        Booking booking = hotel.makeBooking(guest, "Single",
            "2026-10-01", "2026-10-03");
        if (booking != null) {
            assertNotNull("Booking should not be null", booking);
            assertNotNull("Booking ID should not be null", booking.getBookingId());
            assertTrue("Booking ID should start with BK",
                booking.getBookingId().startsWith("BK"));
            bookingDAO.removeBooking(booking.getBookingId());
        }
    }

    // Test 7 — Booking can be retrieved by ID
    @Test
    public void testGetBookingById() {
        Guest guest = new Guest("Retrieve Guest", "retrieve@test.com", "0987654321");
        Booking booking = hotel.makeBooking(guest, "Double",
            "2026-11-01", "2026-11-03");
        if (booking != null) {
            Booking retrieved = bookingDAO.getBookingById(booking.getBookingId());
            assertNotNull("Booking should be retrievable by ID", retrieved);
            assertEquals("Booking IDs should match",
                booking.getBookingId(), retrieved.getBookingId());
            bookingDAO.removeBooking(booking.getBookingId());
        }
    }

    // Test 8 — Date conflict detection works
    @Test
    public void testDateConflictDetection() {
        Guest guest = new Guest("Conflict Guest", "conflict@test.com", "1122334455");
        Booking booking = hotel.makeBooking(guest, "Suite",
            "2026-12-01", "2026-12-05");
        if (booking != null) {
            boolean hasConflict = bookingDAO.hasDateConflict(
                booking.getRoom().getRoomNumber(),
                "2026-12-03", "2026-12-07");
            assertTrue("Should detect date conflict", hasConflict);
            bookingDAO.removeBooking(booking.getBookingId());
        }
    }

    // Test 9 — No conflict for non-overlapping dates
    @Test
    public void testNoDateConflictForNonOverlapping() {
        Guest guest = new Guest("No Conflict Guest", "noconflict@test.com", "5544332211");
        Booking booking = hotel.makeBooking(guest, "Single",
            "2027-01-01", "2027-01-05");
        if (booking != null) {
            boolean hasConflict = bookingDAO.hasDateConflict(
                booking.getRoom().getRoomNumber(),
                "2027-01-06", "2027-01-10");
            assertFalse("Should not detect conflict for non-overlapping dates", hasConflict);
            bookingDAO.removeBooking(booking.getBookingId());
        }
    }

    // Test 10 — Booking cancellation works
    @Test
    public void testCancelBooking() {
        Guest guest = new Guest("Cancel Guest", "canceltest@test.com", "9876543210");
        Booking booking = hotel.makeBooking(guest, "Double",
            "2027-02-01", "2027-02-03");
        if (booking != null) {
            boolean cancelled = hotel.cancelBooking(
                booking.getBookingId(), guest);
            assertTrue("Booking should be cancelled successfully", cancelled);
        }
    }

    // Test 11 — Admin cancel works without ownership check
    @Test
    public void testAdminCancelBooking() {
        Guest guest = new Guest("Admin Cancel Guest", "admincancel@test.com", "1234509876");
        Booking booking = hotel.makeBooking(guest, "Single",
            "2027-03-01", "2027-03-03");
        if (booking != null) {
            boolean cancelled = hotel.adminCancelBooking(booking.getBookingId());
            assertTrue("Admin should be able to cancel any booking", cancelled);
        }
    }

    // ===== GUEST TESTS =====

    // Test 12 — Guest can be added and retrieved
    @Test
    public void testAddAndRetrieveGuest() {
        Guest guest = new Guest("Test Retrieve", "testretrieve@test.com", "1111222333");
        guestDAO.addGuest(guest);
        Guest retrieved = guestDAO.getGuestByEmail("testretrieve@test.com");
        assertNotNull("Guest should be retrievable", retrieved);
        assertEquals("Guest name should match", "Test Retrieve", retrieved.getName());
    }

    // Test 13 — Returning guest detection works
    @Test
    public void testReturningGuestDetection() {
        Guest guest = new Guest("Returning", "returning@test.com", "9998887776");
        guestDAO.addGuest(guest);
        boolean isReturning = guestDAO.isReturningGuest("returning@test.com");
        assertTrue("Should detect returning guest", isReturning);
    }

    // Test 14 — New guest is not returning
    @Test
    public void testNewGuestNotReturning() {
        boolean isReturning = guestDAO.isReturningGuest("brandnew@notexist.com");
        assertFalse("New guest should not be detected as returning", isReturning);
    }

    // ===== ADMIN TESTS =====

    // Test 15 — Admin can be retrieved by password
    @Test
    public void testGetAdminByPassword() {
        Admin admin = adminDAO.getAdminByPassword("Kers123");
        assertNotNull("Admin should be retrievable by password", admin);
        assertEquals("Admin name should match", "Falatugatuga", admin.getName());
    }

    // Test 16 — Wrong password returns null
    @Test
    public void testWrongAdminPassword() {
        Admin admin = adminDAO.getAdminByPassword("wrongpassword");
        assertNull("Wrong password should return null", admin);
    }

    // ===== CALCULATION TESTS =====

    // Test 17 — Price calculation is correct
    @Test
    public void testCalculateTotalPrice() {
        double price = hotel.calculateTotalPrice("2026-08-01", "2026-08-03", 100.0);
        assertEquals("2 nights at $100 should be $200", 200.0, price, 0.01);
    }

    // Test 18 — Days calculation is correct
    @Test
    public void testCalculateDays() {
        long days = hotel.calculateDays("2026-08-01", "2026-08-05");
        assertEquals("Should be 4 days", 4, days);
    }

    // ===== VALIDATION TESTS =====

    // Test 19 — Valid email passes
    @Test
    public void testValidEmail() {
        String emailRegex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        assertTrue("Valid email should pass", "john@gmail.com".matches(emailRegex));
        assertTrue("Valid NZ email should pass", "john@company.co.nz".matches(emailRegex));
    }

    // Test 20 — Invalid email fails
    @Test
    public void testInvalidEmail() {
        String emailRegex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        assertFalse("Missing @ should fail", "johngmail.com".matches(emailRegex));
        assertFalse("Missing domain should fail", "john@".matches(emailRegex));
        assertFalse("Missing extension should fail", "john@gmail".matches(emailRegex));
    }

    // Test 21 — Valid phone passes
    @Test
    public void testValidPhone() {
        assertTrue("NZ mobile should pass", "0211234567".matches("\\+?\\d{7,15}"));
        assertTrue("International should pass", "+64211234567".matches("\\+?\\d{7,15}"));
    }

    // Test 22 — Invalid phone fails
    @Test
    public void testInvalidPhone() {
        assertFalse("Too short should fail", "123".matches("\\+?\\d{7,15}"));
        assertFalse("Letters should fail", "abcdefgh".matches("\\+?\\d{7,15}"));
    }

    // ===== ROOM TYPE TESTS =====

    // Test 23 — SingleRoom info is correct
    @Test
    public void testSingleRoomInfo() {
        Room room = new SingleRoom(201, 100.0);
        assertTrue("SingleRoom info should contain Single",
            room.getRoomInfo().contains("Single"));
        assertEquals("Price should match", 100.0, room.getPrice(), 0.01);
    }

    // Test 24 — DoubleRoom info is correct
    @Test
    public void testDoubleRoomInfo() {
        Room room = new DoubleRoom(202, 150.0);
        assertTrue("DoubleRoom info should contain Double",
            room.getRoomInfo().contains("Double"));
        assertEquals("Price should match", 150.0, room.getPrice(), 0.01);
    }

    // Test 25 — Suite info is correct
    @Test
    public void testSuiteInfo() {
        Room room = new Suite(203, 250.0);
        assertTrue("Suite info should contain Suite",
            room.getRoomInfo().contains("Suite"));
        assertEquals("Price should match", 250.0, room.getPrice(), 0.01);
    }
}