/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.UserDAO;
import Models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") != null
                ? request.getParameter("action") : "";

        switch (action) {

            case "register":
                doGetRegister(request, response);
                break;

            case "logout":
                doGetLogout(request, response);
                break;

            default:
                doGetLogin(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") != null
                ? request.getParameter("action") : "";

        switch (action) {

            case "login":
                doPostLogin(request, response);
                break;

            case "register":
                doPostRegister(request, response);
                break;
        }
    }

    private void doGetLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/auth/login.jsp")
                .forward(request, response);
    }

    private void doPostLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.login(username, password);

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("account", user);

            // Có thể phân role sau này
            // if (user.getRole().equals("ADMIN")) ...
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } else {
            request.setAttribute("error", "Invalid username or password!");
            request.getRequestDispatcher("/views/auth/login.jsp")
                    .forward(request, response);
        }
    }

    private void doGetRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/auth/register.jsp")
                .forward(request, response);
    }

    /* =====================================================
       POST REGISTER
       ===================================================== */
    private void doPostRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        UserDAO dao = new UserDAO();

        if (dao.existsByUsername(username)) {
            request.setAttribute("error", "Username already exists!");
            request.getRequestDispatcher("/views/auth/register.jsp")
                    .forward(request, response);
            return;
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(password); // DAO sẽ tự hash
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setRole("TENANT"); // default role
        u.setImage(null);

        boolean success = dao.register(u);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/auth");
        } else {
            request.setAttribute("error", "Register failed!");
            request.getRequestDispatcher("/views/auth/register.jsp")
                    .forward(request, response);
        }
    }

    private void doGetLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/auth");
    }
}
