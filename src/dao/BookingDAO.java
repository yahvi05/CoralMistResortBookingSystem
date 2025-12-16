package dao;

import models.Booking;
import db.DBConnection;

import java.sql.*;

public class BookingDAO {

    private Connection getConnection() throws Exception {
        return DBConnection.getConnection();
    }

    /* -------------------- CREATE -------------------- */
    public Booking insertBooking(Booking b) throws Exception {

        String sql = "INSERT INTO bookings(name, phone, email, checkin, checkout, time, room_type, rate, total_amount, days_staying) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = getConnection();
        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, b.getCustomerName());
        st.setString(2, b.getPhone());
        st.setString(3, b.getEmail());
        st.setDate(4, Date.valueOf(b.getCheckinDate()));
        st.setDate(5, Date.valueOf(b.getCheckoutDate()));
        st.setString(6, b.getCheckinTime());
        st.setString(7, b.getRoomType());
        st.setDouble(8, b.getPricePerDay());
        st.setDouble(9, b.getTotalAmount());
        st.setInt(10, b.getDaysStaying());

        st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            b.setBookingId(rs.getInt(1));
        }

        return b;
    }

    /* -------------------- READ ALL -------------------- */
    public ResultSet getAll() throws Exception {
        Connection con = getConnection();
        String sql = "SELECT * FROM bookings";
        PreparedStatement st = con.prepareStatement(sql);
        return st.executeQuery();
    }

    /* -------------------- SEARCH -------------------- */
    public ResultSet search(int id) throws Exception {
        Connection con = getConnection();
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        return st.executeQuery();
    }

    /* -------------------- UPDATE -------------------- */
    public boolean update(Booking b) throws Exception {

        String sql = "UPDATE bookings SET name=?, phone=?, email=?, checkin=?, checkout=?, time=?, room_type=?, rate=?, total_amount=?, days_staying=? "
                   + "WHERE booking_id=?";

        Connection con = getConnection();
        PreparedStatement st = con.prepareStatement(sql);

        st.setString(1, b.getCustomerName());
        st.setString(2, b.getPhone());
        st.setString(3, b.getEmail());
        st.setDate(4, Date.valueOf(b.getCheckinDate()));
        st.setDate(5, Date.valueOf(b.getCheckoutDate()));
        st.setString(6, b.getCheckinTime());
        st.setString(7, b.getRoomType());
        st.setDouble(8, b.getPricePerDay());
        st.setDouble(9, b.getTotalAmount());
        st.setInt(10, b.getDaysStaying());
        st.setInt(11, b.getBookingId());

        return st.executeUpdate() > 0;
    }

    /* -------------------- DELETE -------------------- */
    public boolean delete(int id) throws Exception {
        Connection con = getConnection();
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        return st.executeUpdate() > 0;
    }
}
