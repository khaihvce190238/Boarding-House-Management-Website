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

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            showLogin(request, response);
            return;
        }

        switch (action) {

            case "login":
                showLogin(request, response);
                break;

            case "register":
                showRegister(request, response);
                break;

            case "logout":
                logout(request, response);
                break;

            case "forgetPassword":
                showForgetPassword(request, response);
                break;

            case "changePassword":
                showChangePassword(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ===================== POST =====================
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

            case "changePassword":
                changePassword(request, response);
                break;

            case "verifyReset":
                verifyReset(request, response);
                break;

            case "doResetPassword":
                doResetPassword(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ===================== SHOW FORMS =====================

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    private void showForgetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("phase", 1);
        request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
    }

    private void showChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        request.getRequestDispatcher("/views/customer/changePassword.jsp").forward(request, response);
    }

    // ===================== LOGIN =====================

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.login(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    // ===================== REGISTER =====================

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username  = request.getParameter("username");
        String password  = request.getParameter("password");
        String confirm   = request.getParameter("confirmPassword");
        String fullName  = request.getParameter("fullName");
        String email     = request.getParameter("email");
        String phone     = request.getParameter("phone");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Tên đăng nhập và mật khẩu không được để trống");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (confirm != null && !confirm.isEmpty() && !confirm.equals(password)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.existsByUsername(username.trim())) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setFullName(fullName != null ? fullName.trim() : "");
        user.setEmail(email != null ? email.trim() : "");
        user.setPhone(phone != null ? phone.trim() : "");
        user.setRole("customer");
        user.setImage("default.png");

        boolean success = userDAO.register(user);

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
        } else {
            request.setAttribute("error", "Đăng ký thất bại, vui lòng thử lại");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }

    // ===================== LOGOUT =====================

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
    }

    // ===================== CHANGE PASSWORD =====================

    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String oldPassword  = request.getParameter("oldPassword");
        String newPassword  = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (oldPassword == null || oldPassword.trim().isEmpty()
                || newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/views/customer/changePassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới không khớp");
            request.getRequestDispatcher("/views/customer/changePassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự");
            request.getRequestDispatcher("/views/customer/changePassword.jsp").forward(request, response);
            return;
        }

        boolean success = userDAO.changePassword(user.getUserId(), oldPassword, newPassword);

        if (success) {
            request.setAttribute("success", "Đổi mật khẩu thành công!");
            request.getRequestDispatcher("/views/customer/changePassword.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng");
            request.getRequestDispatcher("/views/customer/changePassword.jsp").forward(request, response);
        }
    }

    // ===================== RESET PASSWORD — STEP 1: VERIFY =====================

    private void verifyReset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email    = request.getParameter("email");

        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập và email");
            request.setAttribute("phase", 1);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
            return;
        }

        User user = userDAO.findByUsernameAndEmail(username.trim(), email.trim());

        if (user == null) {
            request.setAttribute("error", "Tên đăng nhập hoặc email không khớp");
            request.setAttribute("phase", 1);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("resetUserId", user.getUserId());

        request.setAttribute("phase", 2);
        request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
    }

    // ===================== RESET PASSWORD — STEP 2: SET NEW PASSWORD =====================

    private void doResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("resetUserId") == null) {
            request.setAttribute("error", "Phiên xác thực hết hạn, vui lòng thử lại");
            request.setAttribute("phase", 1);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
            return;
        }

        String newPassword  = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            request.setAttribute("phase", 2);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.setAttribute("phase", 2);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            request.setAttribute("phase", 2);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
            return;
        }

        int userId = (int) session.getAttribute("resetUserId");
        boolean success = userDAO.resetPassword(userId, newPassword);

        session.removeAttribute("resetUserId");

        if (success) {
            session.setAttribute("successMessage", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
        } else {
            request.setAttribute("error", "Đặt lại mật khẩu thất bại, vui lòng thử lại");
            request.setAttribute("phase", 1);
            request.getRequestDispatcher("/views/forgetPassword.jsp").forward(request, response);
        }
    }
}
