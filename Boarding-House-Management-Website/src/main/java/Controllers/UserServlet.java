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
import java.util.List;

public class UserServlet extends HttpServlet {

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
            action = "list";
        }

        switch (action) {

            case "list":
                listUsers(request, response);
                break;

            case "edit":
                showEdit(request, response);
                break;

            case "delete":
                deleteUser(request, response);
                break;

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

            case "update":
                updateUser(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.login(username, password);

        if (user != null) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/views/login.jsp")
                    .forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.getSession().invalidate();
        response.sendRedirect("login.jsp");
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

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
        user.setRole(request.getParameter("role"));
        user.setImage(request.getParameter("image"));

        boolean success = userDAO.register(user);

        if (success) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Registration failed");
            request.getRequestDispatcher("/views/register.jsp")
                    .forward(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userDAO.getAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/views/customer/list.jsp")
                .forward(request, response);
    }

    private void showEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.getById(id);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/customer/edit.jsp")
                .forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = new User();
        user.setUserId(Integer.parseInt(request.getParameter("userId")));
        user.setUsername(request.getParameter("username"));
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole(request.getParameter("role"));
        user.setImage(request.getParameter("image"));
        user.setPassword(request.getParameter("password")); // nếu rỗng sẽ không update

        userDAO.update(user);

        response.sendRedirect("user?action=list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.delete(id);

        response.sendRedirect("user?action=list");
    }
}
