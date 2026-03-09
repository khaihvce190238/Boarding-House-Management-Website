package DALs;

import Models.Notification;
import Utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

    // =============================
    // CREATE
    // =============================
    public void insertNotification(Notification n) {

        String sql = "INSERT INTO notification(created_by,title,content,target_contract_id,is_deleted) "
                + "VALUES(?,?,?,?,0)";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, n.getCreatedBy());
            ps.setString(2, n.getTitle());
            ps.setString(3, n.getContent());

            if (n.getTargetContractId() == null)
                ps.setNull(4, Types.INTEGER);
            else
                ps.setInt(4, n.getTargetContractId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================
    // GET ALL
    // =============================
    public List<Notification> getAllNotifications() {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification "
                + "WHERE is_deleted = 0 "
                + "ORDER BY created_at DESC";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // GET BY ID
    // =============================
    public Notification getNotificationById(int id) {

        String sql = "SELECT * FROM notification WHERE notification_id = ?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapNotification(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =============================
    // GET BY CONTRACT
    // =============================
    public List<Notification> getNotificationByContract(int contractId) {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification "
                + "WHERE is_deleted = 0 "
                + "AND (target_contract_id IS NULL OR target_contract_id = ?) "
                + "ORDER BY created_at DESC";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, contractId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // GET BY USER
    // =============================
    public List<Notification> getNotificationByUser(int userId) {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification "
                + "WHERE created_by = ? "
                + "AND is_deleted = 0 "
                + "ORDER BY created_at DESC";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // SEARCH
    // =============================
    public List<Notification> searchNotification(String keyword) {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification "
                + "WHERE is_deleted = 0 "
                + "AND (title LIKE ? OR content LIKE ?) "
                + "ORDER BY created_at DESC";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // UPDATE
    // =============================
    public void updateNotification(int id, Notification n) {

        String sql = "UPDATE notification "
                + "SET title = ?, content = ?, target_contract_id = ? "
                + "WHERE notification_id = ?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());

            if (n.getTargetContractId() == null)
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, n.getTargetContractId());

            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================
    // DELETE (SOFT DELETE)
    // =============================
    public void deleteNotification(int id) {

        String sql = "UPDATE notification "
                + "SET is_deleted = 1 "
                + "WHERE notification_id = ?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================
    // COUNT
    // =============================
    public int countNotifications() {

        String sql = "SELECT COUNT(*) FROM notification WHERE is_deleted = 0";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =============================
    // MAP RESULTSET
    // =============================
    private Notification mapNotification(ResultSet rs) throws SQLException {

        Notification n = new Notification();

        n.setCreatedBy(rs.getInt("created_by"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));

        Object target = rs.getObject("target_contract_id");

        if (target != null)
            n.setTargetContractId((Integer) target);

        n.setIsDeleted(rs.getBoolean("is_deleted"));

        return n;
    }
}