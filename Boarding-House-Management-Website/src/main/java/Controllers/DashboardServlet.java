/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huuda
 */
import Models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // chưa login
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/views/guest/dashboard.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if ("admin".equalsIgnoreCase(user.getRole())) {

            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard.jsp");

        } else if ("customer".equalsIgnoreCase(user.getRole())) {

            response.sendRedirect(request.getContextPath() + "/views/customer/dashboard.jsp");

        } else {

            response.sendRedirect(request.getContextPath() + "/views/guest/dashboard.jsp");
        }
    }
}
