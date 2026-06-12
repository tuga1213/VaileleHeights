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
public class Suite extends Room {
    public Suite(int roomNumber, double pricePerNight) {
        super(roomNumber, "Suite", pricePerNight);
    }

    @Override
    public String getRoomInfo() {
        return "Suite | Room " + getRoomNumber()
             + " | $" + getPrice() + "/night | Lounge included";
    }
}

