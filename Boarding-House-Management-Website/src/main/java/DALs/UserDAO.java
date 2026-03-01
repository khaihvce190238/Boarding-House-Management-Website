/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

import Models.User;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class UserDAO extends DBContext {

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashText = number.toString(16);

            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username, String password) {

        String sql = "SELECT * FROM Users "
                + "WHERE username = ? AND isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                String storedPassword = rs.getString("password");
                String hashedInput = md5(password);

                if (hashedInput.equalsIgnoreCase(storedPassword)) {
                    return mapUser(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean existsByUsername(String username) {

        String sql = "SELECT 1 FROM Users "
                + "WHERE username = ? AND isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean register(User u) {

        String sql = "INSERT INTO Users(username, password, fullName, email, phone, role, image, isDeleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, u.getUsername());
            st.setString(2, md5(u.getPassword())); // hash password
            st.setString(3, u.getFullName());
            st.setString(4, u.getEmail());
            st.setString(5, u.getPhone());
            st.setString(6, u.getRole());
            st.setString(7, u.getImage());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<User> getAll() {

        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public User getById(int id) {

        String sql = "SELECT * FROM Users "
                + "WHERE userId = ? AND isDeleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(User u) {

        String sql = "UPDATE Users "
                + "SET username = ?, fullName = ?, email = ?, phone = ?, role = ?, image = ? "
                + (u.getPassword() != null && !u.getPassword().isEmpty()
                ? ", password = ? "
                : "")
                + "WHERE userId = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            int index = 1;

            st.setString(index++, u.getUsername());
            st.setString(index++, u.getFullName());
            st.setString(index++, u.getEmail());
            st.setString(index++, u.getPhone());
            st.setString(index++, u.getRole());
            st.setString(index++, u.getImage());

            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                st.setString(index++, md5(u.getPassword()));
            }

            st.setInt(index, u.getUserId());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {

        String sql = "UPDATE Users SET isDeleted = 1 WHERE userId = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private User mapUser(ResultSet rs) throws SQLException {

        return new User(
                rs.getInt("userId"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("fullName"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("role"),
                rs.getString("image"),
                rs.getBoolean("isDeleted")
        );
    }
}
