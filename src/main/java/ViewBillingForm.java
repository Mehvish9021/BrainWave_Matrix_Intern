import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewBillingForm extends JFrame {
    private JComboBox<String> patientBox;
    private JTextArea billArea;
    private JButton loadButton;

    public ViewBillingForm() {
        setTitle("View Billing Records");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JLabel patientLabel = new JLabel("Select Patient:");
        patientBox = new JComboBox<>(getPatientList());
        loadButton = new JButton("Load Bills");

        topPanel.add(patientLabel);
        topPanel.add(patientBox);
        topPanel.add(loadButton);

        billArea = new JTextArea();
        billArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadBills());

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

    private void loadBills() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String patient = patientBox.getSelectedItem().toString().split(" - ")[0];
            String query = "SELECT * FROM billing WHERE patient_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(patient));
            ResultSet rs = pst.executeQuery();

            StringBuilder bills = new StringBuilder("Billing Records:\n\n");
            while (rs.next()) {
                bills.append("Bill ID: ").append(rs.getInt("bill_id")).append("\n")
                        .append("Amount: $").append(rs.getDouble("amount")).append("\n")
                        .append("Payment Status: ").append(rs.getString("payment_status")).append("\n")
                        .append("Payment Method: ").append(rs.getString("payment_method")).append("\n")
                        .append("Date: ").append(rs.getString("bill_date")).append("\n\n");
            }

            billArea.setText(bills.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
