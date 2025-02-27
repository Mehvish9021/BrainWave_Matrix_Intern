import javax.swing.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JButton registerPatientButton = new JButton("Register Patient");
        JButton scheduleAppointmentButton = new JButton("Schedule Appointment");
        JButton logoutButton = new JButton("Logout");

        add(registerPatientButton);
        add(scheduleAppointmentButton);
        add(logoutButton);

        registerPatientButton.addActionListener(e -> new PatientRegistrationForm());
        scheduleAppointmentButton.addActionListener(e -> new AppointmentForm());
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        JButton billingButton = new JButton("Billing & Invoicing");
        billingButton.addActionListener(e -> new BillingForm());
        add(billingButton);
        
        JButton inventoryButton = new JButton("Manage Inventory");
        inventoryButton.addActionListener(e -> new InventoryManagementForm());
        add(inventoryButton);

        JButton userManagementButton = new JButton("Manage Users");
        userManagementButton.addActionListener(e -> new UserManagementForm());
        add(userManagementButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

