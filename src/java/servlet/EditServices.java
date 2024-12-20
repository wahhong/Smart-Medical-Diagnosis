package servlet;

import connection.DbConn;
import dao.ServicesDao;
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

public class EditServices extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int serviceID = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String desc = request.getParameter("desc");
        String priceInput = request.getParameter("price");
        int aiAccess = Integer.parseInt(request.getParameter("aiAccess"));
        int status = Integer.parseInt(request.getParameter("status"));
        double price = 0.0;
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();
        boolean isValid = true;

        if (name == null || name.isEmpty()) {
            request.setAttribute("errorName", "Service name is required.");
            isValid = false;
        }

        if (desc == null || desc.isEmpty()) {
            request.setAttribute("errorDesc", "Description is required.");
            isValid = false;
        } else if (desc.length() > 255) {
            request.setAttribute("errorDesc", "Service description must not exceed 255 characters.");
            isValid = false;
        }

        if (priceInput == null || priceInput.trim().isEmpty()) {
            request.setAttribute("errorPrice", "Fee is required.");
            isValid = false;
        } else {
            try {
                price = Double.parseDouble(priceInput);
                if (price <= 0) {
                    request.setAttribute("errorPrice", "Fee must be greater than 0.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorPrice", "Invalid Fee.");
                isValid = false;
            }
        }

        request.setAttribute("name", name);
        request.setAttribute("desc", desc);
        request.setAttribute("price", price);
        request.setAttribute("status", status);
        request.setAttribute("aiAccess", aiAccess);

        if (!isValid) {
            dispatcher = request.getRequestDispatcher("editServices.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());

            boolean isInserted = servicesDao.updateService(serviceID, name, desc, price, aiAccess, status);

            if (isInserted) {
                session.setAttribute("successMsg", "Service has been added successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to add Service. Please try again.");
            }

            response.sendRedirect("services.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
