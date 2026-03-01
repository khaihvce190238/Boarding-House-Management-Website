/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

/**
 *
 * @author huudanh
 */
import DALs.ContractDAO;
import Models.Contract;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContractServlet extends HttpServlet {

    private ContractDAO contractDAO;

    @Override
    public void init() {
        contractDAO = new ContractDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            List<Contract> list = contractDAO.getAll();
            request.setAttribute("contracts", list);
            request.getRequestDispatcher("contract-list.jsp").forward(request, response);
            return;
        }

        switch (action) {

            case "create":
                request.getRequestDispatcher("contract-form.jsp").forward(request, response);
                break;

            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Contract c = contractDAO.getById(id);
                request.setAttribute("contract", c);
                request.getRequestDispatcher("contract-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                contractDAO.delete(deleteId);
                response.sendRedirect("contract");
                break;

            case "terminate":
                int contractId = Integer.parseInt(request.getParameter("id"));
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                contractDAO.terminate(contractId, roomId);
                response.sendRedirect("contract");
                break;

            default:
                response.sendRedirect("contract");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        int roomId = Integer.parseInt(request.getParameter("roomId"));
        LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));

        String endDateStr = request.getParameter("endDate");
        LocalDate endDate = (endDateStr == null || endDateStr.isEmpty())
                ? null
                : LocalDate.parse(endDateStr);

        BigDecimal deposit = new BigDecimal(request.getParameter("deposit"));
        String status = request.getParameter("status");

        if ("insert".equals(action)) {

            Contract c = new Contract(
                    0, roomId, startDate, endDate,
                    deposit, status, false
            );

            contractDAO.insert(c);
            response.sendRedirect("contract");

        } else if ("update".equals(action)) {

            int contractId = Integer.parseInt(request.getParameter("contractId"));

            Contract c = new Contract(
                    contractId, roomId, startDate,
                    endDate, deposit, status, false
            );

            contractDAO.update(c);
            response.sendRedirect("contract");
        }
    }
}
