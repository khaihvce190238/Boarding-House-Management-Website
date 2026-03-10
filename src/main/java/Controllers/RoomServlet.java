package Controllers;

import DALs.FacilityDAO;
import DALs.RoomDAO;
import Models.Room;
import Models.RoomAmenity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RoomServlet extends HttpServlet {

    private RoomDAO     roomDAO;
    private FacilityDAO facilityDAO;

    @Override
    public void init() {
        roomDAO     = new RoomDAO();
        facilityDAO = new FacilityDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            // ── Customer-facing ──────────────────────────────────────────────
            case "categories":
                showCategories(request, response);
                break;

            case "publicList":
                showPublicList(request, response);
                break;

            case "publicDetail":
                showPublicDetail(request, response);
                break;

            // ── Admin ────────────────────────────────────────────────────────
            case "create":
                showCreateForm(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            case "delete":
                deleteRoom(request, response);
                break;

            case "detail":
                showDetail(request, response);
                break;

            default:
                listRooms(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {

            case "create":
                insertRoom(request, response);
                break;

            case "edit":
                updateRoom(request, response);
                break;
        }
    }

    // ================= CUSTOMER: CATEGORIES =================
    private void showCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Integer> counts = roomDAO.getCountByStatus();
        request.setAttribute("counts", counts);
        request.getRequestDispatcher("/views/customer/roomCategories.jsp")
                .forward(request, response);
    }

    // ================= CUSTOMER: PUBLIC LIST (filter by status) =================
    private void showPublicList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = request.getParameter("status");
        List<Room> rooms;

        if (status != null && !status.trim().isEmpty()) {
            rooms = roomDAO.getByStatus(status.trim());
            request.setAttribute("activeStatus", status.trim());
        } else {
            rooms = roomDAO.getAllRooms();
            request.setAttribute("activeStatus", "all");
        }

        Map<String, Integer> counts = roomDAO.getCountByStatus();
        request.setAttribute("rooms",  rooms);
        request.setAttribute("counts", counts);
        request.getRequestDispatcher("/views/customer/rooms.jsp")
                .forward(request, response);
    }

    // ================= CUSTOMER: PUBLIC DETAIL + AMENITIES =================
    private void showPublicDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Room room = roomDAO.getRoomById(id);
        List<RoomAmenity> amenities = facilityDAO.getAmenitiesByRoomId(id);

        request.setAttribute("room",      room);
        request.setAttribute("amenities", amenities);
        request.getRequestDispatcher("/views/customer/roomDetail.jsp")
                .forward(request, response);
    }

    // ================= LIST =================
    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Room> rooms = roomDAO.getAllRooms();

        request.setAttribute("rooms", rooms);

        request.getRequestDispatcher("/views/admin/rooms/rooms.jsp")
                .forward(request, response);
    }

    // ================= CREATE =================
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/admin/rooms/createRoom.jsp")
                .forward(request, response);
    }

    private void insertRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String roomNumber = request.getParameter("roomNumber");
        String status = request.getParameter("status");
        String image = request.getParameter("image");

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setStatus(status);
        room.setImage(image);

        roomDAO.insertRoom(room);

        response.sendRedirect("room");
    }

    // ================= EDIT =================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Room room = roomDAO.getRoomById(id);

        request.setAttribute("room", room);

        request.getRequestDispatcher("/views/admin/rooms/editRoom.jsp")
                .forward(request, response);
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response)
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

        roomDAO.updateRoom(room);

        response.sendRedirect("room");
    }

    // ================= DELETE =================
    private void deleteRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        roomDAO.deleteRoom(id);

        response.sendRedirect("room");
    }

    // ================= DETAIL =================
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Room room = roomDAO.getRoomById(id);

        request.setAttribute("room", room);

        request.getRequestDispatcher("/views/admin/rooms/roomDetail.jsp")
                .forward(request, response);
    }
}
