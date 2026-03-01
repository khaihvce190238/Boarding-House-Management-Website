/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huudanh
 */
import Models.Service;
import Models.ServiceUsage;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO extends DBContext {

    /* ==============================
       SERVICE (soft delete)
       ============================== */
    // 🔹 Lấy tất cả service chưa bị xóa
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM [service] "
                + "WHERE is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapService(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Lấy service theo id
    public Service getServiceById(int id) {
        String sql = "SELECT * FROM [service] "
                + "WHERE service_id = ? "
                + "AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapService(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm service
    public void insertService(Service s) {
        String sql = "INSERT INTO [service](service_name, category_id, description, image, is_deleted) "
                + "VALUES (?, ?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, s.getServiceName());
            st.setInt(2, s.getCategoryId());
            st.setString(3, s.getDescription());
            st.setString(4, s.getImage());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Update service
    public void updateService(Service s) {
        String sql = "UPDATE [service] "
                + "SET service_name = ?, category_id = ?, description = ?, image = ? "
                + "WHERE service_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, s.getServiceName());
            st.setInt(2, s.getCategoryId());
            st.setString(3, s.getDescription());
            st.setString(4, s.getImage());
            st.setInt(5, s.getServiceId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Soft delete service
    public void deleteService(int id) {
        String sql = "UPDATE [service] "
                + "SET is_deleted = 1 "
                + "WHERE service_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* ==============================
       SERVICE USAGE
       ============================== */
    // 🔹 Thêm usage
    public void insertUsage(ServiceUsage u) {
        String sql = "INSERT INTO service_usage(contract_id, service_id, quantity, usage_date, billed) "
                + "VALUES (?, ?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, u.getContractId());
            st.setInt(2, u.getServiceId());
            st.setBigDecimal(3, u.getQuantity());
            st.setDate(4, Date.valueOf(u.getUsageDate()));
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Lấy usage theo contract
    public List<ServiceUsage> getUsageByContract(int contractId) {
        List<ServiceUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM service_usage "
                + "WHERE contract_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapUsage(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Lấy usage chưa billing
    public List<ServiceUsage> getUnbilledUsage(int contractId) {
        List<ServiceUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM service_usage "
                + "WHERE contract_id = ? "
                + "AND billed = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapUsage(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Đánh dấu đã billing
    public void markAsBilled(int usageId) {
        String sql = "UPDATE service_usage "
                + "SET billed = 1 "
                + "WHERE usage_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, usageId);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* ==============================
       MAP FUNCTIONS
       ============================== */
    private Service mapService(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_id"),
                rs.getString("service_name"),
                rs.getInt("category_id"),
                rs.getString("description"),
                rs.getString("image"),
                rs.getBoolean("is_deleted")
        );
    }

    private ServiceUsage mapUsage(ResultSet rs) throws SQLException {
        return new ServiceUsage(
                rs.getInt("usage_id"),
                rs.getInt("contract_id"),
                rs.getInt("service_id"),
                rs.getBigDecimal("quantity"),
                rs.getDate("usage_date").toLocalDate(),
                rs.getBoolean("billed")
        );
    }
}//chưa có tính tiền
