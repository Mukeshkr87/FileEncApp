package service;

import db.MyConnection;
import java.sql.Connection;

public class test {
    public static void main(String[] args) {
        try (Connection con = MyConnection.getConnection()) {
            System.out.println("Connected schema: " + con.getSchema());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
