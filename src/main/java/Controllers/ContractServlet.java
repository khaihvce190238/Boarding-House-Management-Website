package Controllers;

import DALs.BillDAO;
import DALs.ContractDAO;
import DALs.RoomDAO;
import DALs.UserDAO;
import Models.Bill;
import Models.Contract;
import Models.ContractUser;
import Models.Room;
import Models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContractServlet extends HttpServlet {

    private ContractDAO contractDAO;
    private RoomDAO     roomDAO;
    private UserDAO     userDAO;
    private BillDAO     billDAO;

    private static final int PAGE_SIZE = 10;

    @Override
    public void init() {
        contractDAO = new ContractDAO();
        roomDAO     = new RoomDAO();
        userDAO     = new UserDAO();
        billDAO     = new BillDAO();
    }

    // =====================================================================
    // GET
    // =====================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = isAdminOrStaff(request) ? "list" : "mycontract";
        }

        switch (action) {

            // ── ADMIN ──────────────────────────────────────────────────────
            case "list":
                listContracts(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "detail":
                showDetail(request, response);
                break;
            case "terminate":
                terminateContract(request, response);
                break;
            case "delete":
                deleteContract(request, response);
                break;
            case "addTenant":
                showAddTenantForm(request, response);
                break;
            case "removeTenant":
                removeTenant(request, response);
                break;

            // ── CUSTOMER ──────────────────────────────────────────────────
            case "mycontract":
                myContracts(request, response);
                break;
            case "mydetail":
                myContractDetail(request, response);
                break;
            case "signContract":
                showSignContractForm(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/contract?action=list");
        }
    }

    // =====================================================================
    // POST
    // =====================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "insert":
                insertContract(request, response);
                break;
            case "customerSign":
                customerSignContract(request, response);
                break;
            case "update":
                updateContract(request, response);
                break;
            case "addTenant":
                addTenant(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/contract?action=list");
        }
    }

    // =====================================================================
    // ADMIN – LIST
    // =====================================================================
    private void listContracts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        String statusFilter = request.getParameter("status");
        String search       = request.getParameter("search");

        List<Contract> all = contractDAO.getAllWithDetails(statusFilter, search);

        // stats
        long activeCount     = all.stream().filter(c -> "active".equals(c.getStatus())).count();
        long terminatedCount = all.stream().filter(c -> "terminated".equals(c.getStatus())).count();

        // pagination
        int totalItems = all.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / PAGE_SIZE));
        int page = parsePage(request.getParameter("page"), totalPages);

        int from = (page - 1) * PAGE_SIZE;
        int to   = Math.min(from + PAGE_SIZE, totalItems);
        List<Contract> contracts = (from < totalItems) ? all.subList(from, to) : java.util.Collections.emptyList();

        request.setAttribute("contracts",       contracts);
        request.setAttribute("activeCount",     activeCount);
        request.setAttribute("terminatedCount", terminatedCount);
        request.setAttribute("totalItems",      totalItems);
        request.setAttribute("totalPages",      totalPages);
        request.setAttribute("currentPage",     page);
        request.setAttribute("pageSize",        PAGE_SIZE);
        request.setAttribute("statusFilter",    statusFilter);
        request.setAttribute("search",          search);

        forward(request, response, "/views/admin/contracts/contracts.jsp");
    }

    // =====================================================================
    // ADMIN – CREATE
    // =====================================================================
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        List<Room> availableRooms = roomDAO.getByStatus("available");
        List<User> customers      = userDAO.getAllCustomers(null, "active");

        request.setAttribute("availableRooms", availableRooms);
        request.setAttribute("customers",      customers);
        forward(request, response, "/views/admin/contracts/createContract.jsp");
    }

    private void insertContract(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        try {
            int        roomId    = Integer.parseInt(request.getParameter("roomId"));
            LocalDate  startDate = LocalDate.parse(request.getParameter("startDate"));
            String     endStr    = request.getParameter("endDate");
            LocalDate  endDate   = (endStr == null || endStr.isEmpty()) ? null : LocalDate.parse(endStr);
            BigDecimal deposit   = new BigDecimal(request.getParameter("deposit").replaceAll(",", ""));
            int        userId    = Integer.parseInt(request.getParameter("primaryUserId"));

            // One active contract per tenant at a time
            if (userId > 0 && contractDAO.hasActiveContract(userId)) {
                flash(request, "contractError",
                        "This tenant already has an active contract. Terminate it first before creating a new one.");
                response.sendRedirect(request.getContextPath() + "/contract?action=create");
                return;
            }

            Contract c = new Contract(0, roomId, startDate, endDate, deposit, "active", false);
            int newId = contractDAO.insertWithTenant(c, userId);

            if (newId > 0) {
                flash(request, "contractSuccess", "Contract #" + newId + " created successfully!");
            } else {
                flash(request, "contractError", "Failed to create contract. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            flash(request, "contractError", "Invalid input: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/contract?action=list");
    }

    // =====================================================================
    // CUSTOMER – SIGN CONTRACT (POST)
    // =====================================================================
    private void customerSignContract(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = currentUser(request);
        if (user == null) { response.sendRedirect(request.getContextPath() + "/auth?action=login"); return; }

        // One active contract per person at a time
        if (contractDAO.hasActiveContract(user.getUserId())) {
            flash(request, "contractError",
                    "You already have an active contract. Please terminate your current contract before signing a new one.");
            response.sendRedirect(request.getContextPath() + "/contract?action=mycontract");
            return;
        }

        try {
            int        roomId    = Integer.parseInt(request.getParameter("roomId"));
            LocalDate  startDate = LocalDate.parse(request.getParameter("startDate"));
            String     endStr    = request.getParameter("endDate");
            LocalDate  endDate   = (endStr == null || endStr.isEmpty()) ? null : LocalDate.parse(endStr);
            BigDecimal deposit   = new BigDecimal(request.getParameter("deposit").replaceAll(",", ""));

            Contract c = new Contract(0, roomId, startDate, endDate, deposit, "active", false);
            int newId = contractDAO.insertWithTenant(c, user.getUserId());

            if (newId > 0) {
                flash(request, "contractSuccess", "Contract signed! Your contract #" + newId + " is now active.");
                response.sendRedirect(request.getContextPath() + "/contract?action=mydetail&id=" + newId);
            } else {
                flash(request, "contractError", "Could not sign contract. The room may no longer be available.");
                response.sendRedirect(request.getContextPath() + "/contract?action=signContract&roomId=" + roomId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flash(request, "contractError", "Invalid input: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/contract?action=signContract");
        }
    }

    // =====================================================================
    // ADMIN – EDIT
    // =====================================================================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        int id = Integer.parseInt(request.getParameter("id"));
        Contract c = contractDAO.getDetailById(id);
        if (c == null) { response.sendRedirect(request.getContextPath() + "/contract?action=list"); return; }

        request.setAttribute("contract", c);
        forward(request, response, "/views/admin/contracts/editContract.jsp");
    }

    private void updateContract(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        try {
            int        contractId = Integer.parseInt(request.getParameter("contractId"));
            int        roomId     = Integer.parseInt(request.getParameter("roomId"));
            LocalDate  startDate  = LocalDate.parse(request.getParameter("startDate"));
            String     endStr     = request.getParameter("endDate");
            LocalDate  endDate    = (endStr == null || endStr.isEmpty()) ? null : LocalDate.parse(endStr);
            BigDecimal deposit    = new BigDecimal(request.getParameter("deposit").replaceAll(",", ""));
            String     status     = request.getParameter("status");

            Contract c = new Contract(contractId, roomId, startDate, endDate, deposit, status, false);
            contractDAO.update(c);
            flash(request, "contractSuccess", "Contract updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            flash(request, "contractError", "Update failed: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/contract?action=list");
    }

    // =====================================================================
    // ADMIN – DETAIL
    // =====================================================================
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        int id = Integer.parseInt(request.getParameter("id"));
        Contract c = contractDAO.getDetailById(id);
        if (c == null) { response.sendRedirect(request.getContextPath() + "/contract?action=list"); return; }

        List<ContractUser> tenants = contractDAO.getTenantsWithInfo(id);
        List<Bill>         bills   = billDAO.getBillsByContractId(id);
        List<User>         customers = userDAO.getAllCustomers(null, "active");

        request.setAttribute("contract",  c);
        request.setAttribute("tenants",   tenants);
        request.setAttribute("bills",     bills);
        request.setAttribute("customers", customers);
        forward(request, response, "/views/admin/contracts/contractDetail.jsp");
    }

    // =====================================================================
    // ADMIN – TERMINATE
    // =====================================================================
    private void terminateContract(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        int contractId = Integer.parseInt(request.getParameter("id"));
        int roomId     = Integer.parseInt(request.getParameter("roomId"));
        contractDAO.terminate(contractId, roomId);
        flash(request, "contractSuccess", "Contract #" + contractId + " has been terminated.");
        response.sendRedirect(request.getContextPath() + "/contract?action=list");
    }

    // =====================================================================
    // ADMIN – DELETE
    // =====================================================================
    private void deleteContract(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        int id = Integer.parseInt(request.getParameter("id"));
        contractDAO.delete(id);
        flash(request, "contractSuccess", "Contract deleted.");
        response.sendRedirect(request.getContextPath() + "/contract?action=list");
    }

    // =====================================================================
    // ADMIN – CO-TENANTS
    // =====================================================================
    private void showAddTenantForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        int id = Integer.parseInt(request.getParameter("id"));
        Contract c = contractDAO.getDetailById(id);
        List<ContractUser> tenants   = contractDAO.getTenantsWithInfo(id);
        List<User>         customers = userDAO.getAllCustomers(null, "active");

        request.setAttribute("contract",  c);
        request.setAttribute("tenants",   tenants);
        request.setAttribute("customers", customers);
        forward(request, response, "/views/admin/contracts/manageTenants.jsp");
    }

    private void addTenant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        try {
            int       contractId = Integer.parseInt(request.getParameter("contractId"));
            int       userId     = Integer.parseInt(request.getParameter("userId"));
            String    role       = request.getParameter("role");
            String    joinedStr  = request.getParameter("joinedAt");
            LocalDate joinedAt   = (joinedStr == null || joinedStr.isEmpty()) ? LocalDate.now() : LocalDate.parse(joinedStr);

            // One active contract per person at a time
            if (contractDAO.hasActiveContract(userId)) {
                flash(request, "contractError",
                        "This person already has an active contract. They must leave their current contract first.");
                response.sendRedirect(request.getContextPath() + "/contract?action=addTenant&id=" + contractId);
                return;
            }

            boolean ok = contractDAO.addTenant(contractId, userId, role, joinedAt);
            if (ok) {
                flash(request, "contractSuccess", "Tenant added successfully!");
            } else {
                flash(request, "contractError", "Tenant already exists in this contract.");
            }
            response.sendRedirect(request.getContextPath() + "/contract?action=addTenant&id=" + contractId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/contract?action=list");
        }
    }

    private void removeTenant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdminOrStaff(request)) { redirect401(request, response); return; }

        int contractId = Integer.parseInt(request.getParameter("contractId"));
        int userId     = Integer.parseInt(request.getParameter("userId"));
        contractDAO.removeTenant(contractId, userId);
        flash(request, "contractSuccess", "Tenant removed from contract.");
        response.sendRedirect(request.getContextPath() + "/contract?action=addTenant&id=" + contractId);
    }

    // =====================================================================
    // CUSTOMER – MY CONTRACTS
    // =====================================================================
    private void myContracts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = currentUser(request);
        if (user == null) { response.sendRedirect(request.getContextPath() + "/auth?action=login"); return; }

        List<Contract> contracts = contractDAO.getContractsByUserId(user.getUserId());
        request.setAttribute("contracts", contracts);
        forward(request, response, "/views/customer/contracts.jsp");
    }

    // =====================================================================
    // CUSTOMER – MY CONTRACT DETAIL
    // =====================================================================
    private void myContractDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = currentUser(request);
        if (user == null) { response.sendRedirect(request.getContextPath() + "/auth?action=login"); return; }

        int id = Integer.parseInt(request.getParameter("id"));
        Contract c = contractDAO.getDetailById(id);

        // Security: verify this user belongs to this contract
        List<ContractUser> tenants = contractDAO.getTenantsWithInfo(id);
        boolean belongs = tenants.stream().anyMatch(t -> t.getUserId() == user.getUserId());
        if (c == null || !belongs) {
            response.sendRedirect(request.getContextPath() + "/contract?action=mycontract");
            return;
        }

        List<Bill> bills = billDAO.getBillsByContractId(id);
        request.setAttribute("contract", c);
        request.setAttribute("tenants",  tenants);
        request.setAttribute("bills",    bills);
        forward(request, response, "/views/customer/contractDetail.jsp");
    }

    // =====================================================================
    // CUSTOMER – SIGN CONTRACT (browse available rooms → select → sign)
    // =====================================================================
    private void showSignContractForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = currentUser(request);
        if (user == null) { response.sendRedirect(request.getContextPath() + "/auth?action=login"); return; }

        // Pass active-contract flag so JSP can warn/block
        boolean alreadyHasContract = contractDAO.hasActiveContract(user.getUserId());
        request.setAttribute("alreadyHasContract", alreadyHasContract);

        String roomIdStr = request.getParameter("roomId");
        if (roomIdStr != null) {
            Room room = roomDAO.getRoomById(Integer.parseInt(roomIdStr));
            request.setAttribute("room", room);
        } else {
            List<Room> availableRooms = roomDAO.getByStatus("available");
            request.setAttribute("availableRooms", availableRooms);
        }
        forward(request, response, "/views/customer/signContract.jsp");
    }

    // =====================================================================
    // HELPERS
    // =====================================================================
    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    private void flash(HttpServletRequest req, String key, String msg) {
        req.getSession().setAttribute(key, msg);
    }

    private void redirect401(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        User u = currentUser(req);
        if (u == null) {
            // Not logged in → go to login
            res.sendRedirect(req.getContextPath() + "/auth?action=login");
        } else {
            // Logged in but not authorized → send to customer area with error
            flash(req, "contractError", "You don't have permission to access that page.");
            res.sendRedirect(req.getContextPath() + "/contract?action=mycontract");
        }
    }

    private int parsePage(String param, int max) {
        try { int p = Integer.parseInt(param); return Math.min(Math.max(p, 1), max); }
        catch (Exception e) { return 1; }
    }

    private User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return (s != null) ? (User) s.getAttribute("user") : null;
    }

    private boolean isAdminOrStaff(HttpServletRequest req) {
        User u = currentUser(req);
        if (u == null) return false;
        return "admin".equalsIgnoreCase(u.getRole()) || "staff".equalsIgnoreCase(u.getRole());
    }
}
