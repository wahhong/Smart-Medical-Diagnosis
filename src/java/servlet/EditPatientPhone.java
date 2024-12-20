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

public class EditPatientPhone extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int patientID = Integer.parseInt(request.getParameter("patientID"));
        String phone = request.getParameter("phone");
        HttpSession session = request.getSession();

        if (!phone.matches("^[0-9]{10,11}$")) {
            session.setAttribute("phoneError", "Invalid phone number.");
            response.sendRedirect("profile.jsp");
        } else {
            try {
                PatientDao patientDao = new PatientDao(DbConn.getConnection());
                boolean isUpdated = patientDao.updatePatientPhone(patientID, phone);

                if (isUpdated) {
                    session.setAttribute("phoneSuccess", "Phone updated successfully.");
                } else {
                    session.setAttribute("phoneError", "Failed to update phone.");
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
