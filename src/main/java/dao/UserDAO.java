package dao;

import db.MyConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Check if a user already exists by email
    public static boolean isExist(String email) throws SQLException {
        Connection con = MyConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT email FROM users WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // if a record exists, return true
    }

    // Add a new user
    public static int addUser(User user) throws SQLException {
        try (Connection con = MyConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO users (name, email) VALUES (?, ?)")) {

            con.setAutoCommit(true); // auto commit to save changes

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            return ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001 = unique constraint violation
                throw new SQLException("User already exists with email: " + user.getEmail(), e);
            } else {
                throw e;
            }
        }
    }

}
