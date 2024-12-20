package servlet;

import connection.DbConn;
import dao.PatientDao;
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

public class EditPatientImage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        int patientID = Integer.parseInt(request.getParameter("patientID"));
        Part imagePart = request.getPart("profileImage");

        if (imagePart == null || imagePart.getSize() == 0) {
            session.setAttribute("imageError", "No image file selected.");
            response.sendRedirect("profile.jsp");
            return;
        }

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            boolean isUpdated = patientDao.updatePatientImage(patientID, imagePart);

            if (isUpdated) {
                session.setAttribute("imageSuccess", "Profile picture updated successfully.");
            } else {
                session.setAttribute("imageError", "Failed to update profile picture.");
            }

            response.sendRedirect("profile.jsp");
        } catch (NumberFormatException e) {
            Logger.getLogger(EditPatientImage.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("imageError", "Invalid patient ID.");
            response.sendRedirect("profile.jsp");
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(EditPatientImage.class.getName()).log(Level.SEVERE, null, e);
            session.setAttribute("imageError", "An unexpected error occurred.");
            response.sendRedirect("profile.jsp");
        }
    }
}
