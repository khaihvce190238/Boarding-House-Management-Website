/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.NotificationDAO;
import Models.Notification;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class NotificationServlet extends HttpServlet {

    private NotificationDAO notificationDAO;

    @Override
    public void init() {
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "list":
                listAll(request, response);
                break;

            case "byContract":
                listByContract(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {

            case "create":
                createNotification(request, response);
                break;

            case "delete":
                deleteNotification(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int createdBy = Integer.parseInt(request.getParameter("createdBy"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        String targetParam = request.getParameter("targetContractId");
        Integer targetContractId = null;

        if (targetParam != null && !targetParam.isEmpty()) {
            targetContractId = Integer.parseInt(targetParam);
        }

        Notification notification = new Notification(
                createdBy,
                title,
                content,
                targetContractId,
                false
        );

        notificationDAO.insert(notification);

        response.sendRedirect("notification?action=list");
    }

    private void listAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Notification> list = notificationDAO.getAll();

        request.setAttribute("notifications", list);
        request.getRequestDispatcher("/views/notification/list.jsp")
                .forward(request, response);
    }

    private void listByContract(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));

        List<Notification> list = notificationDAO.getByContract(contractId);

        request.setAttribute("notifications", list);
        request.getRequestDispatcher("/views/notification/list.jsp")
                .forward(request, response);
    }

    private void deleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int notificationId = Integer.parseInt(request.getParameter("notificationId"));

        notificationDAO.delete(notificationId);

        response.sendRedirect("notification?action=list");
    }
}
