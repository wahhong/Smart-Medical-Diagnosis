package servlet;

import connection.DbConn;
import dao.DoctorDao;
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

public class EditDoctorImage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Part imagePart = request.getPart("image");
        HttpSession session = request.getSession();
        
        if (imagePart == null || imagePart.getSize() == 0) {
            session.setAttribute("errorImage", "No image file selected.");
            response.sendRedirect("editDoctorImage.jsp?id="+id);
            return;
        }
        
        try {
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            boolean isUpdated = doctorDao.updateDoctorImage(id, imagePart);

            if (isUpdated) {
                session.setAttribute("successMsg", "Profile picture updated successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to update profile picture.");
            }

            response.sendRedirect("doctor.jsp");
        } catch (NumberFormatException e) {
            Logger.getLogger(EditPatientImage.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("imageError", "Invalid doctor ID.");
            response.sendRedirect("editDoctorImage.jsp?id="+id);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(EditPatientImage.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("imageError", "An unexpected error occurred.");
            response.sendRedirect("editDoctorImage.jsp?id="+id);
        }
    }
}
