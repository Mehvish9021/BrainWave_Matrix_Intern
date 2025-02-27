import javax.swing.*;
import java.awt.*;

public class DashBoard extends JFrame {
    public DashBoard() {
        setTitle("Hospital Management System - Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Hospital Management System!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel);

        JButton patientButton = new JButton("Manage Patients");
        JButton appointmentButton = new JButton("Appointments");
        JButton billingButton = new JButton("Billing");
        JButton logoutButton = new JButton("Logout");

        add(patientButton);
        add(appointmentButton);
        add(billingButton);
        add(logoutButton);

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
