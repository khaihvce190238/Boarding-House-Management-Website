package DALs;

import Utils.DBContext;
import Models.PriceCategory;
import Models.Service;
import Models.ServiceUsage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceDAO extends DBContext {

    // =============================
    // GET ALL SERVICES
    // =============================
    public List<Service> getAllServices() {

        List<Service> list = new ArrayList<>();

        String sql = "SELECT * FROM service WHERE is_deleted = 0";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Service s = new Service();

                s.setServiceId(rs.getInt("service_id"));
                s.setServiceName(rs.getString("service_name"));
                s.setCategoryId(rs.getInt("category_id"));
                s.setDescription(rs.getString("description"));
                s.setImage(rs.getString("image"));
                s.setIsDeleted(rs.getBoolean("is_deleted"));

                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // GET SERVICE BY ID
    // =============================
    public Service getServiceById(int id) {

        String sql = "SELECT * FROM service WHERE service_id = ?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Service s = new Service();

                s.setServiceId(rs.getInt("service_id"));
                s.setServiceName(rs.getString("service_name"));
                s.setCategoryId(rs.getInt("category_id"));
                s.setDescription(rs.getString("description"));
                s.setImage(rs.getString("image"));
                s.setIsDeleted(rs.getBoolean("is_deleted"));

                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =============================
    // INSERT SERVICE
    // =============================
    public void insertService(Service s) {

        String sql = "INSERT INTO service(service_name, category_id, description, image, is_deleted) VALUES(?,?,?,?,0)";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, s.getServiceName());
            ps.setInt(2, s.getCategoryId());
            ps.setString(3, s.getDescription());
            ps.setString(4, s.getImage());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =============================
    // UPDATE SERVICE
    // =============================
    public void updateService(Service s) {

        String sql = "UPDATE service SET service_name=?, category_id=?, description=?, image=? WHERE service_id=?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, s.getServiceName());
            ps.setInt(2, s.getCategoryId());
            ps.setString(3, s.getDescription());
            ps.setString(4, s.getImage());
            ps.setInt(5, s.getServiceId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =============================
    // DELETE SERVICE (SOFT DELETE)
    // =============================
    public void deleteService(int id) {

        String sql = "UPDATE service SET is_deleted = 1 WHERE service_id=?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =============================
    // ADD SERVICE USAGE
    // =============================
    public void addUsage(ServiceUsage u) {

        String sql = "INSERT INTO service_usage(contract_id, service_id, quantity, usage_date, billed) VALUES(?,?,?,?,0)";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, u.getContractId());
            ps.setInt(2, u.getServiceId());
            ps.setBigDecimal(3, u.getQuantity());
            ps.setDate(4, Date.valueOf(u.getUsageDate()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =============================
    // GET SERVICE USAGE BY CONTRACT
    // =============================
    public List<ServiceUsage> getUsageByContract(int contractId) {

        List<ServiceUsage> list = new ArrayList<>();

        String sql = "SELECT * FROM service_usage WHERE contract_id = ?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, contractId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ServiceUsage u = new ServiceUsage();

                u.setUsageId(rs.getInt("usage_id"));
                u.setContractId(rs.getInt("contract_id"));
                u.setServiceId(rs.getInt("service_id"));
                u.setQuantity(rs.getBigDecimal("quantity"));
                u.setUsageDate(rs.getDate("usage_date").toLocalDate());
                u.setBilled(rs.getBoolean("billed"));

                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // GET SERVICE PRICE
    // =============================
    public BigDecimal getServicePrice(int serviceId, LocalDate usageDate) {

        String sql
                = "SELECT TOP 1 ph.price_amount "
                + "FROM service s "
                + "JOIN price_history ph ON s.category_id = ph.category_id "
                + "WHERE s.service_id = ? "
                + "AND ph.effective_from <= ? "
                + "ORDER BY ph.effective_from DESC";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, serviceId);
            ps.setDate(2, Date.valueOf(usageDate));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("price_amount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    // =============================
    // CALCULATE USAGE COST
    // =============================
    public BigDecimal calculateUsageCost(ServiceUsage usage) {

        BigDecimal price = getServicePrice(
                usage.getServiceId(),
                usage.getUsageDate()
        );

        return price.multiply(usage.getQuantity());
    }

    // =============================
    // TOTAL SERVICE COST BY CONTRACT
    // =============================
    public BigDecimal calculateServiceTotal(int contractId) {

        BigDecimal total = BigDecimal.ZERO;

        List<ServiceUsage> list = getUsageByContract(contractId);

        for (ServiceUsage u : list) {

            if (!u.isBilled()) {
                total = total.add(calculateUsageCost(u));
            }

        }

        return total;
    }

    // =============================
    // GET ALL USAGE WITH DETAILS (admin: request list)
    // =============================
    public List<ServiceUsage> getAllUsageWithDetails() {

        List<ServiceUsage> list = new ArrayList<>();

        String sql = "SELECT su.usage_id, su.contract_id, su.service_id, su.quantity, "
                + "su.usage_date, su.billed, "
                + "s.service_name, r.room_number, "
                + "ph.price_amount AS unit_price "
                + "FROM service_usage su "
                + "JOIN service s ON su.service_id = s.service_id "
                + "JOIN contract c ON su.contract_id = c.contract_id "
                + "JOIN room r ON c.room_id = r.room_id "
                + "LEFT JOIN price_history ph ON s.category_id = ph.category_id "
                + "  AND ph.effective_from = ( "
                + "    SELECT TOP 1 effective_from FROM price_history "
                + "    WHERE category_id = s.category_id "
                + "    AND effective_from <= su.usage_date "
                + "    ORDER BY effective_from DESC "
                + "  ) "
                + "ORDER BY su.usage_date DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ServiceUsage u = new ServiceUsage();
                u.setUsageId(rs.getInt("usage_id"));
                u.setContractId(rs.getInt("contract_id"));
                u.setServiceId(rs.getInt("service_id"));
                u.setQuantity(rs.getBigDecimal("quantity"));
                u.setUsageDate(rs.getDate("usage_date").toLocalDate());
                u.setBilled(rs.getBoolean("billed"));
                u.setServiceName(rs.getString("service_name"));
                u.setRoomNumber(rs.getString("room_number"));
                BigDecimal unitPrice = rs.getBigDecimal("unit_price");
                if (unitPrice == null) unitPrice = BigDecimal.ZERO;
                u.setUnitPrice(unitPrice);
                u.setTotalCost(unitPrice.multiply(u.getQuantity()));
                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // GET USAGE BY USER ID (customer: service history)
    // =============================
    public List<ServiceUsage> getUsageByUserId(int userId) {

        List<ServiceUsage> list = new ArrayList<>();

        String sql = "SELECT su.usage_id, su.contract_id, su.service_id, su.quantity, "
                + "su.usage_date, su.billed, "
                + "s.service_name, r.room_number, "
                + "ph.price_amount AS unit_price "
                + "FROM service_usage su "
                + "JOIN service s ON su.service_id = s.service_id "
                + "JOIN contract c ON su.contract_id = c.contract_id "
                + "JOIN room r ON c.room_id = r.room_id "
                + "JOIN contract_user cu ON c.contract_id = cu.contract_id "
                + "LEFT JOIN price_history ph ON s.category_id = ph.category_id "
                + "  AND ph.effective_from = ( "
                + "    SELECT TOP 1 effective_from FROM price_history "
                + "    WHERE category_id = s.category_id "
                + "    AND effective_from <= su.usage_date "
                + "    ORDER BY effective_from DESC "
                + "  ) "
                + "WHERE cu.user_id = ? "
                + "ORDER BY su.usage_date DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ServiceUsage u = new ServiceUsage();
                u.setUsageId(rs.getInt("usage_id"));
                u.setContractId(rs.getInt("contract_id"));
                u.setServiceId(rs.getInt("service_id"));
                u.setQuantity(rs.getBigDecimal("quantity"));
                u.setUsageDate(rs.getDate("usage_date").toLocalDate());
                u.setBilled(rs.getBoolean("billed"));
                u.setServiceName(rs.getString("service_name"));
                u.setRoomNumber(rs.getString("room_number"));
                BigDecimal unitPrice = rs.getBigDecimal("unit_price");
                if (unitPrice == null) unitPrice = BigDecimal.ZERO;
                u.setUnitPrice(unitPrice);
                u.setTotalCost(unitPrice.multiply(u.getQuantity()));
                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // GET SERVICE CATEGORIES (for forms)
    // =============================
    public List<PriceCategory> getServicePriceCategories() {

        List<PriceCategory> list = new ArrayList<>();

        String sql = "SELECT category_id, category_code, category_type, unit "
                + "FROM price_category "
                + "WHERE is_deleted = 0 AND category_type = 'service' "
                + "ORDER BY category_code";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PriceCategory c = new PriceCategory();
                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryCode(rs.getString("category_code"));
                c.setCategoryType(rs.getString("category_type"));
                c.setUnit(rs.getString("unit"));
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // MARK USAGE BILLED
    // =============================
    public void markUsageBilled(int contractId) {

        String sql = "UPDATE service_usage SET billed = 1 WHERE contract_id = ?";

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, contractId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
