package models;

import java.time.LocalDate;

public class Booking {

    private int bookingId;
    private String customerName;   // maps to SQL column: name
    private String phone;
    private String email;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String checkinTime;
    private String roomType;
    private double pricePerDay;    // maps to SQL column: rate
    private double totalAmount;
    private int daysStaying;

    // Constructor WITHOUT ID (for insert)
    public Booking(String customerName, String phone, String email,
                   LocalDate checkinDate, LocalDate checkoutDate,
                   String checkinTime, String roomType,
                   double pricePerDay) {

        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinTime = checkinTime;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;

        this.daysStaying = (int) (checkoutDate.toEpochDay() - checkinDate.toEpochDay());
        this.totalAmount = daysStaying * pricePerDay;
    }

    // Constructor WITH ID (for update/search)
    public Booking(int bookingId, String customerName, String phone, String email,
                   LocalDate checkinDate, LocalDate checkoutDate,
                   String checkinTime, String roomType,
                   double pricePerDay, double totalAmount, int daysStaying) {

        this.bookingId = bookingId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinTime = checkinTime;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.totalAmount = totalAmount;
        this.daysStaying = daysStaying;
    }

    // GETTERS + SETTERS
    public int getBookingId() { return bookingId; }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDate getCheckinDate() { return checkinDate; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public String getCheckinTime() { return checkinTime; }
    public String getRoomType() { return roomType; }
    public double getPricePerDay() { return pricePerDay; }
    public double getTotalAmount() { return totalAmount; }
    public int getDaysStaying() { return daysStaying; }
}
