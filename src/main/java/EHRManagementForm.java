import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EHRManagementForm extends JFrame {
    private JComboBox<String> patientBox;
    private JTextArea diagnosisArea, treatmentArea, prescriptionArea;
    private JButton saveButton, viewRecordsButton;

    public EHRManagementForm() {
        setTitle("Electronic Health Records (EHR)");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        JLabel patientLabel = new JLabel("Select Patient:");
        patientBox = new JComboBox<>(getPatientList());

        JLabel diagnosisLabel = new JLabel("Diagnosis:");
        diagnosisArea = new JTextArea(3, 20);

        JLabel treatmentLabel = new JLabel("Treatment:");
        treatmentArea = new JTextArea(3, 20);

        JLabel prescriptionLabel = new JLabel("Prescription:");
        prescriptionArea = new JTextArea(3, 20);

        saveButton = new JButton("Save Record");
        viewRecordsButton = new JButton("View Records");

        
        add(patientLabel);
        add(patientBox);
        add(diagnosisLabel);
        add(diagnosisArea);
        add(treatmentLabel);
        add(treatmentArea);
        add(prescriptionLabel);
        add(prescriptionArea);
        add(viewRecordsButton);
        add(saveButton);

        saveButton.addActionListener(this::saveMedicalRecord);
        viewRecordsButton.addActionListener(e -> new ViewEHRForm());

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

    private String[] getDropdownData(ResultSet rs) throws Exception {
        java.util.List<String> list = new java.util.ArrayList<>();
        while (rs.next()) {
            list.add(rs.getInt(1) + " - " + rs.getString(2));
        }
        return list.toArray(new String[0]);
    }

    private void saveMedicalRecord(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String patient = patientBox.getSelectedItem().toString().split(" - ")[0];
            String diagnosis = diagnosisArea.getText();
            String treatment = treatmentArea.getText();
            String prescription = prescriptionArea.getText();

            String query = "INSERT INTO ehr (patient_id, doctor_id, diagnosis, treatment, prescription) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(patient));
            pst.setInt(2, getDoctorId()); // Assume logged-in user is a doctor
            pst.setString(3, diagnosis);
            pst.setString(4, treatment);
            pst.setString(5, prescription);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Medical record saved successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getDoctorId() {
        return 1; 
    }
}

