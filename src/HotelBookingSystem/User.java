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
public abstract class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract String getRole();

    public String getName() { return name; }
    public String getEmail() { return email; }
}

