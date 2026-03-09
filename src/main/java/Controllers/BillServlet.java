package Controllers;

import DALs.BillDAO;
import Models.Bill;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BillServlet extends HttpServlet {

    private BillDAO billDAO;

    @Override
    public void init() {
        billDAO = new BillDAO();
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
                listBills(request, response);
                break;

            case "create":
                showCreateForm(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            case "detail":
                viewDetail(request, response);
                break;

            case "delete":
                deleteBill(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {

            case "create":
                createBill(request, response);
                break;

            case "update":
                updateBill(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ===============================
    // LIST
    // ===============================
    private void listBills(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Bill> bills = billDAO.getAllBills();

        request.setAttribute("bills", bills);

        request.getRequestDispatcher("/views/admin/bills/bills.jsp")
                .forward(request, response);
    }

    // ===============================
    // SHOW CREATE FORM
    // ===============================
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/admin/bills/createBill.jsp")
                .forward(request, response);
    }

    // ===============================
    // CREATE
    // ===============================
    private void createBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));

        LocalDate period = LocalDate.parse(request.getParameter("period"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

        Bill bill = new Bill();
        bill.setContractId(contractId);
        bill.setPeriod(period);
        bill.setDueDate(dueDate);
        bill.setStatus("unpaid");

        billDAO.insertBill(bill);

        response.sendRedirect("bill?action=list");
    }

    // ===============================
    // SHOW EDIT FORM
    // ===============================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int billId = Integer.parseInt(request.getParameter("id"));

        Bill bill = billDAO.getBillById(billId);

        request.setAttribute("bill", bill);

        request.getRequestDispatcher("/views/admin/bills/editBill.jsp")
                .forward(request, response);
    }

    // ===============================
    // UPDATE
    // ===============================
    private void updateBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int billId = Integer.parseInt(request.getParameter("billId"));

        int contractId = Integer.parseInt(request.getParameter("contractId"));

        LocalDate period = LocalDate.parse(request.getParameter("period"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

        String status = request.getParameter("status");

        Bill bill = new Bill();

        bill.setBillId(billId);
        bill.setContractId(contractId);
        bill.setPeriod(period);
        bill.setDueDate(dueDate);
        bill.setStatus(status);

        billDAO.updateBill(bill);

        response.sendRedirect("bill?action=list");
    }

    // ===============================
    // DETAIL
    // ===============================
    private void viewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int billId = Integer.parseInt(request.getParameter("id"));

        Bill bill = billDAO.getBillById(billId);

        request.setAttribute("bill", bill);

        request.getRequestDispatcher("/views/admin/bills/billDetail.jsp")
                .forward(request, response);
    }

    // ===============================
    // DELETE
    // ===============================
    private void deleteBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int billId = Integer.parseInt(request.getParameter("id"));

        billDAO.deleteBill(billId);

        response.sendRedirect("bill?action=list");
    }
}
