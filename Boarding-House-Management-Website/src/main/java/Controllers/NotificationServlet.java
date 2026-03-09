package Controllers;

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

    // =============================
    // GET
    // =============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "list":
                listNotifications(request, response);
                break;

            case "create":
                request.getRequestDispatcher("/admin/notifications/createNotification.jsp")
                        .forward(request, response);
                break;

            case "detail":
                showDetail(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            default:
                listNotifications(request, response);
        }
    }

    // =============================
    // POST
    // =============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("notification?action=list");
            return;
        }

        switch (action) {

            case "create":
                createNotification(request, response);
                break;

            case "update":
                updateNotification(request, response);
                break;

            case "delete":
                deleteNotification(request, response);
                break;

            default:
                response.sendRedirect("notification?action=list");
        }
    }

    // =============================
    // LIST + FILTER
    // =============================
    private void listNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contractParam = request.getParameter("contractId");
        String createdParam = request.getParameter("createdBy");
        String keyword = request.getParameter("keyword");

        List<Notification> list;

        if (contractParam != null && !contractParam.isEmpty()) {

            int contractId = Integer.parseInt(contractParam);
            list = notificationDAO.getNotificationByContract(contractId);

        } else if (createdParam != null && !createdParam.isEmpty()) {

            int createdBy = Integer.parseInt(createdParam);
            list = notificationDAO.getNotificationByUser(createdBy);

        } else if (keyword != null && !keyword.isEmpty()) {

            list = notificationDAO.searchNotification(keyword);

        } else {

            list = notificationDAO.getAllNotifications();
        }

        request.setAttribute("notifications", list);

        request.getRequestDispatcher("/admin/notifications/notifications.jsp")
                .forward(request, response);
    }

    // =============================
    // CREATE
    // =============================
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

        Notification n = new Notification(
                createdBy,
                title,
                content,
                targetContractId,
                false
        );

        notificationDAO.insertNotification(n);

        response.sendRedirect("notification?action=list");
    }

    // =============================
    // DETAIL
    // =============================
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Notification n = notificationDAO.getNotificationById(id);

        request.setAttribute("notification", n);

        request.getRequestDispatcher("/admin/notifications/notificationDetail.jsp")
                .forward(request, response);
    }

    // =============================
    // EDIT FORM
    // =============================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Notification n = notificationDAO.getNotificationById(id);

        request.setAttribute("notification", n);

        request.getRequestDispatcher("/admin/notifications/editNotification.jsp")
                .forward(request, response);
    }

    // =============================
    // UPDATE
    // =============================
    private void updateNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("notificationId"));

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        String targetParam = request.getParameter("targetContractId");

        Integer targetContractId = null;

        if (targetParam != null && !targetParam.isEmpty()) {
            targetContractId = Integer.parseInt(targetParam);
        }

        Notification n = new Notification();
        n.setTitle(title);
        n.setContent(content);
        n.setTargetContractId(targetContractId);

        notificationDAO.updateNotification(id, n);

        response.sendRedirect("notification?action=list");
    }

    // =============================
    // DELETE
    // =============================
    private void deleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("notificationId"));

        notificationDAO.deleteNotification(id);

        response.sendRedirect("notification?action=list");
    }
}
