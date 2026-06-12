/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.model;

import Vaileleheights.model.Room;
import Vaileleheights.model.Guest;

/**
 *
 * @author 
 * Falatugatuga Kerslake
 * 22181971
 */


public class Booking {
    private String bookingId;
    private Guest customer;
    private Room room;
    private String startDate;
    private String endDate;

    public Booking(Guest customer, Room room, String startDate, String endDate, String bookingId) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public Guest getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
}

