package DALs;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author huudanh
 */
import Models.Notification;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

    public void insert(Notification n) {
        String sql = "INSERT INTO [notification](created_by, title, content, target_contract_id, is_deleted) "
                + "VALUES (?, ?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, n.getCreatedBy());
            st.setString(2, n.getTitle());
            st.setString(3, n.getContent());

            if (n.getTargetContractId() == null) {
                st.setNull(4, Types.INTEGER);
            } else {
                st.setInt(4, n.getTargetContractId());
            }

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getAll() {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM [notification] "
                + "WHERE is_deleted = 0 "
                + "ORDER BY created_at DESC";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Notification> getByContract(int contractId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM [notification] "
                + "WHERE is_deleted = 0 "
                + "AND (target_contract_id IS NULL OR target_contract_id = ?) "
                + "ORDER BY created_at DESC";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int notificationId) {
        String sql = "UPDATE [notification] "
                + "SET is_deleted = 1 "
                + "WHERE notification_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, notificationId);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Notification mapNotification(ResultSet rs) throws SQLException {

        Integer targetId = (Integer) rs.getObject("target_contract_id");

        return new Notification(
                rs.getInt("created_by"),
                rs.getString("title"),
                rs.getString("content"),
                targetId,
                rs.getBoolean("is_deleted")
        );
    }
}
