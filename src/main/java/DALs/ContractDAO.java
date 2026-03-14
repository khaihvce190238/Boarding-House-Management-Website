/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author huudanh
 */
package DALs;

import Models.Contract;
import Models.ContractUser;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractDAO extends DBContext {

    // 🔹 Lấy tất cả contract chưa bị xóa
    public List<Contract> getAll() {
        List<Contract> list = new ArrayList<>();
        String sql = "SELECT * FROM contract "
                + "WHERE is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapContract(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Lấy contract theo ID
    public Contract getById(int id) {
        String sql = "SELECT * FROM contract "
                + "WHERE contract_id = ? "
                + "AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapContract(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Tạo contract mới (và cập nhật room thành occupied)
    public void insert(Contract c) {
        String sql = "INSERT INTO contract(room_id, start_date, end_date, deposit, status, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?, 0)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setInt(1, c.getRoomId());
                st.setDate(2, Date.valueOf(c.getStartDate()));
                st.setDate(3, c.getEndDate() != null ? Date.valueOf(c.getEndDate()) : null);
                st.setBigDecimal(4, c.getDeposit() != null ? c.getDeposit() : BigDecimal.ZERO);
                st.setString(5, c.getStatus());
                st.executeUpdate();
            }

            // Cập nhật phòng thành occupied
            String updateRoom = "UPDATE room "
                    + "SET status = 'occupied' "
                    + "WHERE room_id = ?";
            try (PreparedStatement st2 = connection.prepareStatement(updateRoom)) {
                st2.setInt(1, c.getRoomId());
                st2.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 🔹 Cập nhật contract
    public void update(Contract c) {
        String sql = "UPDATE contract "
                + "SET start_date = ?, end_date = ?, deposit = ?, status = ? "
                + "WHERE contract_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(c.getStartDate()));
            st.setDate(2, c.getEndDate() != null ? Date.valueOf(c.getEndDate()) : null);
            st.setBigDecimal(3, c.getDeposit());
            st.setString(4, c.getStatus());
            st.setInt(5, c.getContractId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Kết thúc hợp đồng (terminate)
    public void terminate(int contractId, int roomId) {
        String sql = "UPDATE contract "
                + "SET status = 'terminated' "
                + "WHERE contract_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setInt(1, contractId);
                st.executeUpdate();
            }

            // Trả phòng về available
            String updateRoom = "UPDATE room "
                    + "SET status = 'available' "
                    + "WHERE room_id = ?";
            try (PreparedStatement st2 = connection.prepareStatement(updateRoom)) {
                st2.setInt(1, roomId);
                st2.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ── Rich SELECT fragment ─────────────────────────────────────────────────
    private static final String RICH_SELECT =
        "SELECT c.contract_id, c.room_id, c.start_date, c.end_date, c.deposit, "
      + "       c.status, c.is_deleted, c.created_at, "
      + "       r.room_number, rc.category_name, rc.base_price, "
      + "       u.full_name AS primary_tenant_name, u.userName AS primary_tenant_username, "
      + "       (SELECT COUNT(*) FROM contract_user WHERE contract_id = c.contract_id) AS tenant_count "
      + "FROM contract c "
      + "LEFT JOIN room r ON c.room_id = r.room_id "
      + "LEFT JOIN room_category rc ON r.category_id = rc.category_id "
      + "LEFT JOIN contract_user cu_p ON cu_p.contract_id = c.contract_id AND cu_p.role = 'owner' "
      + "LEFT JOIN [user] u ON cu_p.user_id = u.user_id ";

    // 🔹 Rich list — all contracts with room + tenant info
    public List<Contract> getAllWithDetails(String statusFilter, String search) {
        List<Contract> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(RICH_SELECT)
            .append("WHERE c.is_deleted = 0 ");
        if (statusFilter != null && !statusFilter.trim().isEmpty())
            sql.append("AND c.status = ? ");
        if (search != null && !search.trim().isEmpty())
            sql.append("AND (r.room_number LIKE ? OR u.full_name LIKE ? OR u.userName LIKE ?) ");
        sql.append("ORDER BY c.contract_id DESC");

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;
            if (statusFilter != null && !statusFilter.trim().isEmpty()) st.setString(idx++, statusFilter);
            if (search != null && !search.trim().isEmpty()) {
                String kw = "%" + search.trim() + "%";
                st.setString(idx++, kw); st.setString(idx++, kw); st.setString(idx++, kw);
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) list.add(mapRich(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 🔹 Rich single contract
    public Contract getDetailById(int id) {
        String sql = RICH_SELECT + "WHERE c.contract_id = ? AND c.is_deleted = 0";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapRich(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 🔹 Co-tenants for a contract (with user info)
    public List<ContractUser> getTenantsWithInfo(int contractId) {
        List<ContractUser> list = new ArrayList<>();
        String sql = "SELECT cu.contract_id, cu.user_id, cu.role, cu.joined_at, cu.left_at, "
                   + "       u.full_name, u.userName, u.email, u.phone "
                   + "FROM contract_user cu "
                   + "JOIN [user] u ON cu.user_id = u.user_id "
                   + "WHERE cu.contract_id = ? "
                   + "ORDER BY cu.role ASC, cu.joined_at ASC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ContractUser cu = new ContractUser();
                cu.setContractId(rs.getInt("contract_id"));
                cu.setUserId(rs.getInt("user_id"));
                cu.setRole(rs.getString("role"));
                Date jd = rs.getDate("joined_at"); if (jd != null) cu.setJoinedAt(jd.toLocalDate());
                Date ld = rs.getDate("left_at");   if (ld != null) cu.setLeftAt(ld.toLocalDate());
                cu.setFullName(rs.getString("full_name"));
                cu.setUsername(rs.getString("userName"));
                cu.setEmail(rs.getString("email"));
                cu.setPhone(rs.getString("phone"));
                list.add(cu);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 🔹 Add tenant to contract
    public boolean addTenant(int contractId, int userId, String role, LocalDate joinedAt) {
        String check = "SELECT 1 FROM contract_user WHERE contract_id = ? AND user_id = ?";
        try (PreparedStatement st = connection.prepareStatement(check)) {
            st.setInt(1, contractId); st.setInt(2, userId);
            if (st.executeQuery().next()) return false; // already exists
        } catch (SQLException e) { e.printStackTrace(); return false; }

        String sql = "INSERT INTO contract_user(contract_id, user_id, role, joined_at) VALUES (?,?,?,?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            st.setInt(2, userId);
            // DB CHECK constraint: only 'owner' or 'member' allowed
            String safeRole = "owner".equals(role) ? "owner" : "member";
            st.setString(3, safeRole);
            st.setDate(4, joinedAt != null ? Date.valueOf(joinedAt) : Date.valueOf(LocalDate.now()));
            return st.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 🔹 Remove tenant (set left_at = today)
    public boolean removeTenant(int contractId, int userId) {
        String sql = "UPDATE contract_user SET left_at = ? WHERE contract_id = ? AND user_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(LocalDate.now()));
            st.setInt(2, contractId);
            st.setInt(3, userId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 🔹 Create contract + add primary tenant (transactional)
    public int insertWithTenant(Contract c, int primaryUserId) {
        String sql = "INSERT INTO contract(room_id, start_date, end_date, deposit, status, created_at, is_deleted) "
                   + "VALUES (?, ?, ?, ?, ?, GETDATE(), 0)";
        try {
            connection.setAutoCommit(false);
            int contractId = -1;

            try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                st.setInt(1, c.getRoomId());
                st.setDate(2, Date.valueOf(c.getStartDate()));
                st.setDate(3, c.getEndDate() != null ? Date.valueOf(c.getEndDate()) : null);
                st.setBigDecimal(4, c.getDeposit() != null ? c.getDeposit() : BigDecimal.ZERO);
                st.setString(5, "active");
                st.executeUpdate();
                ResultSet keys = st.getGeneratedKeys();
                if (keys.next()) contractId = keys.getInt(1);
            }

            if (contractId > 0 && primaryUserId > 0) {
                String cuSql = "INSERT INTO contract_user(contract_id, user_id, role, joined_at) VALUES (?,?,'owner',?)";
                try (PreparedStatement st2 = connection.prepareStatement(cuSql)) {
                    st2.setInt(1, contractId);
                    st2.setInt(2, primaryUserId);
                    st2.setDate(3, Date.valueOf(c.getStartDate()));
                    st2.executeUpdate();
                }
            }

            // Mark room as occupied
            try (PreparedStatement st3 = connection.prepareStatement(
                    "UPDATE room SET status = 'occupied' WHERE room_id = ?")) {
                st3.setInt(1, c.getRoomId());
                st3.executeUpdate();
            }

            connection.commit();
            return contractId;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return -1;
    }

    // 🔹 Check whether a user already has an overlapping active contract
    public boolean hasActiveContract(int userId) {
        String sql = "SELECT 1 FROM contract c "
                   + "JOIN contract_user cu ON cu.contract_id = c.contract_id "
                   + "WHERE cu.user_id = ? "
                   + "AND c.status = 'active' "
                   + "AND c.is_deleted = 0 "
                   + "AND (cu.left_at IS NULL OR cu.left_at > GETDATE())";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Active contract for a given user
    public Contract getActiveContractByUserId(int userId) {
        String sql = RICH_SELECT
                   + "JOIN contract_user cu2 ON cu2.contract_id = c.contract_id AND cu2.user_id = ? "
                   + "WHERE c.status = 'active' AND c.is_deleted = 0";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapRich(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 🔹 Lấy contracts theo user_id (qua contract_user)
    public List<Contract> getContractsByUserId(int userId) {
        List<Contract> list = new ArrayList<>();
        String sql = "SELECT c.* FROM contract c "
                + "JOIN contract_user cu ON c.contract_id = cu.contract_id "
                + "WHERE cu.user_id = ? AND c.is_deleted = 0 "
                + "ORDER BY c.start_date DESC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapContract(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Soft delete
    public void delete(int id) {
        String sql = "UPDATE contract "
                + "SET is_deleted = 1 "
                + "WHERE contract_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Map ResultSet → Contract (basic columns only)
    private Contract mapContract(ResultSet rs) throws SQLException {
        return new Contract(
                rs.getInt("contract_id"),
                rs.getInt("room_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getBigDecimal("deposit"),
                rs.getString("status"),
                rs.getBoolean("is_deleted")
        );
    }

    // 🔹 Map ResultSet → Contract (rich / joined columns)
    private Contract mapRich(ResultSet rs) throws SQLException {
        Contract c = mapContract(rs);
        // extra joined fields
        try { c.setRoomNumber(rs.getString("room_number")); } catch (SQLException ignored) {}
        try { c.setCategoryName(rs.getString("category_name")); } catch (SQLException ignored) {}
        try { c.setBasePrice(rs.getBigDecimal("base_price")); } catch (SQLException ignored) {}
        try {
            Date ca = rs.getDate("created_at");
            if (ca != null) c.setCreatedAt(ca.toLocalDate());
        } catch (SQLException ignored) {}
        try { c.setPrimaryTenantName(rs.getString("primary_tenant_name")); } catch (SQLException ignored) {}
        try { c.setPrimaryTenantUsername(rs.getString("primary_tenant_username")); } catch (SQLException ignored) {}
        try { c.setTenantCount(rs.getInt("tenant_count")); } catch (SQLException ignored) {}
        return c;
    }
}
