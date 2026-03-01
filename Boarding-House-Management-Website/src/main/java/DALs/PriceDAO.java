/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huudanh
 */
import Models.PriceCategory;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceDAO extends DBContext {

    // ================= CATEGORY =================
    public List<PriceCategory> getAllCategory() {
        List<PriceCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM price_category "
                + "WHERE is_deleted = 0";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapCategory(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertCategory(PriceCategory c) {
        String sql = "INSERT INTO price_category(category_code, category_type, unit, is_deleted) "
                + "VALUES (?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, c.getCategoryCode());
            st.setString(2, c.getCategoryType());
            st.setString(3, c.getUnit());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(PriceCategory c) {
        String sql = "UPDATE price_category "
                + "SET category_code = ?, category_type = ?, unit = ? "
                + "WHERE category_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, c.getCategoryCode());
            st.setString(2, c.getCategoryType());
            st.setString(3, c.getUnit());
            st.setInt(4, c.getCategoryId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int id) {
        String sql = "UPDATE price_category "
                + "SET is_deleted = 1 "
                + "WHERE category_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= PRICE HISTORY =================
    public void addNewPrice(int categoryId, BigDecimal amount, LocalDate effectiveFrom) {
        String sql = "INSERT INTO price_history(category_id, price_amount, effective_from, is_deleted) "
                + "VALUES (?, ?, ?, 0)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            st.setBigDecimal(2, amount);
            st.setDate(3, Date.valueOf(effectiveFrom));
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔥 Lấy giá hiện hành theo ngày
    public BigDecimal getCurrentPrice(int categoryId, LocalDate date) {
        String sql = "SELECT TOP 1 price_amount FROM price_history "
                + "WHERE category_id = ? "
                + "AND effective_from <= ? "
                + "AND is_deleted = 0 "
                + "ORDER BY effective_from DESC";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            st.setDate(2, Date.valueOf(date));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("price_amount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    // 🔹 Lấy toàn bộ lịch sử giá
    public List<String> getPriceHistory(int categoryId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT price_amount, effective_from FROM price_history "
                + "WHERE category_id = ? "
                + "AND is_deleted = 0 "
                + "ORDER BY effective_from DESC";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String record = rs.getDate("effective_from").toString()
                        + " - "
                        + rs.getBigDecimal("price_amount").toString();
                list.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= MAP =================
    private PriceCategory mapCategory(ResultSet rs) throws SQLException {
        return new PriceCategory(
                rs.getInt("category_id"),
                rs.getString("category_code"),
                rs.getString("category_type"),
                rs.getString("unit"),
                rs.getBoolean("is_deleted")
        );
    }
}
