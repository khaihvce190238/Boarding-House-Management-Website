/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huuda
 */
import Models.Room;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomDAO extends DBContext {

    // 🔹 Lấy tất cả phòng chưa bị xóa
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapRoom(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Lấy phòng theo ID
    public Room getRoomById(int id) {
        String sql = "SELECT * FROM room WHERE room_id = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapRoom(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm phòng
    public void insertRoom(Room r) {
        String sql = "INSERT INTO room(room_number, status, image, is_deleted)"
                + "VALUES (?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, r.getRoomNumber());
            st.setString(2, r.getStatus()); // available / occupied / maintenance
            st.setString(3, r.getImage());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Cập nhật phòng
    public void updateRoom(Room r) {

        String sql = "UPDATE room "
                + "SET room_number = ?, status = ?, image = ? "
                + "WHERE room_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, r.getRoomNumber());
            st.setString(2, r.getStatus());
            st.setString(3, r.getImage());
            st.setInt(4, r.getRoomId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Soft delete
    public void deleteRoom(int id) {
        String sql = "UPDATE room SET is_deleted = 1 WHERE room_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //hard Delete
    
    // 🔹 Cập nhật trạng thái phòng
    public void updateStatus(int roomId, String status) {
        String sql = "UPDATE room SET status = ? WHERE room_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, roomId);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //search by key
    public List<Room> searchByNumber(String keyword) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE room_number LIKE ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + keyword + "%");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapRoom(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    //get room by Status
    public List<Room> getByStatus(String status) {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM room WHERE status = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapRoom(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ── Available rooms only ─────────────────────────────────────────────────
    public List<Room> getAvailableRooms() {
        return getByStatus("available");
    }

    // ── Count rooms grouped by status ────────────────────────────────────────
    public Map<String, Integer> getCountByStatus() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("available",   0);
        counts.put("occupied",    0);
        counts.put("maintenance", 0);

        String sql = "SELECT status, COUNT(*) AS cnt FROM room WHERE is_deleted = 0 GROUP BY status";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getString("status"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counts;
    }

    // 🔹 Map ResultSet → Room
    private Room mapRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("room_id"),
                rs.getString("room_number"),
                rs.getString("status"),
                rs.getString("image"),
                rs.getBoolean("is_deleted")
        );
    }
}
