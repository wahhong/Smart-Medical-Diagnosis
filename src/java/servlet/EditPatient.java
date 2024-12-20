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

public class EditPatient extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String phone = request.getParameter("phone");
        int status = Integer.parseInt(request.getParameter("status"));
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();
        boolean isValid = true;

        if (name == null || name.isEmpty()) {
            request.setAttribute("errorName", "Invalid Name.");
            isValid = false;
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("errorEmail", "Invalid email.");
            isValid = false;
        }

        if (phone == null || !phone.matches("^[0-9]{10,11}$")) {
            request.setAttribute("errorPhone", "Invalid Phone Number.");
            isValid = false;
        }

        if (dob == null || dob.isEmpty()) {
            request.setAttribute("errorDOB", "Date of birth is required.");
            isValid = false;
        }

        request.setAttribute("name", name);
        request.setAttribute("gender", gender);
        request.setAttribute("email", email);
        request.setAttribute("dob", dob);
        request.setAttribute("phone", phone);

        if (!isValid) {
            dispatcher = request.getRequestDispatcher("editPatient.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            boolean isUpdated = patientDao.updatePatientDetails(id, name, email, phone, gender, dob, status);

            if (isUpdated) {
                session.setAttribute("successMsg", "Patient has been updated successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to update patient due to email are used. Please try again.");
            }

            response.sendRedirect("patient.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
