import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AppointmentForm extends JFrame {
    private JComboBox<String> patientBox, doctorBox;
    private JTextField appointmentDateField;
    private JTextArea notesArea;
    private JButton scheduleButton;

    public AppointmentForm() {
        setTitle("Schedule Appointment");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel patientLabel = new JLabel("Select Patient:");
        patientBox = new JComboBox<>(getPatientList());
        JLabel doctorLabel = new JLabel("Select Doctor:");
        doctorBox = new JComboBox<>(getDoctorList());
        JLabel dateLabel = new JLabel("Appointment Date (YYYY-MM-DD HH:MM:SS):");
        appointmentDateField = new JTextField();
        JLabel notesLabel = new JLabel("Notes:");
        notesArea = new JTextArea(3, 20);
        scheduleButton = new JButton("Schedule");

        add(patientLabel);
        add(patientBox);
        add(doctorLabel);
        add(doctorBox);
        add(dateLabel);
        add(appointmentDateField);
        add(notesLabel);
        add(notesArea);
        add(new JLabel()); // Empty space
        add(scheduleButton);

        scheduleButton.addActionListener(this::scheduleAppointment);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] getPatientList() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT patient_id, name FROM patients");
            ResultSet rs = pst.executeQuery();
            return getDropdownData(rs);
        } catch (Exception ex) {
            return new String[]{"Error loading patients"};
        }
    }

    private String[] getDoctorList() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT staff_id, name FROM staff WHERE role='Doctor'");
            ResultSet rs = pst.executeQuery();
            return getDropdownData(rs);
        } catch (Exception ex) {
            return new String[]{"Error loading doctors"};
        }
    }

    private String[] getDropdownData(ResultSet rs) throws Exception {
        java.util.List<String> list = new java.util.ArrayList<>();
        while (rs.next()) {
            list.add(rs.getInt(1) + " - " + rs.getString(2));
        }
        return list.toArray(new String[0]);
    }

    private void scheduleAppointment(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String patient = patientBox.getSelectedItem().toString().split(" - ")[0];
            String doctor = doctorBox.getSelectedItem().toString().split(" - ")[0];
            String date = appointmentDateField.getText();
            String notes = notesArea.getText();

            String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, notes) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(patient));
            pst.setInt(2, Integer.parseInt(doctor));
            pst.setString(3, date);
            pst.setString(4, notes);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
