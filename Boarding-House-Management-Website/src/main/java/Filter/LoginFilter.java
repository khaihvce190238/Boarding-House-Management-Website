package Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();
        boolean loggedIn = (session != null && session.getAttribute("account") != null);

        // ✅ Các đường dẫn được phép truy cập công khai (không cần login)
        boolean isPublicPath =
                path.endsWith("login")
                || path.endsWith("register")
                || path.endsWith("home")           // cho phép trang home
                || path.endsWith("/")              // cho phép đường dẫn gốc
                || path.contains("assets/")        // static files
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".webp")
                || path.endsWith(".gif");

        // ✅ Nếu user đã đăng nhập hoặc đang vào trang public => cho qua
        if (loggedIn || isPublicPath) {
            chain.doFilter(request, response);
        } else {
            // 🚫 Nếu chưa login và vào trang cần quyền truy cập → chuyển hướng đến /login
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần xử lý thêm
    }

    @Override
    public void destroy() {
        // Không cần xử lý thêm
    }
}