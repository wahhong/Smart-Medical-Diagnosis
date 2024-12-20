package servlet;

import connection.DbConn;
import dao.PatientDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class EditAdPatientImage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("id"));
        Part imagePart = request.getPart("image");

        if (imagePart == null || imagePart.getSize() == 0) {
            session.setAttribute("errorImage", "No image file selected.");
            response.sendRedirect("editPatientImage.jsp?id="+id);
            return;
        }

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            boolean isUpdated = patientDao.updatePatientImage(id, imagePart);

            if (isUpdated) {
                session.setAttribute("successMsg", "Profile picture updated successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to update profile picture.");
            }

            response.sendRedirect("patient.jsp");
        } catch (NumberFormatException e) {
            Logger.getLogger(EditPatientImage.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("errorImage", "Invalid patient ID.");
            response.sendRedirect("editPatientImage.jsp?id="+id);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(EditPatientImage.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("errorImage", "An unexpected error occurred.");
            response.sendRedirect("editPatientImage.jsp?id="+id);
        }
    }
}
