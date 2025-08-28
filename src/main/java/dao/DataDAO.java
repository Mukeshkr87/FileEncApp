package dao;

import db.MyConnection;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {

    // Get all hidden files for a user
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM data WHERE email = ?");
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        List<Data> files = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String path = rs.getString("path");
            files.add(new Data(id, name, path));
        }
        return files;
    }

    // Hide a file (store in DB as BLOB)
    public static int hideFiles(Data file) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        String sql = "INSERT INTO data(name, path, email, bin_data) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        // Clean file name and path
        String filePath = file.getPath().trim();
        if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
            filePath = filePath.substring(1, filePath.length() - 1);
        }

        String fileName = file.getFileName().trim();
        if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
            fileName = fileName.substring(1, fileName.length() - 1);
        }

        File f = new File(filePath);
        if (!f.exists() || !f.isFile()) {
            System.out.println("File does not exist: " + filePath);
            return 0;
        }

        // Store file as BLOB
        try (FileInputStream fis = new FileInputStream(f)) {
            ps.setString(1, fileName);
            ps.setString(2, f.getAbsolutePath()); // store original path
            ps.setString(3, file.getEmail());
            ps.setBinaryStream(4, fis, (int) f.length());
            int ans = ps.executeUpdate();

            // Delete original file safely
            if (f.delete()) {
                System.out.println("Original file deleted successfully.");
            } else {
                System.out.println("Failed to delete original file. It may be locked by OS or OneDrive.");
            }

            System.out.println("File hidden successfully.");
            return ans;
        }
    }

    // Unhide a file (restore from DB)
    public static void unHide(int id) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT path, bin_data FROM data WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String path = rs.getString("path");

            // Clean path in case quotes were stored
            path = path.trim();
            if (path.startsWith("\"") && path.endsWith("\"")) {
                path = path.substring(1, path.length() - 1);
            }

            Blob blob = rs.getBlob("bin_data");
            try (InputStream is = blob.getBinaryStream();
                 FileOutputStream fos = new FileOutputStream(path)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            // Delete DB entry
            ps = connection.prepareStatement("DELETE FROM data WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("File successfully unhidden at: " + path);
        } else {
            System.out.println("No file found with ID: " + id);
        }
    }
}
