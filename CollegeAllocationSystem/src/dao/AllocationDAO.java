package dao;

import db.DBConnection;
import java.sql.*;

public class AllocationDAO {
    public static void allocateSeats() {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM students ORDER BY percentile DESC");

            while (rs.next()) {
                int sid = rs.getInt("student_id");
                float percentile = rs.getFloat("percentile");

                for (int i = 1; i <= 3; i++) {
                    int pref = rs.getInt("preference_" + i);
                    PreparedStatement check = con.prepareStatement(
                        "SELECT * FROM college_branches WHERE id = ? AND cutoff_percentile <= ? AND " +
                        "(SELECT COUNT(*) FROM students WHERE allocated_cb_id = ?) < total_seats"
                    );
                    check.setInt(1, pref);
                    check.setFloat(2, percentile);
                    check.setInt(3, pref);
                    ResultSet cb = check.executeQuery();

                    if (cb.next()) {
                        PreparedStatement updateStudent = con.prepareStatement(
                            "UPDATE students SET allocated_cb_id = ? WHERE student_id = ?"
                        );
                        updateStudent.setInt(1, pref);
                        updateStudent.setInt(2, sid);
                        updateStudent.executeUpdate();

                        break; // Allocation done, move to next student
                    }
                }
            }

            // ✅ Recalculate filled seats properly
            PreparedStatement updateFilledSeats = con.prepareStatement(
                "UPDATE college_branches SET filled_seats = (" +
                "  SELECT COUNT(*) FROM students WHERE allocated_cb_id = college_branches.id" +
                ")"
            );
            updateFilledSeats.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
