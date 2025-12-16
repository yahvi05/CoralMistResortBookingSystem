package services;

import dao.BookingDAO;
import models.Booking;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    BookingDAO dao = new BookingDAO();

    public Booking createBooking(String name, String phone, String email,
                                 LocalDate in, LocalDate out, String time,
                                 String type, double rate) throws Exception {

        Booking b = new Booking(name, phone, email, in, out, time, type, rate);
        return dao.insertBooking(b);
    }

    public List<Booking> listAll() throws Exception {

        List<Booking> list = new ArrayList<>();
        ResultSet rs = dao.getAll();

        while (rs.next()) {

            list.add(new Booking(
                rs.getInt("booking_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getDate("checkin").toLocalDate(),
                rs.getDate("checkout").toLocalDate(),
                rs.getString("time"),
                rs.getString("room_type"),
                rs.getDouble("rate"),
                rs.getDouble("total_amount"),
                rs.getInt("days_staying")
            ));
        }

        return list;
    }

    public Booking searchBooking(int id) throws Exception {

        ResultSet rs = dao.search(id);

        if (rs.next()) {
            return new Booking(
                rs.getInt("booking_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getDate("checkin").toLocalDate(),
                rs.getDate("checkout").toLocalDate(),
                rs.getString("time"),
                rs.getString("room_type"),
                rs.getDouble("rate"),
                rs.getDouble("total_amount"),
                rs.getInt("days_staying")
            );
        }
        return null;
    }

    public boolean deleteBooking(int id) throws Exception {
        return dao.delete(id);
    }

    public boolean updateBooking(Booking b) throws Exception {
        return dao.update(b);
    }
}
