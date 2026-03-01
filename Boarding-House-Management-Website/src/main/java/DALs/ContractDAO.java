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

    // 🔹 Map ResultSet → Contract
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
}
