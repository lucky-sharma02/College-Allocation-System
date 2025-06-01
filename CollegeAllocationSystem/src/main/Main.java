package main;

import ui.StudentRegistrationForm;
import ui.AdminPanelWithLogin;
import ui.AllocationResultViewer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("College Allocation System");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(700, 500);

            JTabbedPane tabbedPane = new JTabbedPane();

            // Add the Student Registration tab
            tabbedPane.addTab("Student Registration", new StudentRegistrationForm(false));

            // Launch the Admin Panel after successful login
            JButton adminLoginButton = new JButton("Admin Login");
            adminLoginButton.addActionListener(e -> new AdminPanelWithLogin());

            JPanel adminLoginPanel = new JPanel();
            adminLoginPanel.add(adminLoginButton);

            tabbedPane.addTab("Admin Panel", adminLoginPanel); // Add a login button to launch the Admin Panel

            tabbedPane.addTab("Allocation Results", new AllocationResultViewer());

            mainFrame.add(tabbedPane);
            mainFrame.setVisible(true);
        });
    }
}
