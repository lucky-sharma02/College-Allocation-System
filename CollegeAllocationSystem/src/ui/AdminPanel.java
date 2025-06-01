package ui;

import javax.swing.*;
import dao.AllocationDAO;

public class AdminPanel extends JPanel {
    
    public AdminPanel() {
        setLayout(null);

        // Create "Run Allocation" button
        JButton allocateButton = new JButton("Run Allocation");
        allocateButton.setBounds(70, 50, 150, 40);
        add(allocateButton);

        // Set the action when the button is clicked
        allocateButton.addActionListener(e -> {
            // Run the allocation logic
            AllocationDAO.allocateSeats();
            JOptionPane.showMessageDialog(this, "Allocation Complete!");
        });
    }
}
