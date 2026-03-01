/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.ServiceDAO;
import DALs.PriceDAO;
import Models.Service;
import Models.ServiceUsage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ServiceServlet extends HttpServlet {

    private ServiceDAO serviceDAO;
    private PriceDAO priceDAO;

    @Override
    public void init() {
        serviceDAO = new ServiceDAO();
        priceDAO = new PriceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            List<Service> list = serviceDAO.getAllServices();
            request.setAttribute("services", list);
            request.getRequestDispatcher("service-list.jsp")
                    .forward(request, response);
            return;
        }

        switch (action) {

            case "create":
                request.getRequestDispatcher("service-form.jsp")
                        .forward(request, response);
                break;

            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Service s = serviceDAO.getServiceById(id);
                request.setAttribute("service", s);
                request.getRequestDispatcher("service-form.jsp")
                        .forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                serviceDAO.deleteService(deleteId);
                response.sendRedirect("service");
                break;

            case "usage":
                int contractId = Integer.parseInt(request.getParameter("contractId"));
                List<ServiceUsage> usageList = serviceDAO.getUsageByContract(contractId);
                request.setAttribute("usageList", usageList);
                request.getRequestDispatcher("usage-list.jsp")
                        .forward(request, response);
                break;

            case "unbilled":
                int cId = Integer.parseInt(request.getParameter("contractId"));
                List<ServiceUsage> unbilled = serviceDAO.getUnbilledUsage(cId);

                BigDecimal total = BigDecimal.ZERO;

                for (ServiceUsage u : unbilled) {
                    BigDecimal price = priceDAO.getCurrentPrice(
                            serviceDAO.getServiceById(u.getServiceId()).getCategoryId(),
                            u.getUsageDate()
                    );

                    BigDecimal cost = price.multiply(u.getQuantity());
                    total = total.add(cost);
                }

                request.setAttribute("usageList", unbilled);
                request.setAttribute("totalAmount", total);
                request.getRequestDispatcher("usage-unbilled.jsp")
                        .forward(request, response);
                break;

            case "markBilled":
                int usageId = Integer.parseInt(request.getParameter("usageId"));
                serviceDAO.markAsBilled(usageId);
                response.sendRedirect("service");
                break;

            default:
                response.sendRedirect("service");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("insert".equals(action)) {

            String name = request.getParameter("serviceName");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");
            String image = request.getParameter("image");

            Service s = new Service(
                    0, name, categoryId, description, image, false
            );

            serviceDAO.insertService(s);
            response.sendRedirect("service");
        } else if ("update".equals(action)) {

            int id = Integer.parseInt(request.getParameter("serviceId"));
            String name = request.getParameter("serviceName");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");
            String image = request.getParameter("image");

            Service s = new Service(
                    id, name, categoryId, description, image, false
            );

            serviceDAO.updateService(s);
            response.sendRedirect("service");
        } else if ("addUsage".equals(action)) {

            int contractId = Integer.parseInt(request.getParameter("contractId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            LocalDate usageDate = LocalDate.parse(request.getParameter("usageDate"));

            ServiceUsage u = new ServiceUsage(
                    0, contractId, serviceId,
                    quantity, usageDate, false
            );

            serviceDAO.insertUsage(u);
            response.sendRedirect("service?action=usage&contractId=" + contractId);
        }
    }
}
