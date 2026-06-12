/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Booking;
import java.util.List;

public interface BookingDAO {
    void addBooking(Booking booking);
    boolean removeBooking(String bookingId);
    List<Booking> getAllBookings();
    Booking getBookingById(String bookingId);
}
