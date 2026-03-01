/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huudanh
 */
import Models.Bill;
import Models.BillItem;
import Utils.DBContext;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO extends DBContext {

    // 🔹 Tạo bill mới
    public int insertBill(Bill bill) {
        String sql = "INSERT INTO bill(contract_id, period, due_date, total_amount, status, is_deleted) "
                + "VALUES (?, ?, ?, 0, 'pending', 0)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, bill.getContractId());
            st.setDate(2, Date.valueOf(bill.getPeriod()));
            st.setDate(3, Date.valueOf(bill.getDueDate()));
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 🔹 Lấy bill theo contract
    public List<Bill> getBillsByContract(int contractId) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill "
                + "WHERE contract_id = ? "
                + "AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapBill(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Update status
    public void updateStatus(int billId, String status) {
        String sql = "UPDATE bill "
                + "SET status = ? "
                + "WHERE bill_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, billId);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Soft delete bill
    public void deleteBill(int billId) {
        String sql = "UPDATE bill "
                + "SET is_deleted = 1 "
                + "WHERE bill_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, billId);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Thêm bill item
    public void insertBillItem(BillItem item) {
        String sql = "INSERT INTO bill_item(bill_id, category_id, description, quantity, unit_price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, item.getBillId());
            st.setInt(2, item.getCategoryId());
            st.setString(3, item.getDescription());
            st.setBigDecimal(4, item.getQuantity());
            st.setBigDecimal(5, item.getUnitPrice());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Lấy bill item theo bill
    public List<BillItem> getItemsByBill(int billId) {
        List<BillItem> list = new ArrayList<>();
        String sql = "SELECT * FROM bill_item "
                + "WHERE bill_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, billId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapBillItem(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Tính tổng tiền và update bill
    public void updateTotalAmount(int billId) {
        String sql = "SELECT SUM(quantity * unit_price) "
                + "FROM bill_item "
                + "WHERE bill_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, billId);
            ResultSet rs = st.executeQuery();

            BigDecimal total = BigDecimal.ZERO;
            if (rs.next() && rs.getBigDecimal(1) != null) {
                total = rs.getBigDecimal(1);
            }

            String updateSql = "UPDATE bill "
                    + "SET total_amount = ? "
                    + "WHERE bill_id = ?";

            try (PreparedStatement st2 = connection.prepareStatement(updateSql)) {
                st2.setBigDecimal(1, total);
                st2.setInt(2, billId);
                st2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Bill mapBill(ResultSet rs) throws SQLException {
        return new Bill(
                rs.getInt("bill_id"),
                rs.getInt("contract_id"),
                rs.getDate("period").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getBigDecimal("total_amount"),
                rs.getString("status"),
                rs.getBoolean("is_deleted")
        );
    }

    private BillItem mapBillItem(ResultSet rs) throws SQLException {
        return new BillItem(
                rs.getInt("bill_item_id"),
                rs.getInt("bill_id"),
                rs.getInt("category_id"),
                rs.getString("description"),
                rs.getBigDecimal("quantity"),
                rs.getBigDecimal("unit_price")
        );
    }
}
