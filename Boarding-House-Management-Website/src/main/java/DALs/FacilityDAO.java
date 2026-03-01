/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huudanh
 */
import Models.Facility;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacilityDAO extends DBContext {

    // 🔹 Lấy tất cả facility chưa bị xóa
    public List<Facility> getAll() {
        List<Facility> list = new ArrayList<>();
        String sql = "SELECT * FROM facility "
                + "WHERE is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapFacility(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Lấy facility theo ID
    public Facility getById(int id) {
        String sql = "SELECT * FROM facility "
                + "WHERE facility_id = ? "
                + "AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapFacility(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm facility
    public void insert(Facility f) {
        String sql = "INSERT INTO facility(facility_name, description, image, is_deleted) "
                + "VALUES (?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, f.getFacilityName());
            st.setString(2, f.getDescription());
            st.setString(3, f.getImage());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Cập nhật facility
    public void update(Facility f) {
        String sql = "UPDATE facility "
                + "SET facility_name = ?, description = ?, image = ? "
                + "WHERE facility_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, f.getFacilityName());
            st.setString(2, f.getDescription());
            st.setString(3, f.getImage());
            st.setInt(4, f.getFacilityId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Soft delete
    public void delete(int id) {
        String sql = "UPDATE facility "
                + "SET is_deleted = 1 "
                + "WHERE facility_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Map ResultSet → Facility
    private Facility mapFacility(ResultSet rs) throws SQLException {
        return new Facility(
                rs.getInt("facility_id"),
                rs.getString("facility_name"),
                rs.getString("description"),
                rs.getString("image"),
                rs.getBoolean("is_deleted")
        );
    }
}
