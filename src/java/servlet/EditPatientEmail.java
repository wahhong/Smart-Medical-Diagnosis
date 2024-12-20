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

public class EditPatientEmail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int patientID = Integer.parseInt(request.getParameter("patientID"));
        String email = request.getParameter("email");
        HttpSession session = request.getSession();

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            session.setAttribute("emailError", "Invalid email format.");

            response.sendRedirect("profile.jsp");
        } else {
            try {

                PatientDao patientDao = new PatientDao(DbConn.getConnection());
                boolean isUpdated = patientDao.updatePatientEmail(patientID, email);

                if (isUpdated) {
                    session.setAttribute("emailSuccess", "Email updated successfully.");
                } else {
                    session.setAttribute("emailError", "Failed to update email.");
                }

                response.sendRedirect("profile.jsp");

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(EditPatientName.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("nameError", "An unexpected error occurred.");
                response.sendRedirect("profile.jsp");
            }
        }
    }
}
