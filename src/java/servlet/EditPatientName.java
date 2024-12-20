package servlet;

import connection.DbConn;
import dao.PatientDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditPatientName extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int patientID = Integer.parseInt(request.getParameter("patientID"));
        String name = request.getParameter("name");
        HttpSession session = request.getSession();

        try {
            if (name != null && !name.isEmpty()) {
                PatientDao patientDao = new PatientDao(DbConn.getConnection());
                boolean isUpdated = patientDao.updatePatientName(patientID, name);

                if (isUpdated) {
                    session.setAttribute("nameSuccess", "Name updated successfully.");
                } else {
                    session.setAttribute("nameError", "Failed to update name.");
                }
            } else {
                session.setAttribute("nameError", "Name cannot be empty.");
            }

            response.sendRedirect("profile.jsp");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EditPatientName.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("nameError", "An unexpected error occurred.");
            response.sendRedirect("profile.jsp");
        }
    }
}
