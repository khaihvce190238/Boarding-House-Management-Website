/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.BillDAO;
import DALs.PriceDAO;
import Models.Bill;
import Models.BillItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillServlet extends HttpServlet {

    private BillDAO billDAO;
    private PriceDAO priceDAO;

    @Override
    public void init() {
        billDAO = new BillDAO();
        priceDAO = new PriceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {

            case "list":
                listBills(request, response);
                break;

            case "view":
                viewBillItems(request, response);
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

            case "create":
                createBill(request, response);
                break;

            case "addItem":
                addBillItem(request, response);
                break;

            case "updateStatus":
                updateStatus(request, response);
                break;

            case "delete":
                deleteBill(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));
        LocalDate period = LocalDate.parse(request.getParameter("period"));
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

        Bill bill = new Bill();
        bill.setContractId(contractId);
        bill.setPeriod(period);
        bill.setDueDate(dueDate);

        int billId = billDAO.insertBill(bill);

        response.sendRedirect("bill?action=view&billId=" + billId);
    }

    private void addBillItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int billId = Integer.parseInt(request.getParameter("billId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));

        // Lấy giá hiện hành
        BigDecimal unitPrice = priceDAO.getCurrentPrice(categoryId, LocalDate.now());

        BillItem item = new BillItem();
        item.setBillId(billId);
        item.setCategoryId(categoryId);
        item.setDescription(request.getParameter("description"));
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);

        billDAO.insertBillItem(item);

        // Cập nhật tổng tiền
        billDAO.updateTotalAmount(billId);

        response.sendRedirect("bill?action=view&billId=" + billId);
    }

    private void listBills(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int contractId = Integer.parseInt(request.getParameter("contractId"));
        List<Bill> bills = billDAO.getBillsByContract(contractId);

        request.setAttribute("bills", bills);
        request.getRequestDispatcher("/views/bill/list.jsp").forward(request, response);
    }

    private void viewBillItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int billId = Integer.parseInt(request.getParameter("billId"));
        List<BillItem> items = billDAO.getItemsByBill(billId);

        request.setAttribute("items", items);
        request.setAttribute("billId", billId);
        request.getRequestDispatcher("/views/bill/detail.jsp").forward(request, response);
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int billId = Integer.parseInt(request.getParameter("billId"));
        String status = request.getParameter("status");

        billDAO.updateStatus(billId, status);

        response.sendRedirect("bill?action=view&billId=" + billId);
    }

    private void deleteBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int billId = Integer.parseInt(request.getParameter("billId"));

        billDAO.deleteBill(billId);

        response.sendRedirect("bill?action=list&contractId="
                + request.getParameter("contractId"));
    }
}
