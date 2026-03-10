package Controllers;

import DALs.FacilityDAO;
import DALs.RoomCategoryDAO;
import DALs.RoomDAO;
import Models.Room;
import Models.RoomAmenity;
import Models.RoomCategory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RoomServlet extends HttpServlet {

    private RoomDAO         roomDAO;
    private RoomCategoryDAO categoryDAO;
    private FacilityDAO     facilityDAO;

    @Override
    public void init() {
        roomDAO     = new RoomDAO();
        categoryDAO = new RoomCategoryDAO();
        facilityDAO = new FacilityDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

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

        List<RoomCategory> categories = categoryDAO.getAllCategoriesWithCount();
        Map<String, Integer> statusCounts = roomDAO.getCountByStatus();

        request.setAttribute("categories",   categories);
        request.setAttribute("statusCounts", statusCounts);
        request.getRequestDispatcher("/views/customer/roomCategories.jsp")
                .forward(request, response);
    }

    // ================= CUSTOMER: PUBLIC LIST =================
    // Supports filtering by categoryId and/or status
    private void showPublicList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryParam = request.getParameter("categoryId");
        String statusParam   = request.getParameter("status");

        List<Room> rooms;
        int    activeCategoryId = 0;
        String activeStatus     = "";

        boolean hasCategory = categoryParam != null && !categoryParam.trim().isEmpty();
        boolean hasStatus   = statusParam   != null && !statusParam.trim().isEmpty();

        if (hasCategory) {
            activeCategoryId = Integer.parseInt(categoryParam.trim());
            if (hasStatus) {
                activeStatus = statusParam.trim();
                rooms = roomDAO.getRoomsByCategoryAndStatus(activeCategoryId, activeStatus);
            } else {
                rooms = roomDAO.getRoomsByCategoryId(activeCategoryId);
            }
        } else if (hasStatus) {
            activeStatus = statusParam.trim();
            rooms = roomDAO.getByStatus(activeStatus);
        } else {
            rooms = roomDAO.getAllRooms();
        }

        List<RoomCategory>   categories   = categoryDAO.getAllCategories();
        Map<String, Integer> statusCounts = roomDAO.getCountByStatus();

        request.setAttribute("rooms",            rooms);
        request.setAttribute("categories",       categories);
        request.setAttribute("statusCounts",     statusCounts);
        request.setAttribute("activeCategoryId", activeCategoryId);
        request.setAttribute("activeStatus",     activeStatus);
        request.getRequestDispatcher("/views/customer/rooms.jsp")
                .forward(request, response);
    }

    // ================= CUSTOMER: PUBLIC DETAIL + AMENITIES =================
    private void showPublicDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Room room = roomDAO.getRoomById(id);
        List<RoomAmenity> amenities = facilityDAO.getAmenitiesByRoomId(id);

        // Fetch full category info for base price display
        RoomCategory category = null;
        if (room != null && room.getCategoryId() > 0) {
            category = categoryDAO.getCategoryById(room.getCategoryId());
        }

        request.setAttribute("room",      room);
        request.setAttribute("amenities", amenities);
        request.setAttribute("category",  category);
        request.getRequestDispatcher("/views/customer/roomDetail.jsp")
                .forward(request, response);
    }

    // ================= LIST (admin) =================
    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Room> rooms = roomDAO.getAllRooms();
        List<RoomCategory> categories = categoryDAO.getAllCategories();
        request.setAttribute("rooms",      rooms);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/admin/rooms/rooms.jsp")
                .forward(request, response);
    }

    // ================= CREATE (admin) =================
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<RoomCategory> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/admin/rooms/createRoom.jsp")
                .forward(request, response);
    }

    private void insertRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String roomNumber    = request.getParameter("roomNumber");
        String status        = request.getParameter("status");
        String image         = request.getParameter("image");
        String categoryParam = request.getParameter("categoryId");

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setStatus(status);
        room.setImage(image);
        if (categoryParam != null && !categoryParam.isEmpty()) {
            room.setCategoryId(Integer.parseInt(categoryParam));
        }
        roomDAO.insertRoom(room);
        response.sendRedirect("room");
    }

    // ================= EDIT (admin) =================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Room room = roomDAO.getRoomById(id);
        List<RoomCategory> categories = categoryDAO.getAllCategories();
        request.setAttribute("room",       room);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/admin/rooms/editRoom.jsp")
                .forward(request, response);
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int    id            = Integer.parseInt(request.getParameter("id"));
        String roomNumber    = request.getParameter("roomNumber");
        String status        = request.getParameter("status");
        String image         = request.getParameter("image");
        String categoryParam = request.getParameter("categoryId");

        Room room = new Room();
        room.setRoomId(id);
        room.setRoomNumber(roomNumber);
        room.setStatus(status);
        room.setImage(image);
        if (categoryParam != null && !categoryParam.isEmpty()) {
            room.setCategoryId(Integer.parseInt(categoryParam));
        }
        roomDAO.updateRoom(room);
        response.sendRedirect("room");
    }

    // ================= DELETE (admin) =================
    private void deleteRoom(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        roomDAO.deleteRoom(id);
        response.sendRedirect("room");
    }

    // ================= DETAIL (admin) =================
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int  id   = Integer.parseInt(request.getParameter("id"));
        Room room = roomDAO.getRoomById(id);
        request.setAttribute("room", room);
        request.getRequestDispatcher("/views/admin/rooms/roomDetail.jsp")
                .forward(request, response);
    }
}
