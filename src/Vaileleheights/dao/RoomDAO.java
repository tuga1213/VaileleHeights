/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Room;
import java.util.List;

public interface RoomDAO {
    void addRoom(Room room);
    boolean removeRoom(int roomNumber);
    List<Room> getAllRooms();
    Room getRoomByNumber(int roomNumber);
    void updateRoomStatus(int roomNumber, boolean isBooked);
}
