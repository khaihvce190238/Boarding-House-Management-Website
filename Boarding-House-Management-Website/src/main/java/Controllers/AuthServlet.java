package Controllers;

import DALs.UserDAO;
import Models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("views/guest/home.jsp");
            return;
        }

        switch (action) {

            case "logout":
                logout(request, response);
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

            case "login":
                login(request, response);
                break;

            case "register":
                register(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.login(username, password);

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.sendRedirect("dashboard");

        } else {

            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/views/login.jsp")
                    .forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        if (userDAO.existsByUsername(username)) {

            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/views/register.jsp")
                    .forward(request, response);
            return;
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(request.getParameter("password"));
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole("customer"); // mặc định
        user.setImage("default.png");

        boolean success = userDAO.register(user);

        if (success) {
            response.sendRedirect("views/guest/dashboard.jsp");
        } else {

            request.setAttribute("error", "Registration failed");
            request.getRequestDispatcher("/views/register.jsp")
                    .forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("views/guest/dashboard.jsp");
    }
}
