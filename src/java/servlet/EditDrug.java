package servlet;

import connection.DbConn;
import dao.DrugDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditDrug extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String desc = request.getParameter("desc");
        String priceInput = request.getParameter("price");
        String exp = request.getParameter("exp");
        String qtyInput = request.getParameter("qty");
        int status = Integer.parseInt(request.getParameter("status"));
        double price = 0.0;
        int qty = 0;
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();
        boolean isValid = true;

        if (name == null || name.isEmpty()) {
            request.setAttribute("errorName", "Drug name is required.");
            isValid = false;
        }

        if (desc == null || desc.isEmpty()) {
            request.setAttribute("errorDesc", "Description is required.");
            isValid = false;
        } else if (desc.length() > 255) {
            request.setAttribute("errorDesc", "Service description must not exceed 255 characters.");
            isValid = false;
        }

        if (qtyInput == null || qtyInput.trim().isEmpty()) {
            request.setAttribute("errorQty", "Quantity is required.");
            isValid = false;
        } else {
            qty = Integer.parseInt(qtyInput);
            if (qty <= 0) { 
                request.setAttribute("errorQty", "Quantity must be greater than 0.");
                isValid = false;
            }
        }

        if (exp == null || exp.isEmpty()) {
            request.setAttribute("errorExp", "Expire Date is required.");
            isValid = false;
        }

        if (priceInput == null || priceInput.trim().isEmpty()) {
            request.setAttribute("errorPrice", "Price is required.");
            isValid = false;
        } else {
            price = Double.parseDouble(priceInput);
            if (price <= 0) { 
                request.setAttribute("errorPrice", "Price must be greater than 0.");
                isValid = false;
            }
        }

        request.setAttribute("name", name);
        request.setAttribute("desc", desc);
        request.setAttribute("price", price);
        request.setAttribute("exp", exp);
        request.setAttribute("qty", qty);
        request.setAttribute("status", status);
        
        try {
            DrugDao drugDao = new DrugDao(DbConn.getConnection());

            boolean isInserted = drugDao.editDrug(id, name, desc, qty, price, exp, status);

            if (isInserted) {
                session.setAttribute("successMsg", "Drug has been added successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to add Drug. Please try again.");
            }

            response.sendRedirect("drug.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
