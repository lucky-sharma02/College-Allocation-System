package dao;

import java.sql.*;
import java.util.*;
import models.College;
import db.DBConnection;

public class CollegeDAO {
    public static List<College> getAllColleges() {
        List<College> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM colleges");
            while (rs.next()) {
                College c = new College();
                c.id = rs.getInt("college_id");
                c.name = rs.getString("college_name");
                c.location = rs.getString("location");
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
