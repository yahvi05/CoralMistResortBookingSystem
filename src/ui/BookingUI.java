package ui;

import models.Booking;
import services.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

/* --------------------- DATE FORMATTER --------------------- */

class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private String pattern = "yyyy-MM-dd";
    private java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(pattern);

    @Override
    public Object stringToValue(String text) throws java.text.ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws java.text.ParseException {
        if (value != null) {
            java.util.Calendar cal = (java.util.Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
}

/* ------------------------- MAIN UI ------------------------- */

public class BookingUI extends JFrame {

    BookingService service = new BookingService();

    public BookingUI() {

        setTitle("Coral Mist Resort - Booking System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton addBtn = new JButton("Add Booking");
        JButton viewBtn = new JButton("View All Bookings");
        JButton searchBtn = new JButton("Search Booking");
        JButton deleteBtn = new JButton("Delete Booking");
        JButton exitBtn = new JButton("Exit");

        addBtn.addActionListener(e -> openAddBookingForm());
        viewBtn.addActionListener(e -> openViewBookings());
        searchBtn.addActionListener(e -> openSearchForm());
        deleteBtn.addActionListener(e -> openDeleteForm());
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(searchBtn);
        panel.add(deleteBtn);
        panel.add(exitBtn);

        add(panel);
        setVisible(true);
    }

    /* --------------------- ADD BOOKING ---------------------- */

    private void openAddBookingForm() {

        JFrame form = new JFrame("Add Booking");
        form.setSize(450, 500);
        form.setLayout(new GridLayout(12, 2, 5, 5));
        form.setLocationRelativeTo(null);

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField priceField = new JTextField();

        /* -------- CHECK-IN DATE PICKER -------- */

        UtilDateModel m1 = new UtilDateModel();
        JDatePanelImpl pane1 = new JDatePanelImpl(m1, new Properties());
        JDatePickerImpl inPicker = new JDatePickerImpl(pane1, new DateLabelFormatter());

        /* -------- CHECK-OUT DATE PICKER -------- */

        UtilDateModel m2 = new UtilDateModel();
        JDatePanelImpl pane2 = new JDatePanelImpl(m2, new Properties());
        JDatePickerImpl outPicker = new JDatePickerImpl(pane2, new DateLabelFormatter());

        JButton saveBtn = new JButton("Save Booking");

        form.add(new JLabel("Customer Name:"));
        form.add(nameField);

        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Check-in Date:"));
        form.add(inPicker);

        form.add(new JLabel("Check-out Date:"));
        form.add(outPicker);

        form.add(new JLabel("Check-in Time:"));
        form.add(timeField);

        form.add(new JLabel("Room Type:"));
        form.add(typeField);

        form.add(new JLabel("Price per Day:"));
        form.add(priceField);

        form.add(new JLabel(""));
        form.add(saveBtn);

        saveBtn.addActionListener(e -> {
            try {

                LocalDate in = LocalDate.parse(inPicker.getJFormattedTextField().getText());
                LocalDate out = LocalDate.parse(outPicker.getJFormattedTextField().getText());

                Booking b = service.createBooking(
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        in,
                        out,
                        timeField.getText(),
                        typeField.getText(),
                        Double.parseDouble(priceField.getText())
                );

                JOptionPane.showMessageDialog(form, "Booking Saved!\nTotal Amount: " + b.getTotalAmount());
                form.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
            }
        });

        form.setVisible(true);
    }

    /* ----------------------- VIEW BOOKINGS ----------------------- */

    private void openViewBookings() {

        JFrame tableFrame = new JFrame("All Bookings");
        tableFrame.setSize(900, 400);
        tableFrame.setLocationRelativeTo(null);

        String[] cols = {
                "ID", "Name", "Phone", "Email",
                "Check-in", "Check-out", "Time",
                "Room Type", "Rate", "Total"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try {
            List<Booking> list = service.listAll();

            for (Booking b : list) {
                model.addRow(new Object[]{
                        b.getBookingId(),
                        b.getCustomerName(),
                        b.getPhone(),
                        b.getEmail(),
                        b.getCheckinDate(),
                        b.getCheckoutDate(),
                        b.getCheckinTime(),
                        b.getRoomType(),
                        b.getPricePerDay(),
                        b.getTotalAmount()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tableFrame, "Error: " + ex.getMessage());
        }

        JTable table = new JTable(model);
        tableFrame.add(new JScrollPane(table));
        tableFrame.setVisible(true);
    }

    /* ----------------------- SEARCH BOOKING ----------------------- */

    private void openSearchForm() {

        String input = JOptionPane.showInputDialog("Enter Booking ID:");

        if (input == null) return;

        try {
            int id = Integer.parseInt(input);
            Booking b = service.searchBooking(id);

            if (b == null) {
                JOptionPane.showMessageDialog(null, "Booking Not Found.");
                return;
            }

            openUpdateForm(b);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    /* ----------------------- UPDATE FORM ----------------------- */

    private void openUpdateForm(Booking old) {

        JFrame form = new JFrame("Update Booking");
        form.setSize(450, 550);
        form.setLayout(new GridLayout(12, 2, 5, 5));
        form.setLocationRelativeTo(null);

        JTextField nameField = new JTextField(old.getCustomerName());
        JTextField phoneField = new JTextField(old.getPhone());
        JTextField emailField = new JTextField(old.getEmail());
        JTextField timeField = new JTextField(old.getCheckinTime());
        JTextField typeField = new JTextField(old.getRoomType());
        JTextField priceField = new JTextField("" + old.getPricePerDay());

        /* --- Check-in date picker --- */
        UtilDateModel m1 = new UtilDateModel();
        m1.setValue(java.sql.Date.valueOf(old.getCheckinDate()));
        JDatePanelImpl pane1 = new JDatePanelImpl(m1, new Properties());
        JDatePickerImpl inPicker = new JDatePickerImpl(pane1, new DateLabelFormatter());

        /* --- Check-out date picker --- */
        UtilDateModel m2 = new UtilDateModel();
        m2.setValue(java.sql.Date.valueOf(old.getCheckoutDate()));
        JDatePanelImpl pane2 = new JDatePanelImpl(m2, new Properties());
        JDatePickerImpl outPicker = new JDatePickerImpl(pane2, new DateLabelFormatter());

        JButton updateBtn = new JButton("Update");

        form.add(new JLabel("Customer Name:"));
        form.add(nameField);

        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Check-in Date:"));
        form.add(inPicker);

        form.add(new JLabel("Check-out Date:"));
        form.add(outPicker);

        form.add(new JLabel("Check-in Time:"));
        form.add(timeField);

        form.add(new JLabel("Room Type:"));
        form.add(typeField);

        form.add(new JLabel("Price per Day:"));
        form.add(priceField);

        form.add(new JLabel(""));
        form.add(updateBtn);

        updateBtn.addActionListener(e -> {
            try {

                Booking b = new Booking(
                        old.getBookingId(),
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        LocalDate.parse(inPicker.getJFormattedTextField().getText()),
                        LocalDate.parse(outPicker.getJFormattedTextField().getText()),
                        timeField.getText(),
                        typeField.getText(),
                        Double.parseDouble(priceField.getText()),
                        0, 0
                );

                service.updateBooking(b);

                JOptionPane.showMessageDialog(form, "Booking Updated Successfully!");
                form.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
            }
        });

        form.setVisible(true);
    }

    /* ----------------------- DELETE BOOKING ----------------------- */

    private void openDeleteForm() {

        String input = JOptionPane.showInputDialog("Enter Booking ID to Delete:");

        if (input == null) return;

        try {
            int id = Integer.parseInt(input);

            if (service.deleteBooking(id))
                JOptionPane.showMessageDialog(null, "Booking Deleted.");
            else
                JOptionPane.showMessageDialog(null, "Booking ID NOT found.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    /* ----------------------- START APP ----------------------- */

    public void start() {
        new BookingUI();
    }
}
