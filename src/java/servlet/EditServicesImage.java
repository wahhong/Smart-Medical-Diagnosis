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
import javax.servlet.http.Part;

public class EditServicesImage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String serviceIDParam = request.getParameter("id");
        Part imagePart = request.getPart("image");
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();

        if (serviceIDParam == null || serviceIDParam.isEmpty()) {
            request.setAttribute("errorMsg", "Service ID is missing or invalid.");
            dispatcher = request.getRequestDispatcher("editServiceImage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int serviceID = Integer.parseInt(serviceIDParam);

        if (imagePart == null || imagePart.getSize() == 0) {
            request.setAttribute("errorImage", "No image file selected.");
            dispatcher = request.getRequestDispatcher("editServiceImage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            boolean isUpdated = servicesDao.updateServiceImage(serviceID, imagePart);

            if (isUpdated) {
                session.setAttribute("successMsg", "Service image updated successfully.");
            } else {
                request.setAttribute("errorMsg", "Failed to update service image. Please try again.");
            }

            response.sendRedirect("services.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EditServicesImage.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

}
