import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewInventoryForm extends JFrame {
    private JTextArea inventoryArea;
    private JButton refreshButton;

    public ViewInventoryForm() {
        setTitle("View Inventory");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(inventoryArea);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadInventory());

        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        loadInventory();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadInventory() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String query = "SELECT * FROM inventory";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            StringBuilder inventoryList = new StringBuilder("Inventory Items:\n\n");
            while (rs.next()) {
                inventoryList.append("Item ID: ").append(rs.getInt("item_id")).append("\n")
                        .append("Name: ").append(rs.getString("item_name")).append("\n")
                        .append("Category: ").append(rs.getString("category")).append("\n")
                        .append("Quantity: ").append(rs.getInt("quantity")).append("\n")
                        .append("Supplier: ").append(rs.getString("supplier")).append("\n")
                        .append("Last Updated: ").append(rs.getString("last_updated")).append("\n\n");
            }

            inventoryArea.setText(inventoryList.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
