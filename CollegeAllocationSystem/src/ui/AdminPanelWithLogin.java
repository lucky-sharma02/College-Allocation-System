package ui;

import javax.swing.*;

public class AdminPanelWithLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame adminFrame;

    public AdminPanelWithLogin() {
        // Set up the frame for login
        setTitle("Admin Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Username Label and Field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 150, 25);
        add(usernameField);

        // Password Label and Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 25);
        add(passwordField);

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 110, 100, 30);
        add(loginBtn);

        // Action listener for Login Button
        loginBtn.addActionListener(e -> validateAdmin());

        // Make the login window visible
        setVisible(true);
    }

    // Validate Admin Credentials
    private void validateAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals("Admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            showAdminPanel();
            dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.");
        }
    }

    // Show the Admin Panel after login
    private void showAdminPanel() {
        adminFrame = new JFrame("Admin Panel");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setSize(300, 200);
        adminFrame.setLocationRelativeTo(null);

        // Add the Admin Panel content
        AdminPanel adminPanel = new AdminPanel();
        adminFrame.setContentPane(adminPanel);
        adminFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanelWithLogin::new); // Launch the Admin Login window
    }
}
