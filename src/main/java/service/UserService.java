package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;

public class UserService {

    // Returns:
    //  1 -> user saved
    //  0 -> email already exists
    // -1 -> database error
    public static int saveUser(User user) {
        try {
            if (UserDAO.isExist(user.getEmail())) {
                return 0; // already exists
            } else {
                return UserDAO.addUser(user); // should return 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // error
        }
    }
}
