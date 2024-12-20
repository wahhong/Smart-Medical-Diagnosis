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

public class EditDrProfilePass extends HttpServlet {
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();
        boolean isValid = true;

        if (!password.equals(repassword)) {
            request.setAttribute("errorPassword", "Passwords do not match.");
            isValid = false;
        } else if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{10,}$")) {
            request.setAttribute("errorPassword", "Password must be at least 10 characters, contain at least 1 number and 1 special character.");
            isValid = false;
        }

        if (!isValid) {
            dispatcher = request.getRequestDispatcher("profile.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());

            boolean isUpdated = doctorDao.doctorUpdatePassward(id, password);

            if (isUpdated) {
                session.setAttribute("successMsg", "Update password successfully.");
            } else {
                session.setAttribute("errorMsg", "Update password unsuccessfully.");
            }

            response.sendRedirect("profile.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
