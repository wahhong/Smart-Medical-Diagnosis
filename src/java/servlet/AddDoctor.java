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

public class AddDoctor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String role = request.getParameter("role");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        String dob = request.getParameter("dob");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        Integer serviceID = request.getParameter("service") != null ? Integer.parseInt(request.getParameter("service")) : null;
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

        if (!password.equals(repassword)) {
            request.setAttribute("errorPassword", "Passwords do not match.");
        } else if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{10,}$")) {
            request.setAttribute("errorPassword", "Password must be at least 10 characters, contain at least 1 number and 1 special character.");
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
            dispatcher = request.getRequestDispatcher("addDoctor.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            if (serviceID == 0) {
                serviceID = null;
            }

            int doctorID = doctorDao.doctorRegister(serviceID, name, email, password, role, phone, gender, dob, status);

            if (doctorID < 0) {
                request.setAttribute("errorEmail", "the email already exist.");
                dispatcher = request.getRequestDispatcher("addDoctor.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                session.setAttribute("successMsg", "Patient has been added successfully.");
            }

            response.sendRedirect("doctor.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
