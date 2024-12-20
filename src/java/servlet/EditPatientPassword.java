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

public class EditPatientPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int patientID;
        String password = request.getParameter("password");
        String retypePassword = request.getParameter("retype-password");

        try {
            patientID = Integer.parseInt(request.getParameter("patientID"));
        } catch (NumberFormatException e) {
            session.setAttribute("passwordError", "Invalid patient ID.");
            response.sendRedirect("profile.jsp");
            return;
        }

        if (password == null || retypePassword == null || password.isEmpty() || retypePassword.isEmpty()) {
            session.setAttribute("passwordError", "Passwords are required.");
            response.sendRedirect("profile.jsp");
            return;
        }

        if (!password.equals(retypePassword)) {
            session.setAttribute("passwordError", "Passwords do not match.");
            response.sendRedirect("profile.jsp");
            return;
        }

        if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{10,}$")) {
            session.setAttribute("passwordError", "Password must be at least 10 characters, contain at least 1 number, and 1 special character.");
            response.sendRedirect("profile.jsp");
            return;
        }

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            boolean isUpdated = patientDao.updatePatientPassword(patientID, password);

            if (isUpdated) {
                session.setAttribute("passwordSuccess", "Password updated successfully.");
            } else {
                session.setAttribute("passwordError", "Failed to update password.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EditPatientPassword.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("passwordError", "An unexpected error occurred.");
        }

        response.sendRedirect("profile.jsp");
    }
}
