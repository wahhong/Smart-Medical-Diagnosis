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

public class SetPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        int id = Integer.parseInt(request.getParameter("id"));
        RequestDispatcher dispatcher = null;
        String errorPassword = "";
        HttpSession session = request.getSession();
        boolean updatePass = false;

        if (!password.equals(confirmPassword)) {
            errorPassword = "Passwords do not match.";
        } else if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{10,}$")) {
            errorPassword = "Password must be at least 10 characters, contain at least 1 number and 1 special character.";
        }

        if (!"".equals(errorPassword)) {
            session.setAttribute("errorPassword", errorPassword);
            response.sendRedirect("setPassword.jsp?patientID="+id);
            return;
        } else {
            try {
                PatientDao patientDao = new PatientDao(DbConn.getConnection());
                updatePass = patientDao.setPatientPassword(id, password);

                if (updatePass != false) {
                    request.removeAttribute("errorPassword");
                    response.sendRedirect("login.jsp");
                    return;
                }
            } catch (ClassNotFoundException | SQLException ex) {
                session.setAttribute("msgError", "Error occur");
                response.sendRedirect("home.jsp");
            }
        }
    }
}
