/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huudanh
 */
import Models.RoomFacility;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomFacilityDAO extends DBContext {

    // 🔹 Lấy tất cả facility của 1 phòng
    public List<RoomFacility> getByRoomId(int roomId) {
        List<RoomFacility> list = new ArrayList<>();
        String sql = "SELECT room_id, facility_id, quantity "
                + "FROM room_facility "
                + "WHERE room_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapRoomFacility(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Thêm facility vào phòng
    public void insert(RoomFacility rf) {
        String sql = "INSERT INTO room_facility(room_id, facility_id, quantity) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, rf.getRoomId());
            st.setInt(2, rf.getFacilityId());
            st.setInt(3, rf.getQuantity());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Update quantity
    public void update(RoomFacility rf) {
        String sql = "UPDATE room_facility "
                + "SET quantity = ? "
                + "WHERE room_id = ? "
                + "AND facility_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, rf.getQuantity());
            st.setInt(2, rf.getRoomId());
            st.setInt(3, rf.getFacilityId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Xóa thật (hard delete)
    public void delete(int roomId, int facilityId) {
        String sql = "DELETE FROM room_facility "
                + "WHERE room_id = ? "
                + "AND facility_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.setInt(2, facilityId);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Kiểm tra tồn tại (để tránh lỗi trùng PK)
    public boolean exists(int roomId, int facilityId) {
        String sql = "SELECT 1 FROM room_facility "
                + "WHERE room_id = ? "
                + "AND facility_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.setInt(2, facilityId);
            ResultSet rs = st.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Map ResultSet → RoomFacility
    private RoomFacility mapRoomFacility(ResultSet rs) throws SQLException {
        return new RoomFacility(
                rs.getInt("room_id"),
                rs.getInt("facility_id"),
                rs.getInt("quantity")
        );
    }
}
