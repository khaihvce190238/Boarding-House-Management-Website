/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.MeterDAO;
import DALs.PriceDAO;
import Models.Meter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MeterServlet extends HttpServlet {

    private MeterDAO meterDAO;
    private PriceDAO priceDAO;

    @Override
    public void init() {
        meterDAO = new MeterDAO();
        priceDAO = new PriceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("room"); // quay về phòng nếu chưa chọn
            return;
        }

        switch (action) {

            case "list":
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                List<Meter> list = meterDAO.getByRoom(roomId);
                request.setAttribute("meters", list);
                request.setAttribute("roomId", roomId);
                request.getRequestDispatcher("meter-list.jsp")
                        .forward(request, response);
                break;

            case "create":
                request.setAttribute("roomId",
                        request.getParameter("roomId"));
                request.getRequestDispatcher("meter-form.jsp")
                        .forward(request, response);
                break;

            case "edit":
                int rId = Integer.parseInt(request.getParameter("roomId"));
                LocalDate period = LocalDate.parse(request.getParameter("period"));
                Meter m = meterDAO.getByRoomAndPeriod(rId, period);

                request.setAttribute("meter", m);
                request.getRequestDispatcher("meter-form.jsp")
                        .forward(request, response);
                break;

            case "calculate":
                int room = Integer.parseInt(request.getParameter("roomId"));
                LocalDate p = LocalDate.parse(request.getParameter("period"));

                Meter meter = meterDAO.getByRoomAndPeriod(room, p);

                int elecUsage = meterDAO.calculateElectricityUsage(meter);
                int waterUsage = meterDAO.calculateWaterUsage(meter);

                // ⚡ Giả sử:
                // category_id 1 = electricity
                // category_id 2 = water
                BigDecimal elecPrice
                        = priceDAO.getCurrentPrice(1, p);

                BigDecimal waterPrice
                        = priceDAO.getCurrentPrice(2, p);

                BigDecimal elecTotal
                        = elecPrice.multiply(BigDecimal.valueOf(elecUsage));

                BigDecimal waterTotal
                        = waterPrice.multiply(BigDecimal.valueOf(waterUsage));

                BigDecimal total = elecTotal.add(waterTotal);

                request.setAttribute("meter", meter);
                request.setAttribute("elecUsage", elecUsage);
                request.setAttribute("waterUsage", waterUsage);
                request.setAttribute("elecTotal", elecTotal);
                request.setAttribute("waterTotal", waterTotal);
                request.setAttribute("total", total);

                request.getRequestDispatcher("meter-result.jsp")
                        .forward(request, response);
                break;

            default:
                response.sendRedirect("room");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        int roomId = Integer.parseInt(request.getParameter("roomId"));
        LocalDate period = LocalDate.parse(request.getParameter("period"));

        Integer elecOld = parseInteger(request.getParameter("electricityOld"));
        Integer elecNew = parseInteger(request.getParameter("electricityNew"));
        Integer waterOld = parseInteger(request.getParameter("waterOld"));
        Integer waterNew = parseInteger(request.getParameter("waterNew"));

        Meter meter = new Meter(
                0,
                roomId,
                period,
                elecOld,
                elecNew,
                waterOld,
                waterNew
        );

        if ("insert".equals(action)) {
            meterDAO.insert(meter);
        }

        if ("update".equals(action)) {
            int meterId = Integer.parseInt(request.getParameter("meterId"));
            meter.setMeterId(meterId);
            meterDAO.update(meter);
        }

        response.sendRedirect("meter?action=list&roomId=" + roomId);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }
}
