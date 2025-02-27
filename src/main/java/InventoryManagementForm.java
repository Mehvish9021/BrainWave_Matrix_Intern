import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InventoryManagementForm extends JFrame {
    private JTextField itemNameField, quantityField, supplierField;
    private JComboBox<String> categoryBox;
    private JButton addItemButton, updateItemButton, viewInventoryButton;

    public InventoryManagementForm() {
        setTitle("Inventory Management");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel itemNameLabel = new JLabel("Item Name:");
        itemNameField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryBox = new JComboBox<>(new String[]{"Medicine", "Equipment", "Consumables", "Others"});

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        JLabel supplierLabel = new JLabel("Supplier:");
        supplierField = new JTextField();

        addItemButton = new JButton("Add Item");
        updateItemButton = new JButton("Update Quantity");
        viewInventoryButton = new JButton("View Inventory");

        add(itemNameLabel);
        add(itemNameField);
        add(categoryLabel);
        add(categoryBox);
        add(quantityLabel);
        add(quantityField);
        add(supplierLabel);
        add(supplierField);
        add(viewInventoryButton);
        add(addItemButton);
        add(updateItemButton);

        addItemButton.addActionListener(this::addItem);
        updateItemButton.addActionListener(this::updateQuantity);
        viewInventoryButton.addActionListener(e -> new ViewInventoryForm());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addItem(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String itemName = itemNameField.getText();
            String category = categoryBox.getSelectedItem().toString();
            int quantity = Integer.parseInt(quantityField.getText());
            String supplier = supplierField.getText();

            String query = "INSERT INTO inventory (item_name, category, quantity, supplier) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, itemName);
            pst.setString(2, category);
            pst.setInt(3, quantity);
            pst.setString(4, supplier);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Item added successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateQuantity(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String itemName = itemNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            String query = "UPDATE inventory SET quantity = quantity + ? WHERE item_name = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, quantity);
            pst.setString(2, itemName);
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Quantity updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
