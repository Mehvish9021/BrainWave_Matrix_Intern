import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BillingForm extends JFrame {
    private JComboBox<String> patientBox, doctorBox;
    private JTextField amountField;
    private JComboBox<String> paymentStatusBox, paymentMethodBox;
    private JButton generateBillButton, viewBillsButton;

    public BillingForm() {
        setTitle("Billing & Invoicing");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        JLabel patientLabel = new JLabel("Select Patient:");
        patientBox = new JComboBox<>(getPatientList());

        JLabel doctorLabel = new JLabel("Select Doctor:");
        doctorBox = new JComboBox<>(getDoctorList());

        JLabel amountLabel = new JLabel("Amount ($):");
        amountField = new JTextField();

        JLabel paymentStatusLabel = new JLabel("Payment Status:");
        paymentStatusBox = new JComboBox<>(new String[]{"Pending", "Paid"});

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Insurance", "Bank Transfer"});

        generateBillButton = new JButton("Generate Bill");
        viewBillsButton = new JButton("View Bills");

        add(patientLabel);
        add(patientBox);
        add(doctorLabel);
        add(doctorBox);
        add(amountLabel);
        add(amountField);
        add(paymentStatusLabel);
        add(paymentStatusBox);
        add(paymentMethodLabel);
        add(paymentMethodBox);
        add(viewBillsButton);
        add(generateBillButton);

        generateBillButton.addActionListener(this::generateBill);
        viewBillsButton.addActionListener(e -> new ViewBillingForm());

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

    private void generateBill(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String patient = patientBox.getSelectedItem().toString().split(" - ")[0];
            String doctor = doctorBox.getSelectedItem().toString().split(" - ")[0];
            double amount = Double.parseDouble(amountField.getText());
            String paymentStatus = paymentStatusBox.getSelectedItem().toString();
            String paymentMethod = paymentMethodBox.getSelectedItem().toString();

            String query = "INSERT INTO billing (patient_id, doctor_id, amount, payment_status, payment_method) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(patient));
            pst.setInt(2, Integer.parseInt(doctor));
            pst.setDouble(3, amount);
            pst.setString(4, paymentStatus);
            pst.setString(5, paymentMethod);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Bill generated successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
