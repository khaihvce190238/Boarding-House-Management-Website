/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DALs;

/**
 *
 * @author huudanh
 */
import Models.Meter;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterDAO extends DBContext {

    /* ==========================
       INSERT
       ========================== */
    public void insert(Meter meter) {
        String sql = "INSERT INTO meter(room_id, period, electricity_old, electricity_new, water_old, water_new) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, meter.getRoomId());
            st.setDate(2, Date.valueOf(meter.getPeriod()));

            setIntegerOrNull(st, 3, meter.getElectricityOld());
            setIntegerOrNull(st, 4, meter.getElectricityNew());
            setIntegerOrNull(st, 5, meter.getWaterOld());
            setIntegerOrNull(st, 6, meter.getWaterNew());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ==========================
       UPDATE
       ========================== */
    public void update(Meter meter) {
        String sql = "UPDATE meter "
                + "SET electricity_old = ?, electricity_new = ?, "
                + "water_old = ?, water_new = ? "
                + "WHERE meter_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            setIntegerOrNull(st, 1, meter.getElectricityOld());
            setIntegerOrNull(st, 2, meter.getElectricityNew());
            setIntegerOrNull(st, 3, meter.getWaterOld());
            setIntegerOrNull(st, 4, meter.getWaterNew());
            st.setInt(5, meter.getMeterId());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ==========================
       GET BY ROOM + PERIOD
       ========================== */
    public Meter getByRoomAndPeriod(int roomId, java.time.LocalDate period) {
        String sql = "SELECT * FROM meter "
                + "WHERE room_id = ? "
                + "AND period = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.setDate(2, Date.valueOf(period));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapMeter(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ==========================
       GET ALL BY ROOM
       ========================== */
    public List<Meter> getByRoom(int roomId) {
        List<Meter> list = new ArrayList<>();
        String sql = "SELECT * FROM meter "
                + "WHERE room_id = ? "
                + "ORDER BY period DESC";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapMeter(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ==========================
       CALCULATE USAGE
       ========================== */
    public int calculateElectricityUsage(Meter m) {
        if (m.getElectricityOld() == null || m.getElectricityNew() == null) {
            return 0;
        }
        return m.getElectricityNew() - m.getElectricityOld();
    }

    public int calculateWaterUsage(Meter m) {
        if (m.getWaterOld() == null || m.getWaterNew() == null) {
            return 0;
        }
        return m.getWaterNew() - m.getWaterOld();
    }

    /* ==========================
       MAP
       ========================== */
    private Meter mapMeter(ResultSet rs) throws SQLException {
        return new Meter(
                rs.getInt("meter_id"),
                rs.getInt("room_id"),
                rs.getDate("period").toLocalDate(),
                (Integer) rs.getObject("electricity_old"),
                (Integer) rs.getObject("electricity_new"),
                (Integer) rs.getObject("water_old"),
                (Integer) rs.getObject("water_new")
        );
    }

    private void setIntegerOrNull(PreparedStatement st, int index, Integer value) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.INTEGER);
        } else {
            st.setInt(index, value);
        }
    }
}
