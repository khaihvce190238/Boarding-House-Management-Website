/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.FacilityDAO;
import Models.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class FacilityServlet extends HttpServlet {

    private FacilityDAO facilityDAO;

    @Override
    public void init() {
        facilityDAO = new FacilityDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            List<Facility> list = facilityDAO.getAll();
            request.setAttribute("facilities", list);
            request.getRequestDispatcher("facility-list.jsp")
                    .forward(request, response);
            return;
        }

        switch (action) {

            case "create":
                request.getRequestDispatcher("facility-form.jsp")
                        .forward(request, response);
                break;

            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Facility f = facilityDAO.getById(id);
                request.setAttribute("facility", f);
                request.getRequestDispatcher("facility-form.jsp")
                        .forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                facilityDAO.delete(deleteId);
                response.sendRedirect("facility");
                break;

            default:
                response.sendRedirect("facility");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String name = request.getParameter("facilityName");
        String description = request.getParameter("description");
        String image = request.getParameter("image");

        if ("insert".equals(action)) {

            Facility f = new Facility(
                    0,
                    name,
                    description,
                    image,
                    false
            );

            facilityDAO.insert(f);
            response.sendRedirect("facility");

        } else if ("update".equals(action)) {

            int id = Integer.parseInt(request.getParameter("facilityId"));

            Facility f = new Facility(
                    id,
                    name,
                    description,
                    image,
                    false
            );

            facilityDAO.update(f);
            response.sendRedirect("facility");
        }
    }
}
