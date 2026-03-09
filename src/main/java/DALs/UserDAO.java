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

    // ================= MD5 =================
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

    // ================= LOGIN =================
    public User login(String username, String password) {
        String sql = "SELECT * FROM [user] "
                + "WHERE userName = ? AND is_deleted = 0";

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

    // ================= CHECK USERNAME =================
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM [user] "
                + "WHERE userName = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= REGISTER =================
    public boolean register(User u) {
        String sql = "INSERT INTO [user] "
                + "(userName, password, full_name, email, phone, role, image, is_deleted) "
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

    // ================= GET ALL =================
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [user] WHERE is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= GET BY ID =================
    public User getUserById(int id) {
        String sql = "SELECT * FROM [user] "
                + "WHERE user_id = ? AND is_deleted = 0";

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

    // ================= UPDATE =================
    public boolean updateUserById(User u) {
        String sql = "UPDATE [user] "
                + "SET userName = ?, full_name = ?, email = ?, phone = ?, role = ?, image = ? "
                + (u.getPassword() != null && !u.getPassword().isEmpty()
                ? ", password = ? "
                : "")
                + "WHERE user_id = ?";

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

    // ================= DELETE (SOFT DELETE) =================
    public boolean deleteSoft(int id) {
        String sql = "UPDATE [user] SET is_deleted = 1 WHERE user_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUserById(int id) {
        String sql = "DELETE FROM [user] WHERE user_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= FIND BY USERNAME AND EMAIL (Reset Password) =================
    public User findByUsernameAndEmail(String username, String email) {
        String sql = "SELECT * FROM [user] WHERE userName = ? AND email = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= CHANGE PASSWORD (verify old first) =================
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        String selectSql = "SELECT password FROM [user] WHERE user_id = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(selectSql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String stored = rs.getString("password");
            if (!md5(oldPassword).equalsIgnoreCase(stored)) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String updateSql = "UPDATE [user] SET password = ? WHERE user_id = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(updateSql)) {
            st.setString(1, md5(newPassword));
            st.setInt(2, userId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= RESET PASSWORD (no old password check) =================
    public boolean resetPassword(int userId, String newPassword) {
        String sql = "UPDATE [user] SET password = ? WHERE user_id = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, md5(newPassword));
            st.setInt(2, userId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= MAP RESULTSET =================
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("userName"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("role"),
                rs.getString("image"),
                rs.getBoolean("is_deleted")
        );
    }
}
