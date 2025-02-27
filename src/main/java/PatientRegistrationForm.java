import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PatientRegistrationForm extends JFrame {
    private JTextField nameField, dobField, phoneField, emailField, addressField;
    private JComboBox<String> genderBox;
    private JTextArea medicalHistoryArea;
    private JButton registerButton;

    public PatientRegistrationForm() {
        setTitle("Patient Registration");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));

        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField();
        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        dobField = new JTextField();
        JLabel genderLabel = new JLabel("Gender:");
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        JLabel medicalHistoryLabel = new JLabel("Medical History:");
        medicalHistoryArea = new JTextArea(3, 20);

        registerButton = new JButton("Register");

        // Add components
        add(nameLabel);
        add(nameField);
        add(dobLabel);
        add(dobField);
        add(genderLabel);
        add(genderBox);
        add(phoneLabel);
        add(phoneField);
        add(emailLabel);
        add(emailField);
        add(addressLabel);
        add(addressField);
        add(medicalHistoryLabel);
        add(medicalHistoryArea);
        add(new JLabel()); // Empty space
        add(registerButton);

        registerButton.addActionListener(this::registerPatient);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerPatient(ActionEvent e) {
        String name = nameField.getText();
        String dob = dobField.getText();
        String gender = genderBox.getSelectedItem().toString();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String medicalHistory = medicalHistoryArea.getText();

        try (Connection conn = DataBaseConnection.getConnection()) {
            String query = "INSERT INTO patients (name, dob, gender, phone, email, address, medical_history) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, dob);
            pst.setString(3, gender);
            pst.setString(4, phone);
            pst.setString(5, email);
            pst.setString(6, address);
            pst.setString(7, medicalHistory);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient registered successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
