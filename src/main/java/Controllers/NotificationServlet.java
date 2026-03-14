package Controllers;

import DALs.NotificationDAO;
import Models.Notification;
import Models.User;

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

    // =====================================================================
    // GET
    // =====================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "publicList";

        switch (action) {

            // ── Customer-facing ──────────────────────────────────────────
            case "publicList":
                showPublicList(request, response);
                break;

            case "publicDetail":
                showPublicDetail(request, response);
                break;

            // ── Admin / staff ────────────────────────────────────────────
            case "list":
                listNotifications(request, response);
                break;

            case "create":
                request.getRequestDispatcher("/admin/notifications/createNotification.jsp")
                        .forward(request, response);
                break;

            case "detail":
                showAdminDetail(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            default:
                showPublicList(request, response);
        }
    }

    // =====================================================================
    // POST
    // =====================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("notification");
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
                response.sendRedirect("notification");
        }
    }

    // =====================================================================
    // CUSTOMER: list (broadcast + user-relevant)
    // =====================================================================
    private void showPublicList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        List<Notification> notifications;

        if (user != null &&
                ("admin".equals(user.getRole()) || "staff".equals(user.getRole()))) {
            // Admin/staff see everything
            notifications = notificationDAO.getAllNotifications();
        } else if (user != null) {
            // Customer sees broadcast + their contract notifications
            notifications = notificationDAO.getNotificationsForUser(user.getUserId());
        } else {
            // Unauthenticated — only broadcast
            notifications = notificationDAO.getAllNotifications();
        }

        // Filter by type: broadcast | targeted | (empty = all)
        String typeFilter = request.getParameter("type");
        if ("broadcast".equals(typeFilter)) {
            notifications.removeIf(n -> !n.isBroadcast());
        } else if ("targeted".equals(typeFilter)) {
            notifications.removeIf(Notification::isBroadcast);
        }

        request.setAttribute("notifications", notifications);
        request.setAttribute("typeFilter",    typeFilter != null ? typeFilter : "");
        request.getRequestDispatcher("/views/customer/notifications.jsp")
                .forward(request, response);
    }

    // =====================================================================
    // CUSTOMER: detail
    // =====================================================================
    private void showPublicDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Notification n = notificationDAO.getNotificationById(id);
        request.setAttribute("notification", n);
        request.getRequestDispatcher("/views/customer/notificationDetail.jsp")
                .forward(request, response);
    }

    // =====================================================================
    // ADMIN: list with filters
    // =====================================================================
    private void listNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contractParam = request.getParameter("contractId");
        String createdParam  = request.getParameter("createdBy");
        String keyword       = request.getParameter("keyword");

        List<Notification> list;

        if (contractParam != null && !contractParam.isEmpty()) {
            list = notificationDAO.getNotificationByContract(Integer.parseInt(contractParam));
        } else if (createdParam != null && !createdParam.isEmpty()) {
            list = notificationDAO.getNotificationByUser(Integer.parseInt(createdParam));
        } else if (keyword != null && !keyword.isEmpty()) {
            list = notificationDAO.searchNotification(keyword);
        } else {
            list = notificationDAO.getAllNotifications();
        }

        request.setAttribute("notifications", list);
        request.getRequestDispatcher("/admin/notifications/notifications.jsp")
                .forward(request, response);
    }

    // =====================================================================
    // ADMIN: detail
    // =====================================================================
    private void showAdminDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Notification n = notificationDAO.getNotificationById(id);
        request.setAttribute("notification", n);
        request.getRequestDispatcher("/admin/notifications/notificationDetail.jsp")
                .forward(request, response);
    }

    // =====================================================================
    // ADMIN: edit form
    // =====================================================================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Notification n = notificationDAO.getNotificationById(id);
        request.setAttribute("notification", n);
        request.getRequestDispatcher("/admin/notifications/editNotification.jsp")
                .forward(request, response);
    }

    // =====================================================================
    // CREATE
    // =====================================================================
    private void createNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int    createdBy = Integer.parseInt(request.getParameter("createdBy"));
        String title     = request.getParameter("title");
        String content   = request.getParameter("content");
        String targetParam = request.getParameter("targetContractId");
        Integer targetContractId = (targetParam != null && !targetParam.isEmpty())
                                   ? Integer.parseInt(targetParam) : null;

        notificationDAO.insertNotification(
                new Notification(createdBy, title, content, targetContractId, false));
        response.sendRedirect("notification?action=list");
    }

    // =====================================================================
    // UPDATE
    // =====================================================================
    private void updateNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int    id      = Integer.parseInt(request.getParameter("notificationId"));
        String title   = request.getParameter("title");
        String content = request.getParameter("content");
        String tp      = request.getParameter("targetContractId");
        Integer target = (tp != null && !tp.isEmpty()) ? Integer.parseInt(tp) : null;

        Notification n = new Notification();
        n.setTitle(title);
        n.setContent(content);
        n.setTargetContractId(target);

        notificationDAO.updateNotification(id, n);
        response.sendRedirect("notification?action=list");
    }

    // =====================================================================
    // DELETE
    // =====================================================================
    private void deleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("notificationId"));
        notificationDAO.deleteNotification(id);
        response.sendRedirect("notification?action=list");
    }
}
