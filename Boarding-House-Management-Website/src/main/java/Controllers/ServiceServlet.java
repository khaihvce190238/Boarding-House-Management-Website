package Controllers;

import DALs.ServiceDAO;
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

    @Override
    public void init() {
        serviceDAO = new ServiceDAO();
    }

    // ===============================
    // GET
    // ===============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            listServices(request, response);
            return;
        }

        switch (action) {

            case "create":
                request.getRequestDispatcher("/admin/services/createService.jsp")
                        .forward(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            case "delete":
                deleteService(request, response);
                break;

            case "detail":
                showDetail(request, response);
                break;

            case "usage":
                listUsage(request, response);
                break;

            case "totalCost":
                calculateTotal(request, response);
                break;

            case "markBilled":
                markBilled(request, response);
                break;

            default:
                listServices(request, response);
        }
    }

    // ===============================
    // POST
    // ===============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        switch (action) {

            case "insert":
                insertService(request, response);
                break;

            case "update":
                updateService(request, response);
                break;

            case "addUsage":
                addUsage(request, response);
                break;
        }
    }

    // ===============================
    // LIST SERVICES
    // ===============================
    private void listServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Service> list = serviceDAO.getAllServices();

        request.setAttribute("services", list);

        request.getRequestDispatcher("/admin/services/services.jsp")
                .forward(request, response);
    }

    // ===============================
    // INSERT
    // ===============================
    private void insertService(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("serviceName");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String description = request.getParameter("description");
        String image = request.getParameter("image");

        Service s = new Service();
        s.setServiceName(name);
        s.setCategoryId(categoryId);
        s.setDescription(description);
        s.setImage(image);

        serviceDAO.insertService(s);

        response.sendRedirect("services");
    }

    // ===============================
    // SHOW EDIT
    // ===============================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Service service = serviceDAO.getServiceById(id);

        request.setAttribute("service", service);

        request.getRequestDispatcher("/admin/services/editService.jsp")
                .forward(request, response);
    }

    // ===============================
    // UPDATE
    // ===============================
    private void updateService(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("serviceId"));

        String name = request.getParameter("serviceName");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String description = request.getParameter("description");
        String image = request.getParameter("image");

        Service s = new Service();
        s.setServiceId(id);
        s.setServiceName(name);
        s.setCategoryId(categoryId);
        s.setDescription(description);
        s.setImage(image);

        serviceDAO.updateService(s);

        response.sendRedirect("services");
    }

    // ===============================
    // DELETE
    // ===============================
    private void deleteService(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        serviceDAO.deleteService(id);

        response.sendRedirect("services");
    }

    // ===============================
    // DETAIL
    // ===============================
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Service service = serviceDAO.getServiceById(id);

        request.setAttribute("service", service);

        request.getRequestDispatcher("/admin/services/serviceDetail.jsp")
                .forward(request, response);
    }

    // ===============================
    // ADD USAGE
    // ===============================
    private void addUsage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));

        LocalDate usageDate = LocalDate.parse(request.getParameter("usageDate"));

        ServiceUsage usage = new ServiceUsage();

        usage.setContractId(contractId);
        usage.setServiceId(serviceId);
        usage.setQuantity(quantity);
        usage.setUsageDate(usageDate);

        serviceDAO.addUsage(usage);

        response.sendRedirect("services?action=usage&contractId=" + contractId);
    }

    // ===============================
    // LIST USAGE
    // ===============================
    private void listUsage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));

        List<ServiceUsage> usageList = serviceDAO.getUsageByContract(contractId);

        request.setAttribute("usageList", usageList);

        request.getRequestDispatcher("/admin/services/serviceDetail.jsp")
                .forward(request, response);
    }

    // ===============================
    // TOTAL COST
    // ===============================
    private void calculateTotal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));

        BigDecimal total = serviceDAO.calculateServiceTotal(contractId);

        request.setAttribute("totalCost", total);

        request.getRequestDispatcher("/admin/services/serviceDetail.jsp")
                .forward(request, response);
    }

    // ===============================
    // MARK BILLED
    // ===============================
    private void markBilled(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));

        serviceDAO.markUsageBilled(contractId);

        response.sendRedirect("services?action=usage&contractId=" + contractId);
    }
}
