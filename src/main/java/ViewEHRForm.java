
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewEHRForm extends JFrame {
    private JComboBox<String> patientBox;
    private JTextArea recordArea;
    private JButton loadButton;

    public ViewEHRForm() {
        setTitle("View EHR Records");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JLabel patientLabel = new JLabel("Select Patient:");
        patientBox = new JComboBox<>(getPatientList());
        loadButton = new JButton("Load Records");

        topPanel.add(patientLabel);
        topPanel.add(patientBox);
        topPanel.add(loadButton);

        recordArea = new JTextArea();
        recordArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recordArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadRecords());

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

    private void loadRecords() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String patient = patientBox.getSelectedItem().toString().split(" - ")[0];
            String query = "SELECT * FROM ehr WHERE patient_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(patient));
            ResultSet rs = pst.executeQuery();

            StringBuilder records = new StringBuilder("Medical History:\n\n");
            while (rs.next()) {
                records.append("Date: ").append(rs.getString("visit_date")).append("\n")
                        .append("Diagnosis: ").append(rs.getString("diagnosis")).append("\n")
                        .append("Treatment: ").append(rs.getString("treatment")).append("\n")
                        .append("Prescription: ").append(rs.getString("prescription")).append("\n\n");
            }

            recordArea.setText(records.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
