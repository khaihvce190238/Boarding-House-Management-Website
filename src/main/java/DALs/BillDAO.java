package DALs;

import Models.Bill;
import Utils.DBContext;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BillDAO extends DBContext {

    // ==============================
    // GET ALL BILLS
    // ==============================
    public List<Bill> getAllBills() {

        List<Bill> list = new ArrayList<>();

        String sql = "SELECT * FROM bill WHERE is_deleted = 0 ORDER BY bill_id DESC";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapBill(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // GET BILL BY ID
    // ==============================
    public Bill getBillById(int id) {

        String sql = "SELECT * FROM bill WHERE bill_id = ? AND is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapBill(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==============================
    // GET BILL BY TENANT
    // ==============================
    public List<Bill> getBillByTenant(int userId) {

        List<Bill> list = new ArrayList<>();

        String sql = "SELECT b.*  "
                + " FROM bill b "
                + "JOIN contract c ON b.contract_id = c.contract_id "
                + "WHERE c.user_id = ? AND b.is_deleted = 0 "
                + "ORDER BY b.bill_id DESC ";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, userId);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapBill(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // GET BILLS BY CONTRACT
    // ==============================
    public List<Bill> getBillsByContractId(int contractId) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill WHERE contract_id = ? AND is_deleted = 0 ORDER BY period DESC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) list.add(mapBill(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ==============================
    // GET UNPAID BILLS
    // ==============================
    public List<Bill> getUnpaidBills() {

        List<Bill> list = new ArrayList<>();

        String sql = "SELECT * FROM bill "
                + "WHERE status = 'unpaid' "
                + "AND is_deleted = 0";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapBill(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // INSERT BILL
    // ==============================
    public void insertBill(Bill b) {

        String sql = "INSERT INTO bill "
                + "(contract_id, period, due_date, total_amount, status, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, b.getContractId());
            st.setDate(2, Date.valueOf(b.getPeriod()));
            st.setDate(3, Date.valueOf(b.getDueDate()));
            st.setBigDecimal(4, b.getTotalAmount());
            st.setString(5, b.getStatus());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==============================
    // UPDATE BILL
    // ==============================
    public void updateBill(Bill b) {

        String sql = "UPDATE bill "
                + "SET contract_id = ?, "
                + "period = ?, "
                + "due_date = ?, "
                + "total_amount = ?, "
                + "status = ? "
                + "WHERE bill_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, b.getContractId());
            st.setDate(2, Date.valueOf(b.getPeriod()));
            st.setDate(3, Date.valueOf(b.getDueDate()));
            st.setBigDecimal(4, b.getTotalAmount());
            st.setString(5, b.getStatus());
            st.setInt(6, b.getBillId());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==============================
    // SOFT DELETE
    // ==============================
    public void deleteBill(int id) {

        String sql = "UPDATE bill SET is_deleted = 1 WHERE bill_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==============================
    // CALCULATE TOTAL BILL
    // ==============================
    public BigDecimal calculateTotal(int contractId) {

        String sql = "SELECT r.price AS room_price, "
                + "SUM(us.quantity * s.price) AS service_cost "
                + "FROM contract c "
                + "JOIN room r ON c.room_id = r.room_id "
                + "LEFT JOIN usage_service us ON us.contract_id = c.contract_id "
                + "LEFT JOIN service s ON s.service_id = us.service_id "
                + "WHERE c.contract_id = ? "
                + "GROUP BY r.price";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, contractId);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                BigDecimal room = rs.getBigDecimal("room_price");
                BigDecimal service = rs.getBigDecimal("service_cost");

                if (service == null) {
                    service = BigDecimal.ZERO;
                }

                return room.add(service);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    // ==============================
    // AUTO CREATE BILL EVERY MONTH
    // ==============================
    public void createMonthlyBill(int contractId) {

        LocalDate today = LocalDate.now();

        LocalDate period = LocalDate.of(today.getYear(), today.getMonth(), 1);

        LocalDate dueDate = period.plusDays(10);

        BigDecimal total = calculateTotal(contractId);

        Bill bill = new Bill();

        bill.setContractId(contractId);
        bill.setPeriod(period);
        bill.setDueDate(dueDate);
        bill.setTotalAmount(total);
        bill.setStatus("unpaid");

        insertBill(bill);
    }

    // ==============================
    // CHECK BILL EXISTS FOR MONTH
    // ==============================
    public boolean billExists(int contractId, LocalDate period) {

        String sql = "SELECT COUNT(*) "
                + "FROM bill "
                + "WHERE contract_id = ? "
                + "AND period = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, contractId);
            st.setDate(2, Date.valueOf(period));

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==============================
    // MAP RESULTSET -> BILL
    // ==============================
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

}
