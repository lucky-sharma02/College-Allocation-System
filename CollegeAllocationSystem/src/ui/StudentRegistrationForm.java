package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class StudentRegistrationForm extends JPanel {

    private JTextField regIdField, nameField, percentileField;
    private JPasswordField passwordField;
    private JComboBox<String> pref1Combo, pref2Combo, pref3Combo;
    private Map<String, Integer> cbMap = new HashMap<>();

    public StudentRegistrationForm(boolean standalone) {
        setLayout(new GridLayout(8, 2, 10, 10)); // 8 rows

        add(new JLabel("Registration ID:"));
        regIdField = new JTextField();
        add(regIdField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Percentile:"));
        percentileField = new JTextField();
        add(percentileField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        pref1Combo = new JComboBox<>();
        pref2Combo = new JComboBox<>();
        pref3Combo = new JComboBox<>();

        add(new JLabel("Preference 1:"));
        add(pref1Combo);
        add(new JLabel("Preference 2:"));
        add(pref2Combo);
        add(new JLabel("Preference 3:"));
        add(pref3Combo);

        loadCollegeBranchOptions();

        JButton registerButton = new JButton("Register");
        add(new JLabel()); // empty label for spacing
        add(registerButton);

        registerButton.addActionListener(e -> registerStudent());

        if (standalone) {
            JFrame frame = new JFrame("Student Registration");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(this);
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null); // center the window
            frame.setVisible(true);
        }
    }

    private void loadCollegeBranchOptions() {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT cb.id, c.name AS college_name, b.name AS branch_name " +
                "FROM college_branches cb " +
                "JOIN colleges c ON cb.college_id = c.college_id " +
                "JOIN branches b ON cb.branch_id = b.branch_id"
            );

            while (rs.next()) {
                int id = rs.getInt("id");
                String label = rs.getString("college_name") + " - " + rs.getString("branch_name");
                cbMap.put(label, id);
                pref1Combo.addItem(label);
                pref2Combo.addItem(label);
                pref3Combo.addItem(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerStudent() {
        String regId = regIdField.getText().trim();
        String name = nameField.getText().trim();
        String percentileStr = percentileField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (regId.isEmpty() || name.isEmpty() || percentileStr.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            float percentile = Float.parseFloat(percentileStr);

            int pref1 = cbMap.get(pref1Combo.getSelectedItem().toString());
            int pref2 = cbMap.get(pref2Combo.getSelectedItem().toString());
            int pref3 = cbMap.get(pref3Combo.getSelectedItem().toString());

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO students (registration_id, name, percentile, password, preference_1, preference_2, preference_3) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, regId);
                ps.setString(2, name);
                ps.setFloat(3, percentile);
                ps.setString(4, password);
                ps.setInt(5, pref1);
                ps.setInt(6, pref2);
                ps.setInt(7, pref3);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student Registered Successfully!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid percentile.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration failed.");
        }
    }

    // ✅ You can run this class directly for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationForm(true));
    }
}
