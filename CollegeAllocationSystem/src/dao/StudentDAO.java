package dao;

import db.DBConnection;
import models.Student;
import java.sql.*;

public class StudentDAO {
    public static void registerStudent(Student s) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO students (name, percentile, preference_1, preference_2, preference_3) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, s.name);
            ps.setFloat(2, s.percentile);
            ps.setInt(3, s.preference1);
            ps.setInt(4, s.preference2);
            ps.setInt(5, s.preference3);
            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
