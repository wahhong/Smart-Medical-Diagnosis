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

public class DeleteService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int serviceID = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();

        try {
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());

            boolean isInserted = servicesDao.deleteService(serviceID);

            if (isInserted) {
                session.setAttribute("successMsg", "Service has been deleted successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to delete Service. Please try again.");
            }

            response.sendRedirect("services.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
