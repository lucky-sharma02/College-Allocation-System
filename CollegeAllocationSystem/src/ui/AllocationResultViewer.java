package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AllocationResultViewer extends JPanel {
    private JTextArea resultArea;

    public AllocationResultViewer() {
        setLayout(new BorderLayout());

        JButton loginButton = new JButton("View Allocation Result");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(loginButton, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        loginButton.addActionListener(e -> promptForCredentials());
    }

    private void promptForCredentials() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField regIdField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Registration ID:"));
        panel.add(regIdField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Student Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String regId = regIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (!regId.isEmpty() && !password.isEmpty()) {
                showAllocationResult(regId, password);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both Registration ID and Password.");
            }
        }
    }

    private void showAllocationResult(String regId, String password) {
        try (Connection con = DBConnection.getConnection()) {
            // SQL query to get all the necessary details for the student
        	PreparedStatement ps = con.prepareStatement(
        		    "SELECT s.student_id, s.registration_id, s.name, s.percentile, " +
        		    "s.preference_1, s.preference_2, s.preference_3, s.allocated_cb_id, " +

        		    "c1.name AS allocated_college, b1.name AS allocated_branch, " +

        		    "c2.name AS preference_1_college, b2.name AS preference_1_branch, " +
        		    "c3.name AS preference_2_college, b3.name AS preference_2_branch, " +
        		    "c4.name AS preference_3_college, b4.name AS preference_3_branch " +

        		    "FROM students s " +

        		    "LEFT JOIN college_branches cb1 ON s.allocated_cb_id = cb1.id " +
        		    "LEFT JOIN colleges c1 ON cb1.college_id = c1.college_id " +
        		    "LEFT JOIN branches b1 ON cb1.branch_id = b1.branch_id " +

        		    "LEFT JOIN college_branches cb2 ON s.preference_1 = cb2.id " +
        		    "LEFT JOIN colleges c2 ON cb2.college_id = c2.college_id " +
        		    "LEFT JOIN branches b2 ON cb2.branch_id = b2.branch_id " +

        		    "LEFT JOIN college_branches cb3 ON s.preference_2 = cb3.id " +
        		    "LEFT JOIN colleges c3 ON cb3.college_id = c3.college_id " +
        		    "LEFT JOIN branches b3 ON cb3.branch_id = b3.branch_id " +

        		    "LEFT JOIN college_branches cb4 ON s.preference_3 = cb4.id " +
        		    "LEFT JOIN colleges c4 ON cb4.college_id = c4.college_id " +
        		    "LEFT JOIN branches b4 ON cb4.branch_id = b4.branch_id " +

        		    "WHERE s.registration_id = ? AND s.password = ?"
        		);

            ps.setString(1, regId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Build the report for the student in a formatted manner
                StringBuilder sb = new StringBuilder();
                sb.append("=========== Allocation Report ===========\n\n");
                sb.append("Student ID: ").append(rs.getString("student_id")).append("\n");
                sb.append("Registration ID: ").append(rs.getString("registration_id")).append("\n");
                sb.append("Name: ").append(rs.getString("name")).append("\n");
                sb.append("Percentile: ").append(rs.getFloat("percentile")).append("\n");
                sb.append("\nPreferences:\n");
                
                // Show preferences with college name and branch
                sb.append("  1. ").append(rs.getString("preference_1")).append(" - ")
                  .append(rs.getString("preference_1_college")).append(" - ")
                  .append(rs.getString("preference_1_branch")).append("\n");
                sb.append("  2. ").append(rs.getString("preference_2")).append(" - ")
                  .append(rs.getString("preference_2_college")).append(" - ")
                  .append(rs.getString("preference_2_branch")).append("\n");
                sb.append("  3. ").append(rs.getString("preference_3")).append(" - ")
                  .append(rs.getString("preference_3_college")).append(" - ")
                  .append(rs.getString("preference_3_branch")).append("\n");
                
                sb.append("\nAllocated College and Branch:\n");
                sb.append("  College: ").append(rs.getString("allocated_college")).append("\n");
                sb.append("  Branch: ").append(rs.getString("allocated_branch")).append("\n");
                sb.append("\n=========================================\n");

                // Set the result to the text area
                resultArea.setText(sb.toString());
            } else {
                resultArea.setText("Invalid credentials or allocation not done yet.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch result.");
        }
    }
}
