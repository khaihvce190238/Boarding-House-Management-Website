/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.RoomDAO;
import DALs.FacilityDAO;
import Models.Room;
import Models.Facility;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class RoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") != null
                ? request.getParameter("action") : "";

        switch (action) {

            case "add":
                doGetAdd(request, response);
                break;

            case "update":
                doGetUpdate(request, response);
                break;

            case "delete":
                doGetDelete(request, response);
                break;

            case "detail":
                doGetDetail(request, response);
                break;
            case "guest":   // GUEST LIST
                doGetGuest(request, response);
                break;
            case "guestdetail":   // ✅ GUEST DETAIL (MỚI)
            default:
                doGetList(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") != null
                ? request.getParameter("action") : "";

        switch (action) {

            case "add":
                doPostAdd(request, response);
                break;

            case "update":
                doPostUpdate(request, response);
                break;
        }
    }

    private void doGetList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RoomDAO roomDAO = new RoomDAO();
        List<Room> list = roomDAO.getAll();

        request.setAttribute("rooms", list);

        request.getRequestDispatcher("/views/room/list.jsp")
                .forward(request, response);
    }
// ================= GUEST LIST =================

    private void doGetGuest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RoomDAO dao = new RoomDAO();
        List<Room> list = dao.getAll();

        request.setAttribute("rooms", list);
        request.getRequestDispatcher("/views/guest/listroom.jsp")
                .forward(request, response);
    }

    private void doGetAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FacilityDAO facilityDAO = new FacilityDAO();
        List<Facility> facilities = facilityDAO.getAll();

        request.setAttribute("facilities", facilities);

        request.getRequestDispatcher("/views/room/add.jsp")
                .forward(request, response);
    }

    private void doPostAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String roomNumber = request.getParameter("roomNumber");
        String status = request.getParameter("status");
        String image = request.getParameter("image");

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setStatus(status);
        room.setImage(image);

        RoomDAO dao = new RoomDAO();
        dao.insert(room);

        response.sendRedirect(request.getContextPath() + "/room");
    }

    private void doGetUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        RoomDAO roomDAO = new RoomDAO();
        Room room = roomDAO.getById(id);

        FacilityDAO facilityDAO = new FacilityDAO();
        List<Facility> facilities = facilityDAO.getAll();

        request.setAttribute("room", room);
        request.setAttribute("facilities", facilities);

        request.getRequestDispatcher("/views/room/update.jsp")
                .forward(request, response);
    }

    private void doPostUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String roomNumber = request.getParameter("roomNumber");
        String status = request.getParameter("status");
        String image = request.getParameter("image");

        Room room = new Room();
        room.setRoomId(id);
        room.setRoomNumber(roomNumber);
        room.setStatus(status);
        room.setImage(image);

        RoomDAO dao = new RoomDAO();
        dao.update(room);

        response.sendRedirect(request.getContextPath() + "/room");
    }

    private void doGetDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        RoomDAO dao = new RoomDAO();
        dao.delete(id);

        response.sendRedirect(request.getContextPath() + "/room");
    }

    private void doGetDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        RoomDAO roomDAO = new RoomDAO();
        Room room = roomDAO.getById(id);

        request.setAttribute("room", room);

        request.getRequestDispatcher("/views/room/detail.jsp")
                .forward(request, response);
    }
}
