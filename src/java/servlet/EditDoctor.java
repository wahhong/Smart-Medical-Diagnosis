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

public class EditDoctor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer doctorID = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : null;
        String name = request.getParameter("name");
        String role = request.getParameter("role");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        Integer serviceID = request.getParameter("service") != null ? Integer.parseInt(request.getParameter("service")) : null;
        int status = Integer.parseInt(request.getParameter("status"));
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();
        boolean isValid = true;

        if (doctorID == 0) {
            dispatcher = request.getRequestDispatcher("doctor.jsp");
            dispatcher.forward(request, response);
            return;
        }

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
        request.setAttribute("status", status);
        request.setAttribute("role", role);
        request.setAttribute("service", serviceID);

        if (!isValid) {
            dispatcher = request.getRequestDispatcher("editDoctor.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            if (serviceID == 0) {
                serviceID = null;
            }

            boolean isUpdated = doctorDao.updateDoctor(doctorID, serviceID, name, role, email, dob, phone, gender, status);

            if (isUpdated) {
                session.setAttribute("successMsg", "Doctor has been updated successfully.");
            } else {
                request.setAttribute("errorEmail", "the email already exist.");
                dispatcher = request.getRequestDispatcher("editDoctor.jsp");
                dispatcher.forward(request, response);
                return;
            }

            response.sendRedirect("doctor.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
